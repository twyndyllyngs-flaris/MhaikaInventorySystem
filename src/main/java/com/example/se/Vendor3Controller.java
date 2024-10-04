package com.example.se;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Vendor3Controller implements Initializable {
    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/se_system", "root", "");
    Statement statement = connection.createStatement();
    ResultSet resultSet;

    Stage stage_this;

    MainWindowController controller_mainWindow;
    Vendor0Controller controller_vendor0;
    NewExpenseConfirmationController controller_newExpenseConfirmation;
    VendorController controller_vendor1;
    Vendor2Controller controller_vendor2;

    String currentMessage, from;

    @FXML
    Label lbl_message;
    @FXML
    TextArea ta_description;
    @FXML
    ComboBox<String> cb_type, cb_measurement;
    @FXML
    TextField tf_title, tf_quantity, tf_totalItemCost, tf_image;

    FileInputStream image;

    public Vendor3Controller() throws SQLException {
    }

    public void measurementChange(){
        String selected = this.cb_measurement.getSelectionModel().getSelectedItem();

        if (selected == null){
            return;
        }else if (selected.equals("by Item") || selected.equals("by Person") || selected.equals("by Month")){
            this.tf_quantity.setPromptText("Quantity");
        }else {
            this.tf_quantity.setPromptText("Amount");
        }
    }

    public void hover(){
        currentMessage = lbl_message.getText();
        lbl_message.setText("window3 default message");
    }

    public void hoverExit(){
        lbl_message.setText(currentMessage);
    }

    public void close(){
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

    public void submit() throws SQLException, IOException {
        String type = this.cb_type.getSelectionModel().getSelectedItem();
        String title = this.tf_title.getText();
        String quantity = this.tf_quantity.getText();
        String totalItemCost = this.tf_totalItemCost.getText();
        String description = this.ta_description.getText();
        String measurement = this.cb_measurement.getSelectionModel().getSelectedItem();

        if (this.controller_vendor2 != null || this.controller_newExpenseConfirmation != null){
            if (title.equals("") && type.equals("Non-Product")){
                this.lbl_message.setText("Title cannot be empty for non-product entries");
                this.tf_title.requestFocus();
            }else if (quantity.equals("")){
                if (measurement.equals("by Item") || measurement.equals("by Person") || measurement.equals("by Month")){
                    this.lbl_message.setText("Quantity cannot be empty");
                }else {
                    this.lbl_message.setText("Amount cannot be empty");
                }

                this.tf_quantity.requestFocus();
            } else if (totalItemCost.equals("")) {
                this.lbl_message.setText("Total-item-cost cannot be empty");
                this.tf_totalItemCost.requestFocus();
            }else if (image == null && type.equals("Product") && !measurement.equals("by Amount")){
                this.lbl_message.setText("Image is required for this product");
                this.tf_image.requestFocus();
            }else {
                try{
                    Integer.parseInt(quantity);
                }catch (Exception e){
                    this.lbl_message.setText("Please enter a valid quantity");
                    this.tf_quantity.clear();
                    this.tf_quantity.requestFocus();
                    return;
                }

                if (quantity.contains(".")){
                    if (measurement.equals("by Item") || measurement.equals("by Person") || measurement.equals("by Month")){
                        this.lbl_message.setText("Quantity must be a whole integer");
                    }else {
                        this.lbl_message.setText("Amount must be a whole integer");
                    }

                    this.tf_quantity.clear();
                    this.tf_quantity.requestFocus();
                    return;
                } else if (Integer.parseInt(quantity) < 1) {
                    if (measurement.equals("by Item") || measurement.equals("by Person") || measurement.equals("by Month")){
                        this.lbl_message.setText("Quantity cannot be lower than 1");
                    }else {
                        this.lbl_message.setText("Amount cannot be lower than 1");
                    }

                    this.tf_quantity.clear();
                    this.tf_quantity.requestFocus();
                    return;
                }

                try{
                    Double.parseDouble(totalItemCost);
                }catch (Exception e){
                    this.lbl_message.setText("Please enter a valid total item cost");
                    this.tf_totalItemCost.clear();
                    this.tf_totalItemCost.requestFocus();
                    return;
                }

                if (Double.parseDouble(totalItemCost) < 0){
                    this.lbl_message.setText("Total item cost cannot be lower than 0");

                    this.tf_totalItemCost.clear();
                    this.tf_totalItemCost.requestFocus();
                }else {
                    if (type.equals("Product")){
                        Vendor4Application application = new Vendor4Application();
                        application.start(new Stage());
                        application.setData(this.controller_mainWindow, this.controller_vendor0, this.controller_newExpenseConfirmation, this.controller_vendor1, this.controller_vendor2, this);
                        this.stage_this.hide();
                    }else {
                        Vendor5Application application = new Vendor5Application();
                        application.start(new Stage());
                        application.setData(this.controller_mainWindow, this.controller_vendor0, this.controller_newExpenseConfirmation, this.controller_vendor1, this.controller_vendor2, this, null);
                        this.stage_this.hide();
                    }
                }
            }
        }else{
            if (quantity.equals("")){
                if (measurement.equals("by Item") || measurement.equals("by Person") || measurement.equals("by Month")){
                    this.lbl_message.setText("Quantity cannot be empty");
                }else {
                    this.lbl_message.setText("Amount cannot be empty");
                }

                this.tf_quantity.requestFocus();
            } else if (totalItemCost.equals("")) {
                this.lbl_message.setText("Total-item-cost cannot be empty");
                this.tf_totalItemCost.requestFocus();
            }else {
                try{
                    Integer.parseInt(quantity);
                }catch (Exception e){
                    this.lbl_message.setText("Please enter a valid quantity");
                    this.tf_quantity.clear();
                    this.tf_quantity.requestFocus();
                    return;
                }

                if (quantity.contains(".")){
                    if (measurement.equals("by Item") || measurement.equals("by Person") || measurement.equals("by Month")){
                        this.lbl_message.setText("Quantity must be a whole integer");
                    }else {
                        this.lbl_message.setText("Amount must be a whole integer");
                    }

                    this.tf_quantity.clear();
                    this.tf_quantity.requestFocus();
                    return;
                } else if (Integer.parseInt(quantity) < 1) {
                    if (measurement.equals("by Item") || measurement.equals("by Person") || measurement.equals("by Month")){
                        this.lbl_message.setText("Quantity cannot be lower than 1");
                    }else {
                        this.lbl_message.setText("Amount cannot be lower than 1");
                    }

                    this.tf_quantity.clear();
                    this.tf_quantity.requestFocus();
                    return;
                }

                try{
                    Double.parseDouble(totalItemCost);
                }catch (Exception e){
                    this.lbl_message.setText("Please enter a valid total item cost");
                    this.tf_totalItemCost.clear();
                    this.tf_totalItemCost.requestFocus();
                    return;
                }

                if (Double.parseDouble(totalItemCost) < 0){
                    this.lbl_message.setText("Total item cost cannot be lower than 0");

                    this.tf_totalItemCost.clear();
                    this.tf_totalItemCost.requestFocus();
                }else {
                    String existingExpenseTitle = this.controller_vendor0.cb_expenseTitle.getSelectionModel().getSelectedItem() == null ? "" :
                            this.controller_vendor0.cb_expenseTitle.getSelectionModel().getSelectedItem();

                    this.resultSet = this.statement.executeQuery(String.format("SELECT * FROM inventory WHERE ExpenseTitle = '%s'", existingExpenseTitle));

                    if (this.resultSet.next() && this.resultSet.getDate("ExpirationDate") != null){
                        Vendor4Application application = new Vendor4Application();
                        application.start(new Stage());
                        application.setData(this.controller_mainWindow, this.controller_vendor0, this.controller_newExpenseConfirmation, this.controller_vendor1, this.controller_vendor2, this);
                        this.stage_this.hide();
                    }else {
                        Vendor5Application application = new Vendor5Application();
                        application.start(new Stage());
                        application.setData(this.controller_mainWindow, this.controller_vendor0, this.controller_newExpenseConfirmation, this.controller_vendor1, this.controller_vendor2, this, null);
                        this.stage_this.hide();
                    }
                }
            }
        }
    }

    public void setData(MainWindowController controller_mainWindow, Vendor0Controller controller_vendor0, NewExpenseConfirmationController controller_newExpenseConfirmation, VendorController controller_vendor1, Vendor2Controller controller_vendor2, Stage stage_this, String from){
        this.controller_mainWindow = controller_mainWindow;
        this.controller_vendor0 = controller_vendor0;
        this.controller_newExpenseConfirmation = controller_newExpenseConfirmation;
        this.controller_vendor1 = controller_vendor1;
        this.controller_vendor2 = controller_vendor2;
        this.stage_this = stage_this;
        this.from = from;

        if (from.equals("inventory")){
            this.cb_type.getSelectionModel().select("Product");
            this.cb_type.setDisable(true);
        }

        if (controller_vendor2 == null && this.controller_newExpenseConfirmation == null) {
            try {
                this.resultSet = this.statement.executeQuery(String.format("SELECT * FROM expenses WHERE ExpenseTitle = '%s'", this.controller_vendor0.cb_expenseTitle.getSelectionModel().getSelectedItem()));
                this.resultSet.next();


                this.cb_type.getSelectionModel().select(this.resultSet.getString("ExpenseType"));
                this.cb_type.setDisable(true);

                this.tf_image.setText(this.resultSet.getString("ImageName"));
                this.tf_image.setDisable(true);

                this.tf_title.setText(this.resultSet.getString("ExpenseTitle"));
                this.tf_title.setDisable(true);

                this.cb_measurement.getSelectionModel().select(this.resultSet.getString("Measurement"));
                this.cb_measurement.setDisable(true);

                String measurement = this.resultSet.getString("Measurement");

                if (!measurement.equals("by Item") && !measurement.equals("by Person")){
                    this.tf_quantity.setPromptText("Amount");
                }

                this.ta_description.setText(this.resultSet.getString("Description"));
                this.ta_description.setDisable(true);

            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cb_type.getItems().addAll("Product", "Non-Product");
        cb_type.getSelectionModel().select("Non-Product");
        cb_measurement.getItems().addAll("by Item", "by Person", "by Month", "kW/h", "m^3/s", "by Amount");
        cb_measurement.getSelectionModel().select("by Item");
    }

    public void typeChange(){
        if (this.cb_type.getSelectionModel().getSelectedItem().equals("Product")){
            this.cb_measurement.getItems().clear();
            this.cb_measurement.getItems().addAll("by Item", "by Amount");
        }else{
            this.cb_measurement.getItems().clear();
            cb_measurement.getItems().addAll("by Item", "by Person", "by Month", "kW/h", "m^3/s", "by Amount");
        }

        this.cb_measurement.getSelectionModel().select("by Item");
    }

    public void back(){
        if (this.controller_vendor2 != null){
            this.controller_vendor2.stage_this.show();
        }else if (this.controller_newExpenseConfirmation != null){
            this.controller_newExpenseConfirmation.stage_this.show();
        }else {
            this.controller_vendor0.stage_this.show();
        }

        this.stage_this.close();
    }

    public void getFile() throws FileNotFoundException {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG Files", "*.jpg"), new FileChooser.ExtensionFilter("PNG Files", "*.png"));
        File selectedFile = fc.showOpenDialog(null);

        if (selectedFile != null){
            this.tf_image.setText(selectedFile.getName());
            this.image = new FileInputStream(selectedFile.getPath());
        }
    }
}
