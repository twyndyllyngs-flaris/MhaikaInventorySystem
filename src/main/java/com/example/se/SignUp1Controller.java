package com.example.se;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class SignUp1Controller {

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
    @FXML
    Button pb_close;

    Stage stage_login;
    Stage stage;
    Scene scene;

    public SignUp1Controller() throws SQLException {
    }

    public void submit(ActionEvent event){
        String username = tf_adminUsername.getText();
        String password = pf_adminPassword.getText();

        if (username.equals("")){
            lbl_message.setText("Username cannot be empty");
            tf_adminUsername.requestFocus();
        }else if (password.equals("")){
            lbl_message.setText("Password cannot be empty");
            pf_adminPassword.requestFocus();
        }else{
            try{

                resultSet = statement.executeQuery(String.format("SELECT Role FROM accounts WHERE Username = '%s' AND Password = '%s';", username, password));

                if (!resultSet.isBeforeFirst() ) {
                    lbl_message.setText("Credentials incorrect");
                    pf_adminPassword.requestFocus();
                    return;
                }

                resultSet.next();
                String result = resultSet.getString(1);

                if (result.equals("Administrator")){
                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    stage.hide();

                    SignUp2Application s2 = new SignUp2Application();
                    s2.start(new Stage());
                    //s2.setData(this.stage_login, this.stage);
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

    public void close(){
        Platform.exit();
    }

    public void hover(){
        currentMessage = lbl_message.getText();
        lbl_message.setText("An admin account is required to create an account");
    }

    public void hoverExit(){
        lbl_message.setText(currentMessage);
    }

    public void back(ActionEvent event){
        this.stage_login.show();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void setData(Stage stage_login){
        this.stage_login = stage_login;
    }

}
