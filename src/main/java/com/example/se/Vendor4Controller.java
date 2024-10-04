package com.example.se;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Vendor4Controller implements Initializable {

    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/se_system", "root", "");
    Statement statement = connection.createStatement();
    ResultSet resultSet;

    @FXML
    Label lbl_message;

    Stage stage_this;

    String currentMessage;

    MainWindowController controller_mainWindow;
    Vendor0Controller controller_vendor0;
    NewExpenseConfirmationController controller_newExpenseConfirmation;
    VendorController controller_vendor1;
    Vendor2Controller controller_vendor2;
    Vendor3Controller controller_vendor3;

    @FXML
    TextField tf_productType, tf_productName, tf_variant, tf_price;
    @FXML
    DatePicker dp_expirationDate;

    public Vendor4Controller() throws SQLException {
    }

    public void close(){
        this.controller_vendor3.stage_this.close();

        if (controller_vendor2 != null){
            this.controller_vendor2.stage_this.close();
            this.controller_vendor1.stage_this.close();
        }

        if (this.controller_newExpenseConfirmation != null){
            this.controller_newExpenseConfirmation.stage_this.close();
        }

        this.controller_vendor0.stage_this.close();
        this.stage_this.close();
    }

    public void hover(){
        currentMessage = lbl_message.getText();
        lbl_message.setText("Vendor4 Message");
    }

    public void hoverExit(){
        lbl_message.setText(currentMessage);
    }

    public void submit() throws SQLException, IOException {
        String productType = this.tf_productType.getText();
        String productName = this.tf_productName.getText();
        String variant = this.tf_variant.getText();
        String price = this.tf_price.getText();
        Date expirationDate = this.dp_expirationDate.getValue() == null ? null : Date.valueOf(this.dp_expirationDate.getValue());

        String existingExpenseTitle = this.controller_vendor0.cb_expenseTitle.getSelectionModel().getSelectedItem() == null ? "" :
                this.controller_vendor0.cb_expenseTitle.getSelectionModel().getSelectedItem();

        this.resultSet = this.statement.executeQuery(String.format("SELECT * FROM inventory WHERE ExpenseTitle = '%s'", existingExpenseTitle));

        if (this.resultSet.next() && this.resultSet.getDate("ExpirationDate") != null){
            if (expirationDate == null){
                this.lbl_message.setText("Expiration date cannot be empty");
                this.dp_expirationDate.requestFocus();
            }else {
                Vendor5Application application = new Vendor5Application();
                application.start(new Stage());
                application.setData(this.controller_mainWindow, this.controller_vendor0, this.controller_newExpenseConfirmation, this.controller_vendor1, this.controller_vendor2, this.controller_vendor3, this);
                this.stage_this.hide();
            }
        }else {
            if (productType.equals("")){
                this.lbl_message.setText("Product type cannot be empty");
                this.tf_productType.requestFocus();
            }else if (productName.equals("")){
                this.lbl_message.setText("Product name cannot be empty");
                this.tf_productName.requestFocus();
            }else if (variant.equals("")){
                this.lbl_message.setText("Variant cannot be empty");
                this.tf_variant.requestFocus();
            }else if (price.equals("") && !this.controller_vendor3.cb_measurement.getSelectionModel().getSelectedItem().equals("by Amount")){
                this.lbl_message.setText("Price cannot be empty");
                this.tf_price.requestFocus();
            }else {
                ResultSet resultSet = this.statement.executeQuery(String.format("SELECT COUNT(ProductType) FROM Inventory WHERE ProductType = '%s' AND ProductName = '%s' AND Variant = '%s'", productType, productName, variant));
                resultSet.next();

                if (resultSet.getString(1).equals("1")){
                    this.lbl_message.setText("Product already exist");
                    this.tf_variant.requestFocus();
                    return;
                }

                if (this.controller_vendor3.cb_measurement.getSelectionModel().getSelectedItem().equals("by Item")){
                    try{
                        Double.parseDouble(price);
                    }catch (Exception e){
                        this.lbl_message.setText("Price should be numerical");
                        this.tf_price.clear();
                        this.tf_price.requestFocus();
                        return;
                    }

                    if (Double.parseDouble(price) <= 0){
                        this.lbl_message.setText("Cannot set price lower than 0");
                        this.tf_price.clear();
                        this.tf_price.requestFocus();
                        return;
                    }
                }

                Vendor5Application application = new Vendor5Application();
                application.start(new Stage());
                application.setData(this.controller_mainWindow, this.controller_vendor0, this.controller_newExpenseConfirmation, this.controller_vendor1, this.controller_vendor2, this.controller_vendor3, this);
                this.stage_this.hide();
            }
        }
    }

    public void back(){
        this.controller_vendor3.stage_this.show();
        this.stage_this.close();
    }

    public void setData(MainWindowController mw, Vendor0Controller v0, NewExpenseConfirmationController nec, VendorController v, Vendor2Controller v2, Vendor3Controller v3, Stage stage_this) throws SQLException {
        this.controller_mainWindow = mw;
        this.controller_vendor0 = v0;
        this.controller_newExpenseConfirmation = nec;
        this.controller_vendor1 = v;
        this.controller_vendor2 = v2;
        this.controller_vendor3 = v3;
        this.stage_this = stage_this;

        if (v3.cb_measurement.getSelectionModel().getSelectedItem().equals("by Amount")){
            this.tf_price.setDisable(true);
        }

        String existingExpenseTitle = this.controller_vendor0.cb_expenseTitle.getSelectionModel().getSelectedItem();

        if (existingExpenseTitle != null){
            this.resultSet = this.statement.executeQuery(String.format("SELECT * FROM inventory WHERE ExpenseTitle = '%s'", existingExpenseTitle));
            if (this.resultSet.next() && this.resultSet.getDate("ExpirationDate") != null){
                this.tf_productType.setText(this.resultSet.getString("ProductType"));
                this.tf_productType.setDisable(true);
                this.tf_productName.setText(this.resultSet.getString("ProductName"));
                this.tf_productName.setDisable(true);
                this.tf_variant.setText(this.resultSet.getString("Variant"));
                this.tf_variant.setDisable(true);
                this.tf_price.setText(this.resultSet.getString("Price"));
                this.tf_price.setDisable(true);
                this.dp_expirationDate.requestFocus();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.resultSet = this.statement.executeQuery("SELECT ProductType FROM inventory");

            while (resultSet.next()){
                TextFields.bindAutoCompletion(this.tf_productType, resultSet.getString(1)).setDelay(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
