package com.example.se;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Vendor0Controller implements Initializable {
    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/se_system", "root", "");
    Statement statement = connection.createStatement();
    ResultSet resultSet;

    Stage stage_this;

    MainWindowController controller_mainWindow;

    @FXML
    ComboBox<String> cb_expenseTitle;

    String from;

    public Vendor0Controller() throws SQLException {
    }

    public void close(){
        this.stage_this.close();
    }

    public void setData(MainWindowController controller_mainWindow, Stage stage_this, String from){
        this.controller_mainWindow = controller_mainWindow;
        this.stage_this = stage_this;
        this.from = from;

        try {
            if (from.equals("inventory"))
                this.resultSet = this.statement.executeQuery("SELECT DISTINCT ExpenseTitle FROM `expenses` WHERE ExpenseType = 'Product';");
            else
                this.resultSet = this.statement.executeQuery("SELECT DISTINCT ExpenseTitle FROM `expenses`;");

            while (resultSet.next()){
                this.cb_expenseTitle.getItems().add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void submit() throws IOException {
        String expenseTitle = this.cb_expenseTitle.getSelectionModel().getSelectedItem();

        if (expenseTitle == null){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(stage_this, "Error", "Please choose an existing expense title", "Ok", "close");
        }else{
            Vendor3Application application = new Vendor3Application();
            application.start(new Stage());
            application.setData(this.controller_mainWindow, this, null, null, null, from);
            this.stage_this.hide();
        }
    }

    public void createNewExpense() throws IOException {
        NewExpenseConfirmationApplication newExpenseConfirmationApplication = new NewExpenseConfirmationApplication();
        newExpenseConfirmationApplication.start(new Stage());
        newExpenseConfirmationApplication.setData(this.controller_mainWindow, this, from);
        this.cb_expenseTitle.getSelectionModel().clearSelection();
        this.stage_this.hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadComboBox_expenseTitle();
    }


    public void loadComboBox_expenseTitle(){

    }
}
