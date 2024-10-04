package com.example.se;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class SignUp2Controller implements Initializable {
    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/se_system", "root", "");
    Statement statement = connection.createStatement();
    ResultSet resultSet;

    @FXML
    ComboBox<String> cb_role;
    @FXML
    TextField tf_username;
    @FXML
    PasswordField pf_password;
    @FXML
    PasswordField pf_confirmPassword;
    @FXML
    Label lbl_message;
    String currentMessage;
    @FXML
    AnchorPane ap_wrapper;

    Stage stage_this;

    MainWindowController controller_mainWindow;

    public SignUp2Controller() throws SQLException {
    }


    public void setData(MainWindowController controller_mainWindow, Stage stage_this){
        this.controller_mainWindow = controller_mainWindow;
        this.stage_this = stage_this;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cb_role.getItems().addAll("Administrator", "Employee");
    }

    public void submit(ActionEvent event) throws SQLException, IOException {
        String role = cb_role.getSelectionModel().getSelectedItem();
        String username = tf_username.getText();
        String password = pf_password.getText();
        String confirmPassword = pf_confirmPassword.getText();

        if (username.equals("")){
            lbl_message.setText("Username cannot bet empty");
            tf_username.requestFocus();
        }else if (role == null){
            lbl_message.setText("Please pick a role for your account");
            cb_role.requestFocus();
        }else if (password.equals("")){
            lbl_message.setText("Password cannot be empty");
            pf_password.requestFocus();
        }else if (confirmPassword.equals("")){
            lbl_message.setText("Please confirm your password");
            pf_confirmPassword.requestFocus();
        }else{
            resultSet = statement.executeQuery(String.format("SELECT COUNT(*) FROM accounts WHERE Username = '%s'", username));
            resultSet.next();
            String result = resultSet.getString(1);

            if (username.length() < 5 || username.length() > 16){
                lbl_message.setText("Only between and including 5-16 characters are allowed");
                tf_username.clear();
                tf_username.requestFocus();
            }else if (result.equals("1")){
                lbl_message.setText("Username already taken");
                tf_username.clear();
                tf_username.requestFocus();
            }else if (password.length() < 8){
                lbl_message.setText("Password must have at least 8 characters");
                pf_password.clear();
                pf_password.requestFocus();
            }else if (!password.equals(confirmPassword)){
                lbl_message.setText("Password doesn't match.");
                pf_password.clear();
                pf_confirmPassword.clear();
                pf_password.requestFocus();
            }else {
               this.stage_this.hide();

               SignUp3Application s3 = new SignUp3Application();
               s3.start(new Stage());
               s3.setData(this.controller_mainWindow, this);
            }
        }
    }

    public void close(){
        this.stage_this.close();
    }

    public void hover(){
        currentMessage = lbl_message.getText();
        lbl_message.setText("This will serve as your log-in credentials");
    }

    public void hoverExit(){
        lbl_message.setText(currentMessage);
    }
}
