package com.example.se;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class SignUp2_5Controller {
    Stage stage_login;
    Stage stage_signUp2;
    Stage this_stage;

    String username;
    String password;
    String role;

    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/se_system", "root", "");
    Statement statement = connection.createStatement();
    ResultSet resultSet;

    @FXML
    TextField tf_adminUsername;
    @FXML
    TextField pf_adminPassword;
    @FXML
    Label lbl_message;
    String currentMessage;

    public SignUp2_5Controller() throws SQLException {
    }

    public void setData(Stage stage_login, Stage stage_signUp2, String username, String password, String role){
        this.stage_login = stage_login;
        this.stage_signUp2 = stage_signUp2;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public void close(){
        Platform.exit();
    }

    public void back(ActionEvent event){
        this.stage_signUp2.show();
        this_stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this_stage.close();
    }

    public void submit(ActionEvent event){
        String adminUsername = tf_adminUsername.getText();
        String adminPassword = pf_adminPassword.getText();

        if (adminUsername.equals("")){
            lbl_message.setText("Username cannot be empty");
            tf_adminUsername.requestFocus();
        }else if (adminPassword.equals("")){
            lbl_message.setText("Password cannot be empty");
            pf_adminPassword.requestFocus();
        }else{
            try{

                resultSet = statement.executeQuery(String.format("SELECT Role FROM accounts WHERE Username = '%s' AND Password = '%s';", adminUsername, adminPassword));

                if (!resultSet.isBeforeFirst() ) {
                    lbl_message.setText("Credentials incorrect");
                    pf_adminPassword.requestFocus();
                    return;
                }

                resultSet.next();
                String result = resultSet.getString(1);

                if (result.equals("Administrator")){
                    this.this_stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    this.this_stage.hide();

                    SignUp3Application s3 = new SignUp3Application();
                    s3.start(new Stage());
                    //s3.setData(this.stage_login, this.stage_signUp2, this.username, this.password, this.role);
                }else{
                    lbl_message.setText("Please enter an admin account");
                    pf_adminPassword.clear();
                    tf_adminUsername.clear();
                    tf_adminUsername.requestFocus();
                }
            }catch (SQLException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void hover(){
        currentMessage = lbl_message.getText();
        lbl_message.setText("Another Administrator authentication is required when creating an Administrator account");
    }

    public void hoverExit(){
        lbl_message.setText(currentMessage);
    }
}
