package com.proj.gui.controller;

import com.proj.masi.dto.UnitermDefDto;
import com.proj.gui.service.UnitermHttpClient;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.List;

public class MainController {
    @FXML private TableView<UnitermDefDto> tableView;
    @FXML private TableColumn<UnitermDefDto, String> colName;
    @FXML private TableColumn<UnitermDefDto, String> colDescription;
    @FXML private TreeView<String> treeView;
    @FXML
    private Button btnCreate;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;

    private final UnitermHttpClient client = new UnitermHttpClient();

    @FXML
    public void initialize() {
        colName.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().name()));
        colDescription.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().description()));
        loadData();

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) showDetails(newV);
        });

        btnDelete.setOnAction(e -> {
            var sel = tableView.getSelectionModel().getSelectedItem();
            if (sel != null) {
                try {
                    client.delete(sel.id());
                    loadData();
                } catch (IOException ex) {
                    showError(ex);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    showError(ex);
                }
            }
        });
    }

    private void loadData() {
        try {
            List<UnitermDefDto> list = client.findAll();
            tableView.getItems().setAll(list);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void showDetails(UnitermDefDto dto) {
        var root = new TreeItem<>("Uniterm: " + dto.name());
        dto.sequence().forEach(step -> root.getChildren().add(new TreeItem<>("SEQ: " + step)));
        dto.expansion().forEach(exp -> root.getChildren().add(new TreeItem<>("EXP: " + exp)));
        treeView.setRoot(root);
        root.setExpanded(true);
    }

    private void showError(Exception ex) {
        new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
    }
}

