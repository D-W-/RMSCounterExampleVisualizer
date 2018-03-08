package com.harry.front.browser;

/**
 * RMS related tools, parse formatted data into RMS input and run RMS, then visualize output.
 * Created by Han Wang at 12/22/17.
 * Copyright (C) 2015-2017  Han Wang
 */
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.TouchPoint.State;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;


public class WebViewSample {

    public static void run(){
        Stage stage = new Stage();
        stage.setTitle("Web View");
        Scene scene = new Scene(new Browser(),2000,500, Color.web("#666970"));
        stage.setScene(scene);
//        scene.getStylesheets().add("webviewsample/BrowserToolbar.css");
        stage.show();
    }
}

class Browser extends Region {

    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();
    boolean reload = false;

    public Browser() {
        //apply the styles
        getStyleClass().add("browser");
        // load the web page
//        URL url = this.getClass().getResource("/main.html");
//        webEngine.load(url.toString());
        File file = new File("res/main.html");
        webEngine.getLoadWorker().stateProperty()
            .addListener((obs, oldValue, newValue) -> {
                if (newValue == Worker.State.SUCCEEDED && !reload) {
                    webEngine.executeScript("reloadMe();");
                    reload = true;
                }
            });
//        webEngine.executeScript("reloadMe();");
        webEngine.load(file.toURI().toString());
        //add the web view to the scene
        getChildren().add(browser);

    }

    private Node createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    @Override protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(browser,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
    }

    @Override protected double computePrefWidth(double height) {
        return 2000;
    }

    @Override protected double computePrefHeight(double width) {
        return 500;
    }
}

