package com.proj.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class UnitermGui extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Uniterm GUI");

        BorderPane root = new BorderPane();

        Label welcomeLabel = new Label("Welcome to Uniterm GUI");
        root.setCenter(welcomeLabel);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
