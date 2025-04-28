package com.proj.gui.controller;

import com.proj.masi.dto.UnitermDefDto;
import com.proj.masi.dto.structure.ParallelDto;
import com.proj.masi.dto.structure.SequenceDto;
import com.proj.masi.dto.structure.TermDto;
import com.proj.masi.dto.structure.UnitermDto;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CreateDialogController {
    @FXML
    Button btnCreate;
    @FXML
    Button btnCancel;
    @FXML
    TextField tfName;
    @FXML
    TextField tfDesc;
    @FXML
    RadioButton rbSeq;
    @FXML
    RadioButton rbPar;
    @FXML
    ComboBox<String> cbSep;
    @FXML
    VBox childrenBox;


    private final List<TextField> childFields = new ArrayList<>();
    private Stage dialogStage;
    private Consumer<UnitermDefDto> onCreated;

    @FXML
    public void initialize() {
        cbSep.getSelectionModel().selectFirst();
        onAddChild();
        onAddChild();
        btnCreate.setDefaultButton(true);
        btnCancel.setCancelButton(true);
    }

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }
    public void setOnCreated(Consumer<UnitermDefDto> c) {
        this.onCreated = c;
    }

    @FXML
    private void onAddChild() {
        TextField tf = new TextField();
        tf.setPromptText("Uniterm");
        childFields.add(tf);
        childrenBox.getChildren().add(tf);
        Platform.runLater(() -> {
            if (dialogStage != null) dialogStage.sizeToScene();
        });
    }

    @FXML
    private void onRemoveChild() {
        if (!childFields.isEmpty()) {
            var tf = childFields.removeLast();
            childrenBox.getChildren().remove(tf);
        }
        Platform.runLater(() -> {
            if (dialogStage != null) dialogStage.sizeToScene();
        });
    }

    @FXML
    private void onCancel() {
        dialogStage.close();
    }

    @FXML
    private void onCreate() {
        String name = tfName.getText().trim();
        if (name.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(dialogStage);
            alert.setTitle("Uwaga");
            alert.setHeaderText(null);
            alert.setContentText("Nazwa nie może być pusta.");
            alert.showAndWait();
            return;
        }        List<TermDto> kids = childFields.stream()
                .map(TextField::getText)
                .map(String::trim)
                .filter(s->!s.isEmpty())
                .map(text -> (TermDto)new UnitermDto(text))
                .toList();
        if (kids.size()<2) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(dialogStage);
            alert.setTitle("Uwaga");
            alert.setHeaderText(null);
            alert.setContentText("Musisz dodać co najmniej dwa unitermy.");
            alert.showAndWait();
            return;
        }

        TermDto struct = rbSeq.isSelected()
                ? new SequenceDto(kids, cbSep.getValue())
                : new ParallelDto(kids, cbSep.getValue());

        UnitermDefDto dto = new UnitermDefDto(
                null,
                name,
                tfDesc.getText().trim(),
                struct
        );

        onCreated.accept(dto);
        dialogStage.close();
    }
}

