package com.proj.gui.controller;

import com.proj.gui.service.UnitermHttpClient;
import com.proj.gui.util.TexUtils;
import com.proj.masi.dto.request.SaveTransformRequest;
import com.proj.masi.dto.request.TransformRequest;
import com.proj.masi.dto.response.TransformResultDto;
import com.proj.masi.dto.response.UnitermDefDto;
import com.proj.masi.dto.structure.*;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class MainController {


    @FXML private TableView<UnitermDefDto> tableView;
    @FXML private TableColumn<UnitermDefDto, String> colName;
    @FXML private TableColumn<UnitermDefDto, String> colDesc;

    @FXML private TableView<UnitermDefDto> tableView1;
    @FXML private TableColumn<UnitermDefDto, String> colName1;
    @FXML private TableColumn<UnitermDefDto, String> colDesc1;

    @FXML private TableView<TransformResultDto> resultsTable;
    @FXML private TableColumn<TransformResultDto, String> colResName;
    @FXML private TableColumn<TransformResultDto, String> colResDesc;

    @FXML private WebView  webView;
    @FXML private TreeView<String> astView;
    @FXML private Button btnDeleteResult;

    private final UnitermHttpClient client = new UnitermHttpClient();
    private TermDto currentStructure;
    private UUID lastBaseId;
    private UUID lastReplacementId;
    private List<Integer> lastPath;

    @FXML
    private void initialize() {
        initTable(tableView,  colName,  colDesc,  this::display);
        initTable(tableView1, colName1, colDesc1, this::display);
        initResultTable(colResName, colResDesc);
        refreshList();
        refreshResults();

    }

    private static void initTable(TableView<UnitermDefDto> tv,
                                  TableColumn<UnitermDefDto,String> c1,
                                  TableColumn<UnitermDefDto,String> c2,
                                  Consumer<TermDto> onSelect) {

        c1.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().name()));
        c2.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().description()));

        tv.getSelectionModel().selectedItemProperty()
                .addListener((obs, o, s) -> {
                    if (s != null) onSelect.accept(s.structure());
                });

        tv.setOnMouseClicked(evt -> {
            var sel = tv.getSelectionModel().getSelectedItem();
            if (sel != null) onSelect.accept(sel.structure());
        });
    }

    private void initResultTable(TableColumn<TransformResultDto,String> c1,
                                 TableColumn<TransformResultDto,String> c2) {
        c1.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().name()));
        c2.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().description()));
    }


    @FXML private void onRefresh(){
        refreshList();
        refreshResults();
    }

    @FXML
    private void onTransform() {
        UnitermDefDto baseDef = tableView.getSelectionModel().getSelectedItem();
        UnitermDefDto replDef = tableView1.getSelectionModel().getSelectedItem();

        if (baseDef == null || replDef == null) {
            new Alert(Alert.AlertType.WARNING,
                    "Najpierw zaznacz sekwencjonowanie oraz paralelę.",
                    ButtonType.OK).showAndWait();
            return;
        }

        if (!(baseDef.structure() instanceof SequenceDto seq)) {
            new Alert(Alert.AlertType.ERROR,
                    "Zaznaczony base nie jest sekwencją!",
                    ButtonType.OK).showAndWait();
            return;
        }
        if (!(replDef.structure() instanceof ParallelDto)) {
            new Alert(Alert.AlertType.ERROR,
                    "Zaznaczony replacement nie jest paralelą!",
                    ButtonType.OK).showAndWait();
            return;
        }

        int size = seq.children().size();
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Integer integer = i;
            indices.add(integer);
        }

        ChoiceDialog<Integer> idxDialog = new ChoiceDialog<>(0, indices);
        idxDialog.initOwner(tableView.getScene().getWindow());
        idxDialog.setTitle("Wybierz pozycję");
        idxDialog.setHeaderText("Podmień element sekwencji");
        idxDialog.setContentText("Index (0–" + (size-1) + "):");

        idxDialog.showAndWait().ifPresent(chosenIdx -> {
            lastBaseId        = baseDef.id();
            lastReplacementId = replDef.id();
            lastPath          = List.of(chosenIdx);

            try {
                var req = new TransformRequest(
                        lastBaseId,
                        lastReplacementId,
                        lastPath
                );
                currentStructure = client.transform(req);
                display(currentStructure);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            } catch (IOException ioe) {
                showError(ioe);
            }
        });
    }

    @FXML
    private void onSaveCustom() {
        if (currentStructure == null) return;

        var req = new SaveTransformRequest(
                "result_" + UUID.randomUUID().toString().substring(0,5),
                "Wynik przekształcenia",
                currentStructure
        );
        try {
            TransformResultDto result = client.saveTransform(req);
            new Alert(Alert.AlertType.INFORMATION,
                    "Wynik zapisano z id = " + result.id(),
                    ButtonType.OK).showAndWait();
            resultsTable.getItems().add(result);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            showError(ie);
        } catch (IOException ioe) {
            showError(ioe);
        }
    }

    @FXML
    private void onNew() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/CreateDialog.fxml")
        );
        VBox pane = loader.load();
        CreateDialogController ctrl = loader.getController();

        Stage dialog = new Stage();
        dialog.initOwner(tableView.getScene().getWindow());
        dialog.setScene(new Scene(pane));
        dialog.setTitle("Nowa definicja");
        ctrl.setDialogStage(dialog);
        ctrl.setOnCreated(dto -> {
            try {
                client.create(dto);
                refreshList();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                showError(ex);
            } catch (IOException ex) {
                showError(ex);
            }
        });
        dialog.showAndWait();
    }


    private void refreshList() {
        try {
            var list = client.findAll();
            tableView .getItems().setAll(filter(list, SequenceDto.class));
            tableView1.getItems().setAll(filter(list, ParallelDto.class));
        } catch (IOException ex) {
            showError(ex);
        } catch (InterruptedException ex) { Thread.currentThread().interrupt();}
    }

    private void refreshResults() {
        try {
            var list = client.findAllTransformResults();
            resultsTable.getItems().setAll(list);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    @FXML
    private void onDeleteResult() {
        TransformResultDto selected = resultsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Najpierw wybierz wynik do usunięcia.", ButtonType.OK)
                    .showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Na pewno usunąć wynik \"" + selected.name() + "\"?",
                ButtonType.YES, ButtonType.NO
        );
        alert.setHeaderText(null);
        DialogPane dp = alert.getDialogPane();
        ((Button) dp.lookupButton(ButtonType.YES)).setText("Tak");
        ((Button) dp.lookupButton(ButtonType.NO )).setText("Nie");

        alert.showAndWait()
                .filter(bt -> bt == ButtonType.YES)
                .ifPresent(bt -> {
                    try {
                        client.deleteTransformResult(selected.id());
                        resultsTable.getItems().remove(selected);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        showError(ie);
                    } catch (IOException ioe) {
                        showError(ioe);
                    }
                });
    }

    @FXML
    private void onDeleteSequenceDefinition() {
        UnitermDefDto sel = tableView.getSelectionModel().getSelectedItem();
        if (sel == null) {
            new Alert(Alert.AlertType.WARNING,
                    "Najpierw wybierz definicję sekwencjowania.", ButtonType.OK
            ).showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Na pewno usunąć definicję sekwencjowania„" + sel.name() + "”?",
                ButtonType.YES, ButtonType.NO
        );
        alert.setHeaderText(null);
        DialogPane dp = alert.getDialogPane();
        ((Button) dp.lookupButton(ButtonType.YES)).setText("Tak");
        ((Button) dp.lookupButton(ButtonType.NO )).setText("Nie");

        alert.showAndWait()
                .filter(bt -> bt == ButtonType.YES)
                .ifPresent(bt -> {
                    try {
                        client.delete(sel.id());
                        tableView.getItems().remove(sel);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        showError(ie);
                    } catch (IOException ioe) {
                        showError(ioe);
                    }
                });
    }

    @FXML
    private void onDeleteParallelDefinition() {
        UnitermDefDto sel = tableView1.getSelectionModel().getSelectedItem();
        if (sel == null) {
            new Alert(Alert.AlertType.WARNING,
                    "Najpierw wybierz definicję zrównoleglenia.", ButtonType.OK
            ).showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Na pewno usunąć definicję zrównoleglenia „" + sel.name() + "”?",
                ButtonType.YES, ButtonType.NO
        );
        alert.setHeaderText(null);
        DialogPane dp = alert.getDialogPane();
        ((Button) dp.lookupButton(ButtonType.YES)).setText("Tak");
        ((Button) dp.lookupButton(ButtonType.NO )).setText("Nie");

        alert.showAndWait()
                .filter(bt -> bt == ButtonType.YES)
                .ifPresent(bt -> {
                    try {
                        client.delete(sel.id());
                        tableView1.getItems().remove(sel);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        showError(ie);
                    } catch (IOException ioe) {
                        showError(ioe);
                    }
                });
    }

    private static List<UnitermDefDto> filter(List<UnitermDefDto> src, Class<? extends TermDto> cls) {
        return src.stream().filter(d -> cls.isInstance(d.structure())).toList();
    }

    private void display(TermDto s) { displayStructure(s); displayAst(s); }

    private void displayStructure(TermDto structure) {
        String latex      = TexUtils.toTex(structure);
        String latexEscJS = latex.replace("\\", "\\\\")
                .replace("`", "\\`");

        String html = """
            <!DOCTYPE html>
            <html><head>
              <meta charset="utf-8">
              <link  rel="stylesheet"
                     href="https://cdn.jsdelivr.net/npm/katex@0.16.7/dist/katex.min.css">
              <script defer
                     src="https://cdn.jsdelivr.net/npm/katex@0.16.7/dist/katex.min.js"></script>
            </head><body>
              <div id="katex"></div>
              <script>
                document.addEventListener('DOMContentLoaded', () => {
                  katex.render(`%s`, document.getElementById('katex'),
                               {throwOnError: false});
                });
              </script>
            </body></html>
            """.formatted(latexEscJS);

        webView.getEngine().loadContent(html);
    }

    private void displayAst(TermDto root) {
        TreeItem<String> r = buildTree(root);
        r.setExpanded(true);
        astView.setRoot(r);
    }
    private TreeItem<String> buildTree(TermDto n) {
        TreeItem<String> me = new TreeItem<>(n.getClass().getSimpleName() + ": " + n);
        if (n instanceof SequenceDto seq)  seq.children() .forEach(c -> me.getChildren().add(buildTree(c)));
        if (n instanceof ParallelDto par)  par.children() .forEach(c -> me.getChildren().add(buildTree(c)));
        if (n instanceof CustomDto(TermDto root))     me.getChildren().add(buildTree(root));
        return me;
    }

    private static void showError(Exception ex) {
        new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
    }
}