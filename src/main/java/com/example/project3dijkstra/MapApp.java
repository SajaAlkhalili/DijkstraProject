
package com.example.project3dijkstra;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;


public class MapApp extends Application {

public static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {

        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("map-view.fxml"));
        primaryStage.setTitle("خريطة غزة");
        Scene scene = new Scene(root);
stage.setFullScreen(true);
        WebView webView;
        scene.getStylesheets().add(MapApp.class.getResource("style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    public static void main(String[] args) {

        launch(args);

    }
}
