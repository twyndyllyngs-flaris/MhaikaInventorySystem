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

public class SignUp5Controller {
    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/se_system", "root", "");
    Statement statement = connection.createStatement();
    int resultSet;

    String currentMessage;

    @FXML
    Label lbl_message;
    @FXML
    TextField tf_province;
    @FXML
    TextField tf_city;
    @FXML
    TextField tf_barangay;
    @FXML
    TextField tf_street;
    @FXML
    TextField tf_postalCode;

    MainWindowController controller_mainWindow;
    SignUp2Controller controller_signup2;
    SignUp3Controller controller_signup3;
    SignUp4Controller controller_signup4;
    Stage stage_this;

    public SignUp5Controller() throws SQLException {
    }

    public void setData(MainWindowController mw, SignUp2Controller s2, SignUp3Controller s3, SignUp4Controller s4, Stage stage_this){
        this.controller_mainWindow = mw;
        this.controller_signup2 = s2;
        this.controller_signup3 = s3;
        this.controller_signup4 = s4;
        this.stage_this = stage_this;
    }

    public void close(){
        Platform.exit();
    }

    public void back(){
        this.controller_signup4.stage_this.show();
        this.stage_this.hide();
    }

    public void hover(){
        currentMessage = lbl_message.getText();
        lbl_message.setText("Please enter your permanent address");
    }

    public void hoverExit(){
        lbl_message.setText(currentMessage);
    }

    public void submit(ActionEvent event){
        String username = this.controller_signup2.tf_username.getText();
        String password = this.controller_signup2.pf_password.getText();
        String role = this.controller_signup2.cb_role.getSelectionModel().getSelectedItem();
        String status = "Active";

        String firstName = this.controller_signup3.tf_firstName.getText();
        String lastName = this.controller_signup3.tf_lastName.getText();
        String birthDate = this.controller_signup3.dp_birthDate.getValue().toString();
        int age = this.controller_signup3.age;
        String phoneNumber = this.controller_signup4.tf_mobile.getText();
        String telephone = this.controller_signup4.tf_telephone.getText();
        String email = this.controller_signup4.tf_email.getText();
        String sex = this.controller_signup3.cb_gender.getSelectionModel().getSelectedItem();

        String province = tf_province.getText();
        String city = tf_city.getText();
        String barangay = tf_barangay.getText();
        String street = tf_street.getText();
        String postalCode = tf_postalCode.getText();

        if (province.equals("")){
            lbl_message.setText("Province cannot be empty");
            tf_province.requestFocus();
        }else if (city.equals("")){
            lbl_message.setText("City cannot be empty");
            tf_city.requestFocus();
        }else if (barangay.equals("")){
            lbl_message.setText("Barangay Cannot be empty");
            tf_barangay.requestFocus();
        }else if (postalCode.equals("")){
            lbl_message.setText("Postal Code cannot be empty");
            tf_postalCode.requestFocus();
        }else{
            if (province.length() < 3){
                lbl_message.setText("Please enter a valid province");
                tf_province.clear();
                tf_province.requestFocus();
            }else if (city.length() < 3){
                lbl_message.setText("Please enter a valid city");
                tf_city.clear();
                tf_city.requestFocus();
            }else if (barangay.length() < 3){
                lbl_message.setText("Please enter a valid barangay");
                tf_barangay.clear();
                tf_barangay.requestFocus();
            }else if (postalCode.length() != 4){
                lbl_message.setText("Postal code must be 4-digits long");
                tf_postalCode.clear();
                tf_postalCode.requestFocus();
            }else{
                try{

                    try {
                        Integer.parseInt(postalCode);
                    }catch (Exception e){
                        this.lbl_message.setText("Postal code can only contain digits");
                        this.tf_postalCode.clear();
                        this.tf_postalCode.requestFocus();
                        return;
                    }

                    this.statement.execute(String.format("INSERT INTO `accounts` (`Username`, `Password`, `Role`, `DateCreated`, `Status`) VALUES ('%s', '%s', '%s', current_timestamp()," +
                            " '%s');", username, password, role, status));

                    this.statement.execute(String.format("INSERT INTO `user` (`UserID`, `Username`, `FirstName`, `LastName`, `BirthDate`, `Age`, `PhoneNumber`, " +
                            "`Telephone`, `Email`, `Sex`, `Province`, `City`, `Barangay`, `StreetName`, `PostalCode`) VALUES (NULL, '%s', '%s', '%s', " +
                            "'%s', %d, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');", username, firstName, lastName, birthDate, age, phoneNumber,telephone,
                            email, sex, province, city, barangay, street, postalCode));

                    this.controller_mainWindow.loadAccountsTable();

                    PopUpApplication popup = new PopUpApplication();
                    popup.start(new Stage());
                    Stage this_stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    popup.setData(this_stage, "Notice", "Account successfully created!", "Ok", "account created");

                    this.controller_signup2.stage_this.close();
                    this.controller_signup3.stage_this.close();
                    this.controller_signup4.stage_this.close();
                    this.stage_this.close();
                }catch (SQLException e){
                    System.out.println(e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
