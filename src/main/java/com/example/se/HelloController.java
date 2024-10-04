package com.example.se;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;

public class HelloController {
    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/se_system", "root", "");
    Statement statement = connection.createStatement();
    ResultSet resultSet;

    @FXML
    TextField tf_username;
    @FXML
    PasswordField pf_password;
    @FXML
    Label lbl_message;
    String currentMessage;

    @FXML
    Button pb_close;

    private double xOffset = 0;
    private double yOffset = 0;

    Stage stage;
    Scene scene;

    public HelloController() throws SQLException {
    }

    public void login(ActionEvent event) throws SQLException, IOException {
        String username = tf_username.getText();
        String password = pf_password.getText();

        if (username.equals("")){
            lbl_message.setText("Username cannot be empty");
            tf_username.requestFocus();
        }else if (password.equals("")){
            lbl_message.setText("Password cannot be empty");
            pf_password.requestFocus();
        }else{
            resultSet = statement.executeQuery(String.format("SELECT * FROM accounts WHERE Username = '%s' AND Password = '%s';", username.trim(), password.trim()));

            if (resultSet.next()){
                if (!resultSet.getString("Status").equals("Active")){
                    lbl_message.setText("Account is currently disabled. Contact an administrator for further inquiries");
                }else {
                    MainWindowApplication window = new MainWindowApplication();
                    String role = this.resultSet.getString("Role");

                    if (role.equals("Employee")){
                        window.addFXML("employee.fxml");
                    }else {
                        window.addFXML("mainwindow.fxml");
                    }

                    window.start(new Stage());
                    Stage this_stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    window.setData(username, password);
                    this_stage.close();

                    try {
                        this.resultSet = this.statement.executeQuery("SELECT DISTINCT ExpenseTitle FROM inventory WHERE Status = 'Active'");

                        LinkedList<String> titles = new LinkedList<>();

                        while (this.resultSet.next()){
                            String et = this.resultSet.getString("ExpenseTitle");
                            titles.add(et);
                        }

                        for (int i = 0; i < titles.size(); i++){
                            if (checkCriticalLevel(titles.get(i))){
                                PopUpApplication popup = new PopUpApplication();
                                popup.start(new Stage());
                                popup.setData(this.stage, "Warning", titles.get(i) + " is under critical level. Restock as soon as possible",
                                        "Ok", "close");
                            }
                        }
                    } catch (SQLException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                lbl_message.setText("Credentials incorrect");
                pf_password.clear();
                pf_password.requestFocus();
            }
        }
    }

    public boolean checkCriticalLevel(String expenseTitle) throws SQLException {
        String sql = String.format("SELECT SUM(Stock) AS TotalStock, CriticalLevel FROM inventory WHERE ExpenseTitle = '%s' AND Status = 'Active'", expenseTitle);

        this.resultSet = this.statement.executeQuery(sql);
        this.resultSet.next();

        int ts = this.resultSet.getInt("TotalStock");
        int cl = this.resultSet.getInt("CriticalLevel");

        return ts <= cl ;
    }

    public void close(){
        Platform.exit();
    }

    public void signup(ActionEvent event) throws IOException {
        /*stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.hide();

        SignUp1Application s1 = new SignUp1Application();
        s1.start(new Stage());
        s1.setData(stage);*/
    }
}