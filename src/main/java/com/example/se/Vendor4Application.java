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
import java.sql.SQLException;

public class Vendor4Application extends Application {
    private double xOffset = 0;
    private double yOffset = 0;

    Vendor4Controller controller;

    Stage stage_this;

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("vendor4.fxml"));
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

        String css = this.getClass().getResource("vendor4.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.show();
    }

    public void setData(MainWindowController mw, Vendor0Controller v0, NewExpenseConfirmationController nec, VendorController v, Vendor2Controller v2, Vendor3Controller v3) throws SQLException {
       this.controller.setData(mw, v0, nec, v, v2, v3, stage_this);
    }

}
