package com.example.se;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class VendorController {
    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/se_system", "root", "");
    Statement statement = connection.createStatement();
    ResultSet resultSet;

    String currentMessage, from;

    Stage stage_this;
    MainWindowController controller_mainWindow;
    Vendor0Controller controller_vendor0;
    NewExpenseConfirmationController controller_newExpenseConfirmation;

    @FXML
    Label lbl_message;

    @FXML
    TextField tf_companyName, tf_displayName, tf_firstName, tf_lastName;

    public VendorController() throws SQLException {
    }

    public void back(){
        this.controller_newExpenseConfirmation.stage_this.show();
        this.stage_this.close();
    }

    public void hover(){
        currentMessage = lbl_message.getText();
        lbl_message.setText("Provider details can be seen using the show button in expenses module");
    }

    public void hoverExit(){
        lbl_message.setText(currentMessage);
    }

    public void close(){
        this.controller_vendor0.stage_this.close();
        this.controller_newExpenseConfirmation.stage_this.close();
        this.stage_this.close();
    }

    public void submit() throws SQLException, IOException {
        String companyName = this.tf_companyName.getText();
        String displayName = this.tf_displayName.getText();
        String firstname = this.tf_firstName.getText();
        String lastname = this.tf_lastName.getText();

        if (companyName.equals("")){
            this.lbl_message.setText("Company name cannot be empty");
            this.tf_companyName.requestFocus();
        }else if (!firstname.equals("") && lastname.equals("")){
            this.lbl_message.setText("Lastname must come with a firstname");
            this.tf_lastName.requestFocus();
        }else if (!lastname.equals("") && firstname.equals("")){
            this.lbl_message.setText("Firstname must come with a lastname");
            this.tf_firstName.requestFocus();
        }else{
            this.resultSet =  this.statement.executeQuery(String.format("SELECT COUNT(CompanyName) FROM vendors WHERE CompanyName = '%s'", companyName));
            this.resultSet.next();
            if (resultSet.getString(1).equals("1")){
                this.lbl_message.setText("Company name is already taken. Click back to choose an existing provider");
                this.tf_companyName.clear();
                this.tf_companyName.requestFocus();
            }else{
                Vendor2Application application = new Vendor2Application();
                application.start(new Stage());
                application.setData(this.controller_mainWindow, this.controller_vendor0, this.controller_newExpenseConfirmation, this, from);
                this.stage_this.hide();
            }
        }
    }

    public void setData(MainWindowController controller_mainWindow, Vendor0Controller controller_vendor0, NewExpenseConfirmationController controller_newExpenseConfirmation, Stage stage_this, String from){
        this.controller_mainWindow = controller_mainWindow;
        this.controller_vendor0 = controller_vendor0;
        this.controller_newExpenseConfirmation = controller_newExpenseConfirmation;
        this.stage_this = stage_this;
        this.from = from;
    }
}
