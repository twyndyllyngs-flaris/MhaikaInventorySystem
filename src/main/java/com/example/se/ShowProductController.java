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

public class ShowProductController implements Initializable {
    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/se_system", "root", "");
    Statement statement = connection.createStatement();
    ResultSet resultSet;

    Stage stage_this;

    String productID;

    @FXML
    Label lbl_productID, lbl_productName, lbl_variant, lbl_description;

    @FXML
    ImageView iv_productImage;

    public ShowProductController() throws SQLException {
    }

    public void close(){
        this.stage_this.close();
    }

    public void setData(Stage stage_this, String productID, String expenseTitle) throws SQLException {
        this.productID = productID;
        this.stage_this = stage_this;

        this.resultSet = this.statement.executeQuery(String.format("SELECT * FROM expenses WHERE ExpenseTitle = '%s'", expenseTitle));
        this.resultSet.next();

        InputStream image = this.resultSet.getBinaryStream("Image");

        this.resultSet =  this.statement.executeQuery(String.format("SELECT * FROM inventory WHERE ProductID = '%s'", productID));
        this.resultSet.next();

        if (image != null)
            this.iv_productImage.setImage(new Image(image));

        this.lbl_productID.setText(productID);
        this.lbl_productName.setText(this.resultSet.getString("ProductName"));
        this.lbl_variant.setText(this.resultSet.getString("Variant"));

        if (!this.resultSet.getString("Description").equals("")){
            this.lbl_description.setText(this.resultSet.getString("Description"));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
