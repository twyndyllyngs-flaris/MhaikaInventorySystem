package com.example.se;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Vendor2Controller {

    String currentMessage, from;

    Stage stage_this;

    MainWindowController controller_mainWindow;
    Vendor0Controller controller_vendor0;
    NewExpenseConfirmationController controller_newExpenseConfirmation;
    VendorController controller_vendor1;

    @FXML
    Label lbl_message;
    @FXML
    TextField tf_mobile, tf_telephone, tf_email, tf_website;

    public Vendor2Controller() {
    }

    public void hover(){
        currentMessage = lbl_message.getText();
        lbl_message.setText("Please provide record for either mobile or telephone");
    }

    public void hoverExit(){
        lbl_message.setText(currentMessage);
    }

    public void close(){
        this.controller_vendor0.stage_this.close();
        this.controller_newExpenseConfirmation.stage_this.close();
        this.controller_vendor1.stage_this.close();
        this.stage_this.close();
    }

    public void submit() throws SQLException, IOException {
        String mobile = this.tf_mobile.getText();
        String telephone = this.tf_telephone.getText();
        String email = this.tf_email.getText();
        String website = this.tf_website.getText();

        if (mobile.equals("") && telephone.equals("")){
            this.lbl_message.setText("Please fill-up either mobile or telephone");
            this.tf_mobile.requestFocus();
        }else if ( (!mobile.equals("") && telephone.equals("")) && (mobile.length() != 11) ){
            this.lbl_message.setText("Phone number is invalid");
            this.tf_mobile.clear();
            this.tf_mobile.requestFocus();
        }else if ((!mobile.equals("") && !telephone.equals("")) && mobile.length() != 11){
            this.lbl_message.setText("Phone number is invalid");
            this.tf_mobile.clear();
            this.tf_mobile.requestFocus();
        }else {
            boolean b = !email.equals("") && !email.contains("@") && !email.contains(".");

            if (!mobile.equals("")){
                try{
                    Long.parseLong(mobile);
                }catch (Exception e){
                    this.lbl_message.setText("Phone number is invalid");
                    this.tf_mobile.clear();
                    this.tf_mobile.requestFocus();
                    return;
                }

                if (Long.parseLong(mobile) < 0){
                    this.lbl_message.setText("Phone number is invalid");
                    this.tf_mobile.clear();
                    this.tf_mobile.requestFocus();
                }else{
                    if (b){
                        this.lbl_message.setText("Email is invalid");
                        this.tf_email.clear();
                        this.tf_email.requestFocus();
                    } else if (!website.equals("") && !website.contains(".")){
                        this.lbl_message.setText("Website is invalid");
                        this.tf_website.clear();
                        this.tf_website.requestFocus();
                    }else{
                        Vendor3Application application = new Vendor3Application();
                        application.start(new Stage());
                        application.setData(this.controller_mainWindow, this.controller_vendor0, this.controller_newExpenseConfirmation, this.controller_vendor1, this, from);
                        this.stage_this.hide();
                    }
                }
            }else{
                if (b){
                        this.lbl_message.setText("Email is invalid");
                        this.tf_email.clear();
                        this.tf_email.requestFocus();
                } else if (!website.equals("") && !website.contains(".")){
                    this.lbl_message.setText("Website is invalid");
                    this.tf_website.clear();
                    this.tf_website.requestFocus();
                }else{
                    Vendor3Application application = new Vendor3Application();
                    application.start(new Stage());
                    application.setData(this.controller_mainWindow, this.controller_vendor0, this.controller_newExpenseConfirmation, this.controller_vendor1, this, from);
                    this.stage_this.hide();
                }
            }
        }

    }

    public void back(){
        this.controller_vendor1.stage_this.show();
        this.stage_this.close();
    }

    public void setData(MainWindowController controller_mainWindow, Vendor0Controller controller_vendor0, NewExpenseConfirmationController controller_newExpenseConfirmation, VendorController controller_vendor1, Stage stage_this, String from){
        this.controller_mainWindow = controller_mainWindow;
        this.controller_vendor0 = controller_vendor0;
        this.controller_newExpenseConfirmation = controller_newExpenseConfirmation;
        this.controller_vendor1 = controller_vendor1;
        this.stage_this = stage_this;
        this.from = from;
    }
}
