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

public class SignUp5Application extends Application {
    private double xOffset = 0;
    private double yOffset = 0;

    SignUp5Controller controller;
    Stage stage_this;

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("signup5.fxml"));
        Parent root = fxmlLoader.load();
        controller = fxmlLoader.getController();
        this.stage_this = stage;

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

        String css = this.getClass().getResource("signup3.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.show();
    }

    public void setData(MainWindowController mw, SignUp2Controller s2, SignUp3Controller s3, SignUp4Controller s4){
        this.controller.setData(mw, s2 ,s3 ,s4, this.stage_this);
    }
}
