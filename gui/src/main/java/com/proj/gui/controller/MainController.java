package com.proj.gui.controller;

import com.proj.gui.service.UnitermHttpClient;
import com.proj.gui.util.TexUtils;
import com.proj.masi.dto.*;
import com.proj.masi.dto.structure.*;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TreeItem;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainController {


    @FXML private TableView<UnitermDefDto> tableView;
    @FXML private TableColumn<UnitermDefDto, String> colName;
    @FXML private TableColumn<UnitermDefDto, String> colDesc;

    @FXML private TableView<UnitermDefDto> tableView1;
    @FXML private TableColumn<UnitermDefDto, String> colName1;
    @FXML private TableColumn<UnitermDefDto, String> colDesc1;

    @FXML private TableView<UnitermDefDto> tableView2;
    @FXML private TableColumn<UnitermDefDto, String> colName2;
    @FXML private TableColumn<UnitermDefDto, String> colDesc2;

    @FXML private WebView  webView;
    @FXML private TreeView<String> astView;

    private final UnitermHttpClient client = new UnitermHttpClient();
    private TermDto currentStructure;

    @FXML
    private void initialize() {
        initTable(tableView,  colName,  colDesc,  this::display);
        initTable(tableView1, colName1, colDesc1, this::display);
        initTable(tableView2, colName2, colDesc2, this::display);
        refreshList();
    }

    private static void initTable(TableView<UnitermDefDto> tv,
                                  TableColumn<UnitermDefDto,String> c1,
                                  TableColumn<UnitermDefDto,String> c2,
                                  java.util.function.Consumer<TermDto> onSelect) {

        c1.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().name()));
        c2.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().description()));
        tv.getSelectionModel().selectedItemProperty()
                .addListener((obs, o, s) -> { if (s != null) onSelect.accept(s.structure()); });
    }

    @FXML private void onRefresh()     { refreshList(); }

    @FXML
    private void onTransform() {
        UnitermDefDto sel = Stream.of(tableView, tableView1, tableView2)
                .map(t -> t.getSelectionModel().getSelectedItem())
                .filter(java.util.Objects::nonNull)
                .findFirst().orElse(null);
        if (sel == null) return;

        // TODO: wybór path i replacementId z GUI
        var req = new TransformRequest(sel.id(), sel.id(), List.of(1));
        try {
            currentStructure = client.transform(req);
            display(currentStructure);
        } catch (IOException | InterruptedException ex) { showError(ex); }
    }

    @FXML
    private void onSaveCustom() {
        if (currentStructure == null) return;
        var req = new SaveCustomRequest(
                "custom_" + UUID.randomUUID().toString( ).substring(0, 5),
                "Wynik transformacji",
                client.emptyProps(),
                currentStructure
        );
        try {
            client.saveCustom(req);
            refreshList();
        } catch (IOException | InterruptedException ex) { showError(ex); }
    }

    private void refreshList() {
        try {
            var list = client.findAll();
            tableView .getItems().setAll(filter(list, SequenceDto.class));
            tableView1.getItems().setAll(filter(list, ParallelDto.class));
            tableView2.getItems().setAll(filter(list, CustomDto  .class));
        } catch (IOException | InterruptedException ex) { showError(ex); }
    }

    private static List<UnitermDefDto> filter(List<UnitermDefDto> src, Class<? extends TermDto> cls) {
        return src.stream().filter(d -> cls.isInstance(d.structure())).collect(Collectors.toList());
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
