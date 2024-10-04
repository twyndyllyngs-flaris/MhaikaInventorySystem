package com.example.se;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PopUpController {
    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/se_system", "root", "");
    Statement statement = connection.createStatement();
    ResultSet resultSet;

    Stage stage_this, stage_from;

    String title;
    String message;
    String buttonText;
    String from;

    @FXML
    Label lbl_message;
    @FXML
    Label lbl_title;
    @FXML
    Button pb_submit;

    public PopUpController() throws SQLException {
    }

    public void close(ActionEvent event){
        if (from.equals("change expense status") || from.equals("datedelivered_status") || from.equals("payment date set") || from.equals("payment date change")){
            MainWindowController controller_mainWindow = (MainWindowController) this.stage_from.getUserData();
            controller_mainWindow.loadExpensesTable();
        }else if (from.equals("vendor name")){
            MainWindowController controller_mainWindow = (MainWindowController) this.stage_from.getUserData();
            controller_mainWindow.loadVendorsTable();
        }

        this.stage_this = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.stage_this.close();
    }

    public void setData(Stage stage_from, String title, String message, String buttonText, String functionName){
        this.title = title;
        this.message = message;
        this.buttonText = buttonText;
        this.from = functionName;
        this.stage_from = stage_from;

        setLabels();
    }

    public void setLabels(){
        lbl_title.setText(this.title);
        lbl_message.setText(this.message);
        pb_submit.setText(this.buttonText);
    }

    public void submit(ActionEvent event) throws IOException, SQLException {
        if (this.from.equals("account created")){
            this.stage_this = (Stage)((Node)event.getSource()).getScene().getWindow();
            this.stage_this.close();
        } else if (this.from.equals("logout")){
            stage_from.close();

            HelloApplication window_login = new HelloApplication();
            window_login.start(new Stage());

            stage_this = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage_this.close();
        } else if (this.from.equals("exit application")){
            Platform.exit();
        } else if (this.from.equals("close")){
            this.stage_this = (Stage)((Node)event.getSource()).getScene().getWindow();
            this.stage_this.close();
        } else if (this.from.equals("change expense status")){
            MainWindowController controller_mainWindow = (MainWindowController) this.stage_from.getUserData();
            String oldStatus = controller_mainWindow.expenses_oldStatus;
            String newStatus = controller_mainWindow.expenses_newStatus;
            String expenseNumber = controller_mainWindow.expenses_selectedExpenseNumber;

            this.resultSet = this.statement.executeQuery(String.format("SELECT * FROM expenses WHERE ExpenseNumber = '%s'", expenseNumber));
            this.resultSet.next();

            String paymentDate = resultSet.getString("PaymentDate");
            String dd = this.resultSet.getString("DateDelivered");

            if (this.resultSet.getString("ExpenseType").equals("Product")){
                if (oldStatus.equals("Transaction on Process") && newStatus.equals("Transaction Completed")){
                    this.resultSet = this.statement.executeQuery(String.format("SELECT * FROM inventory WHERE ExpenseNumber = '%s'", expenseNumber));
                    this.resultSet.next();

                    String expenseTitle = this.resultSet.getString("ExpenseTitle");
                    int stock = this.resultSet.getInt("Stock");

                    this.resultSet = this.statement.executeQuery(String.format("SELECT * FROM inventory WHERE ExpenseTitle = '%s' AND Status = 'Active'", expenseTitle));

                    if (resultSet.next()){
                        String productID = this.resultSet.getString("ProductID");

                        this.statement.execute(String.format("UPDATE inventory SET Stock = Stock + %d WHERE ProductID = '%s'", stock, productID));
                        this.statement.execute(String.format("DELETE FROM `inventory` WHERE ExpenseNumber = '%s'", expenseNumber));
                    }else {
                        this.statement.execute(String.format("UPDATE inventory SET Status = 'Active' WHERE ExpenseNumber = '%s'", expenseNumber));
                    }

                    this.resultSet = this.statement.executeQuery(String.format("SELECT * FROM expenses WHERE ExpenseNumber = '%s'", expenseNumber));
                    this.resultSet.next();
                }else if (oldStatus.equals("Transaction on Process") && newStatus.equals("Cancelled")){
                    this.statement.execute(String.format("UPDATE inventory SET Status = 'Inactive (Cancelled)' WHERE ExpenseNumber = '%s'",  expenseNumber));
                }

                controller_mainWindow.loadInventoryTable();
                controller_mainWindow.dataChanged();
            }



            if (dd == null && !controller_mainWindow.currentRow.getDeliverTo().equals("")){
                this.statement.execute(String.format("UPDATE expenses SET DateDelivered = current_timestamp() WHERE ExpenseNumber = '%s'", expenseNumber));
            }

            if ( paymentDate == null){
                this.statement.execute(String.format("UPDATE expenses SET PaymentDate = current_timestamp() WHERE ExpenseNumber = '%s'", expenseNumber));
            }

            this.statement.execute(String.format("UPDATE expenses SET Status = '%s' WHERE ExpenseNumber = '%s'", newStatus, expenseNumber));

            controller_mainWindow.loadExpensesTable();

            this.stage_this = (Stage)((Node)event.getSource()).getScene().getWindow();
            this.stage_this.close();
        }else if (this.from.equals("datedelivered_status")){
            MainWindowController controller_mainWindow = (MainWindowController) this.stage_from.getUserData();

            this.statement.execute(String.format("UPDATE expenses SET DateDelivered = '%s' WHERE ExpenseNumber = '%s'", controller_mainWindow.expenses_newDateDelivered,
                    controller_mainWindow.expenses_selectedExpenseNumber));

            if (controller_mainWindow.currentRow.getPaymentTerms().equals("Cash on Delivery"))
                 this.statement.execute(String.format("UPDATE expenses SET PaymentDate = '%s' WHERE ExpenseNumber = '%s'", controller_mainWindow.expenses_newDateDelivered,
                            controller_mainWindow.expenses_selectedExpenseNumber));

            controller_mainWindow.expenses_oldStatus = "Transaction on Process";
            controller_mainWindow.expenses_newStatus = "Transaction Completed";

            this.from = "change expense status";
            this.submit(event);
        }else if (this.from.equals("vendor name")){
            MainWindowController controller_mainWindow = (MainWindowController) this.stage_from.getUserData();

            this.statement.execute(String.format("UPDATE vendors SET FirstName = '', LastName = '' WHERE CompanyName = '%s'", controller_mainWindow.vendors_selected_companyName));
            controller_mainWindow.loadVendorsTable();

            this.stage_this = (Stage)((Node)event.getSource()).getScene().getWindow();
            this.stage_this.close();
        }else if (this.from.equals("payment date set")){
            MainWindowController controller_mainWindow = (MainWindowController) this.stage_from.getUserData();

            this.statement.execute(String.format("UPDATE expenses SET Status = 'Transaction Completed', DateDelivered = '%s', PaymentDate = '%s'  WHERE ExpenseNumber = '%s'",
                    controller_mainWindow.expenses_newPaymentDate, controller_mainWindow.expenses_newPaymentDate, controller_mainWindow.currentRow.getExpenseNumber()));

            controller_mainWindow.expenses_oldStatus = "Transaction on Process";
            controller_mainWindow.expenses_newStatus = "Transaction Completed";

            this.from = "change expense status";
            this.submit(event);
        }else if (this.from.equals("payment date change")){
            MainWindowController controller_mainWindow = (MainWindowController) this.stage_from.getUserData();

            this.statement.execute(String.format("UPDATE expenses SET DateDelivered = '%s', PaymentDate = '%s' WHERE ExpenseNumber = '%s'",
                    controller_mainWindow.expenses_newPaymentDate, controller_mainWindow.expenses_newPaymentDate, controller_mainWindow.currentRow.getExpenseNumber()));

            controller_mainWindow.loadExpensesTable();

            this.stage_this = (Stage)((Node)event.getSource()).getScene().getWindow();
            this.stage_this.close();
        }else if (this.from.equals("revert username")){
            MainWindowController controller_mainWindow = (MainWindowController) this.stage_from.getUserData();
            controller_mainWindow.account_tf_username.setText(controller_mainWindow.account_ll_accountUser.get(0));

            this.stage_this = (Stage)((Node)event.getSource()).getScene().getWindow();
            this.stage_this.close();
        }else if (this.from.equals("revert account-user")){
            MainWindowController controller_mainWindow = (MainWindowController) this.stage_from.getUserData();

            controller_mainWindow.account_tf_username.setText(controller_mainWindow.account_ll_accountUser.get(0));
            controller_mainWindow.account_tf_password.setText(controller_mainWindow.account_ll_accountUser.get(1));
            controller_mainWindow.account_tf_firstName.setText(controller_mainWindow.account_ll_accountUser.get(2));
            controller_mainWindow.account_tf_lastName.setText(controller_mainWindow.account_ll_accountUser.get(3));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(controller_mainWindow.account_ll_accountUser.get(4), formatter);
            controller_mainWindow.account_dp_birthDate.setValue(localDate);

            controller_mainWindow.account_resetAccountUserLabel();

            this.stage_this = (Stage)((Node)event.getSource()).getScene().getWindow();
            this.stage_this.close();
        }else if (this.from.equals("revert password")){
            MainWindowController controller_mainWindow = (MainWindowController) this.stage_from.getUserData();
            controller_mainWindow.account_tf_password.setText(controller_mainWindow.account_ll_accountUser.get(1));

            this.stage_this = (Stage)((Node)event.getSource()).getScene().getWindow();
            this.stage_this.close();
        }else if (this.from.equals("revert first name")){
            MainWindowController controller_mainWindow = (MainWindowController) this.stage_from.getUserData();
            controller_mainWindow.account_tf_firstName.setText(controller_mainWindow.account_ll_accountUser.get(2));

            this.stage_this = (Stage)((Node)event.getSource()).getScene().getWindow();
            this.stage_this.close();
        }else if (this.from.equals("revert last name")){
            MainWindowController controller_mainWindow = (MainWindowController) this.stage_from.getUserData();
            controller_mainWindow.account_tf_lastName.setText(controller_mainWindow.account_ll_accountUser.get(3));

            this.stage_this = (Stage)((Node)event.getSource()).getScene().getWindow();
            this.stage_this.close();
        }else if (this.from.equals("revert birth date")){
            MainWindowController controller_mainWindow = (MainWindowController) this.stage_from.getUserData();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(controller_mainWindow.account_ll_accountUser.get(4), formatter);
            controller_mainWindow.account_dp_birthDate.setValue(localDate);

            this.stage_this = (Stage)((Node)event.getSource()).getScene().getWindow();
            this.stage_this.close();
        }else if (this.from.equals("revert contact-address")){
            MainWindowController controller_mainWindow = (MainWindowController) this.stage_from.getUserData();

            controller_mainWindow.account_tf_phone.setText(controller_mainWindow.account_ll_contactAddress.get(0));
            controller_mainWindow.account_tf_telephone.setText(controller_mainWindow.account_ll_contactAddress.get(1));
            controller_mainWindow.account_tf_email.setText(controller_mainWindow.account_ll_contactAddress.get(2));
            controller_mainWindow.account_tf_province.setText(controller_mainWindow.account_ll_contactAddress.get(3));
            controller_mainWindow.account_tf_city.setText(controller_mainWindow.account_ll_contactAddress.get(4));
            controller_mainWindow.account_tf_barangay.setText(controller_mainWindow.account_ll_contactAddress.get(5));
            controller_mainWindow.account_tf_street.setText(controller_mainWindow.account_ll_contactAddress.get(6));
            controller_mainWindow.account_tf_postalCode.setText(controller_mainWindow.account_ll_contactAddress.get(7));

            controller_mainWindow.account_resetContactAddressLabel();

            this.stage_this = (Stage)((Node)event.getSource()).getScene().getWindow();
            this.stage_this.close();
        }else if (this.from.equals("revert phone")){
            MainWindowController controller_mainWindow = (MainWindowController) this.stage_from.getUserData();
            controller_mainWindow.account_tf_phone.setText(controller_mainWindow.account_ll_contactAddress.get(0));

            this.stage_this = (Stage)((Node)event.getSource()).getScene().getWindow();
            this.stage_this.close();
        }else if (this.from.equals("revert telephone")){
            MainWindowController controller_mainWindow = (MainWindowController) this.stage_from.getUserData();
            controller_mainWindow.account_tf_telephone.setText(controller_mainWindow.account_ll_contactAddress.get(1));

            this.stage_this = (Stage)((Node)event.getSource()).getScene().getWindow();
            this.stage_this.close();
        }else if (this.from.equals("revert email")){
            MainWindowController controller_mainWindow = (MainWindowController) this.stage_from.getUserData();
            controller_mainWindow.account_tf_email.setText(controller_mainWindow.account_ll_contactAddress.get(2));

            this.stage_this = (Stage)((Node)event.getSource()).getScene().getWindow();
            this.stage_this.close();
        }else if (this.from.equals("revert province")){
            MainWindowController controller_mainWindow = (MainWindowController) this.stage_from.getUserData();
            controller_mainWindow.account_tf_province.setText(controller_mainWindow.account_ll_contactAddress.get(3));

            this.stage_this = (Stage)((Node)event.getSource()).getScene().getWindow();
            this.stage_this.close();
        }else if (this.from.equals("revert city")){
            MainWindowController controller_mainWindow = (MainWindowController) this.stage_from.getUserData();
            controller_mainWindow.account_tf_city.setText(controller_mainWindow.account_ll_contactAddress.get(4));

            this.stage_this = (Stage)((Node)event.getSource()).getScene().getWindow();
            this.stage_this.close();
        }else if (this.from.equals("revert barangay")){
            MainWindowController controller_mainWindow = (MainWindowController) this.stage_from.getUserData();
            controller_mainWindow.account_tf_barangay.setText(controller_mainWindow.account_ll_contactAddress.get(5));

            this.stage_this = (Stage)((Node)event.getSource()).getScene().getWindow();
            this.stage_this.close();
        }else if (this.from.equals("revert street")){
            MainWindowController controller_mainWindow = (MainWindowController) this.stage_from.getUserData();
            controller_mainWindow.account_tf_street.setText(controller_mainWindow.account_ll_contactAddress.get(6));

            this.stage_this = (Stage)((Node)event.getSource()).getScene().getWindow();
            this.stage_this.close();
        }else if (this.from.equals("revert postal code")){
            MainWindowController controller_mainWindow = (MainWindowController) this.stage_from.getUserData();
            controller_mainWindow.account_tf_postalCode.setText(controller_mainWindow.account_ll_contactAddress.get(7));

            this.stage_this = (Stage)((Node)event.getSource()).getScene().getWindow();
            this.stage_this.close();
        }
    }
}
