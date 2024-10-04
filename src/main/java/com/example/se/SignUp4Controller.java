package com.example.se;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class SignUp4Controller {
    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/se_system", "root", "");
    Statement statement = connection.createStatement();
    ResultSet resultSet;

    String currentMessage;

    @FXML
    TextField tf_mobile;
    @FXML
    TextField tf_telephone;
    @FXML
    TextField tf_email;
    @FXML
    TextField tf_facebook;
    @FXML
    Label lbl_message;

    MainWindowController controller_mainWindow;
    SignUp2Controller controller_signup2;
    SignUp3Controller controller_signup3;
    Stage stage_this;


    public SignUp4Controller() throws SQLException {
    }

    public void setData(MainWindowController m2, SignUp2Controller s2, SignUp3Controller s3, Stage stage_this){
        this.controller_mainWindow = m2;
        this.controller_signup3 = s3;
        this.controller_signup2 = s2;
        this.stage_this = stage_this;
    }

    public void close(){
        Platform.exit();
    }

    public void back(ActionEvent event){
        this.controller_signup3.stage_this.show();
        this.stage_this.close();
    }

    public void hover(){
        currentMessage = lbl_message.getText();
        lbl_message.setText("Please enter a valid and active contact details");
    }

    public void hoverExit(){
        lbl_message.setText(currentMessage);
    }

    public void submit(ActionEvent event) throws IOException {
        String mobile = tf_mobile.getText();
        String telephone = tf_telephone.getText();
        String email = tf_email.getText();
        String facebook = tf_facebook.getText();

        if (mobile.equals("") && telephone.equals("")){
            lbl_message.setText("Mobile and telephone cannot be empty. Please fill-up at least one of the two");
            tf_mobile.requestFocus();
        }else if (email.equals("")){
            lbl_message.setText("Email cannot be empty");
            tf_email.requestFocus();
        }else if (facebook.equals("")){
            lbl_message.setText("Facebook account cannot be empty");
            tf_facebook.requestFocus();
        }else{
            if (!mobile.equals("")){
                try{
                    long mobileLong = Long.parseLong(mobile);
                }catch (Exception e){
                    lbl_message.setText("Please enter a valid phone number");
                    tf_mobile.clear();
                    tf_mobile.requestFocus();
                    return;
                }

                if (mobile.length() != 11){
                    lbl_message.setText("Mobile number should have 11 digits");
                    tf_mobile.clear();
                    tf_mobile.requestFocus();
                    return;
                }
            }

            if (!telephone.equals("")){
                if (telephone.length() < 5){
                    lbl_message.setText("Telephone must have at least 5 digits");
                    tf_telephone.clear();
                    tf_telephone.requestFocus();
                    return;
                }
            }

            if (!email.contains("@") || email.length() < 10){
                lbl_message.setText("Please enter a valid email address");
                tf_email.clear();
                tf_email.requestFocus();
            }else{
                this.stage_this.hide();

                SignUp5Application s5 = new SignUp5Application();
                s5.start(new Stage());
                s5.setData(this.controller_mainWindow, this.controller_signup2, this.controller_signup3, this);
            }

        }
    }
}
