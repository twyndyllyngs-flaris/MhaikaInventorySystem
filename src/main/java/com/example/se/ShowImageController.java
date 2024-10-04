package com.example.se;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ShowImageController implements Initializable {
    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/se_system", "root", "");
    Statement statement = connection.createStatement();
    ResultSet resultSet;

    Stage stage_this;

    String expenseNumber;

    @FXML
    Label lbl_expenseNumber, lbl_expenseTitle, lbl_description;
    @FXML
    ImageView iv_expenseImage;

    public ShowImageController() throws SQLException {
    }

    public void close(){
        this.stage_this.close();
    }

    public void setData(Stage stage_this, String expenseNumber) throws SQLException {
        this.expenseNumber = expenseNumber;
        this.stage_this = stage_this;

        this.resultSet =  this.statement.executeQuery(String.format("SELECT * FROM expenses WHERE ExpenseNumber = '%s'", expenseNumber));
        this.resultSet.next();

        InputStream image = this.resultSet.getBinaryStream("Image");

        if (image != null){
            this.iv_expenseImage.setImage(new Image(image));
        }

        this.lbl_expenseNumber.setText(expenseNumber);
        this.lbl_expenseTitle.setText(this.resultSet.getString("ExpenseTitle"));

        if (!this.resultSet.getString("Description").equals("")){
            this.lbl_description.setText(this.resultSet.getString("Description"));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
