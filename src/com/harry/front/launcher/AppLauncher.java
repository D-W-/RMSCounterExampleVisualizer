package com.harry.front.launcher;

/**
 * RMS related tools, parse formatted data into RMS input and run RMS, then visualize output.
 * Created by Han Wang at 12/21/17.
 * Copyright (C) 2015-2017  Han Wang
 */
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppLauncher extends Application {

    public static void main(String[] args) {
        Application.launch(AppLauncher.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            final Parent root = FXMLLoader.load(getClass().getClassLoader()
                    .getResource("com/harry/front/fxml/App.fxml"));
            final Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Table Example");

            stage.setMinHeight(250);
            stage.setMinWidth(500);

            stage.setMaxHeight(500);
            stage.setMaxWidth(1000);

            stage.show();
        } catch (Exception e) {
            System.out.print(e);
        }
    }
}
