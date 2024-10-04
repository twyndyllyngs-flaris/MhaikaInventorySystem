package com.example.se;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class SignUp3Controller implements Initializable {
    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/se_system", "root", "");
    Statement statement = connection.createStatement();
    ResultSet resultSet;

    String currentMessage;

    @FXML
    Label lbl_message;
    @FXML
    TextField tf_firstName;
    @FXML
    TextField tf_lastName;
    @FXML
    ComboBox<String> cb_gender;
    @FXML
    DatePicker dp_birthDate;

    MainWindowController controller_mainWindow;
    SignUp2Controller controller_signup2;

    int age;

    Stage stage_this;

    public SignUp3Controller() throws SQLException {
    }

    public void setData(MainWindowController controller_mainWindow, SignUp2Controller controller_signup2, Stage stage_this){
        this.controller_mainWindow = controller_mainWindow;
        this.controller_signup2 = controller_signup2;
        this.stage_this = stage_this;
    }

    public void close(){
        Platform.exit();
    }

    public void back(ActionEvent event){
        this.controller_signup2.stage_this.show();
        this.stage_this.close();
    }

    public void hover(){
        currentMessage = lbl_message.getText();
        lbl_message.setText("Basic information about the user");
    }

    public void hoverExit(){
        lbl_message.setText(currentMessage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cb_gender.getItems().addAll("Male", "Female");
    }

    public void submit(ActionEvent event) throws IOException {
        String gender = cb_gender.getSelectionModel().getSelectedItem();
        String firstName = tf_firstName.getText();
        String lastName = tf_lastName.getText();

        LocalDate birhtdate = dp_birthDate.getValue();  

        if (birhtdate == null){
            lbl_message.setText("Birthdate cannot be empty");
            dp_birthDate.requestFocus();
            return;
        }

        LocalDate now = LocalDate.now();
        String birthdateString = birhtdate.toString();
        this.age = (int)ChronoUnit.YEARS.between(birhtdate, now);

        if (firstName.equals("")){
            lbl_message.setText("Firstname cannot be empty");
            tf_firstName.requestFocus();
        }else if (lastName.equals("")){
            lbl_message.setText("Lastname cannot be empty");
            tf_lastName.requestFocus();
        }else if (gender == null){
            lbl_message.setText("Gender cannot be empty");
            cb_gender.requestFocus();
        }else {
            if (firstName.length() < 2){
                lbl_message.setText("Please enter a valid first name");
                tf_firstName.clear();
                tf_firstName.requestFocus();
                return;
            }else if (lastName.length() < 2){
                lbl_message.setText("Please enter a valid surname");
                tf_lastName.clear();
                tf_lastName.requestFocus();
            }else{
                this.stage_this.hide();

                SignUp4Application s4 = new SignUp4Application();
                s4.start(new Stage());
                s4.setData(this.controller_mainWindow, this.controller_signup2, this);
            }
        }
    }
}
