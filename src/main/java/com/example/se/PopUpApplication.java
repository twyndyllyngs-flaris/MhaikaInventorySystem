package com.example.se;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class PopUpApplication extends Application {
    private double xOffset = 0;
    private double yOffset = 0;

    PopUpController controller;

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("popup.fxml"));
        Parent root = fxmlLoader.load();
        controller = fxmlLoader.getController();

        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });

        Scene scene = new Scene(root);

        scene.setFill(Color.TRANSPARENT);

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setResizable(false);

        stage.initStyle(StageStyle.TRANSPARENT);

        String css = this.getClass().getResource("index.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.show();
    }

    public void setData(Stage stage_from, String title, String message, String buttonText, String functionName){
        this.controller.setData(stage_from, title, message, buttonText, functionName);
    }
}
