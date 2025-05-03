package com.proj.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UnitermGui extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        var loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
        var root = loader.load();
        stage.setScene(new Scene((Parent) root, 900, 600));
        stage.setTitle("Uniterm Manager");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
