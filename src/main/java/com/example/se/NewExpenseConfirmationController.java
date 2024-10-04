package com.example.se;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class NewExpenseConfirmationController implements Initializable {
    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/se_system", "root", "");
    Statement statement = connection.createStatement();
    ResultSet resultSet;

    Stage stage_this;
    MainWindowController controller_mainWindow;
    Vendor0Controller controller_vendor0;

    @FXML
    ComboBox<String> cb_companyName;

    String from;

    public NewExpenseConfirmationController() throws SQLException {
    }

    public void submit() throws IOException {
        String companyName = this.cb_companyName.getSelectionModel().getSelectedItem();

        if (companyName == null){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Please choose an existing company name", "Ok", "close");
        }else{
            Vendor3Application application = new Vendor3Application();
            application.start(new Stage());
            application.setData(this.controller_mainWindow, this.controller_vendor0, this, null, null, from);
            this.stage_this.hide();
        }
    }

    public void createNewExpense() throws IOException {
        VendorApplication application = new VendorApplication();
        application.start(new Stage());
        application.setData(this.controller_mainWindow, this.controller_vendor0, this, from);
        this.cb_companyName.getSelectionModel().clearSelection();
        this.stage_this.hide();
    }

    public void close(){
        this.controller_vendor0.stage_this.close();
        this.stage_this.close();
    }

    public void back(){
        this.controller_vendor0.stage_this.show();
        this.stage_this.close();
    }

    public void setData(MainWindowController controller_mainWindow, Vendor0Controller controller_vendor0, Stage stage_this, String from){
        this.controller_mainWindow = controller_mainWindow;
        this.controller_vendor0 = controller_vendor0;
        this.stage_this = stage_this;
        this.from = from;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadComboBox_companyName();
    }

    public void loadComboBox_companyName(){
        try {
            this.resultSet = this.statement.executeQuery("SELECT CompanyName FROM `vendors`;");
            while (resultSet.next()){
                this.cb_companyName.getItems().add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
