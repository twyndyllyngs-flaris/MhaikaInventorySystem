package com.example.se;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Vendor5Controller implements Initializable {

    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/se_system", "root", "");
    Statement statement = connection.createStatement();
    ResultSet resultSet;

    @FXML
    Label lbl_message;
    @FXML
    TextField tf_deliveredTo, tf_deliveredFrom, tf_shipmentPreference, tf_fees, tf_trackingNumber;
    @FXML
    ComboBox<String> cb_paymentTerms;
    @FXML
    DatePicker dp_expectedDeliveryDate, dp_paymentDate;

    Stage stage_this;

    String currentMessage;

    MainWindowController controller_mainWindow;
    Vendor0Controller controller_vendor0;
    NewExpenseConfirmationController controller_newExpenseConfirmation;
    VendorController controller_vendor1;
    Vendor2Controller controller_vendor2;
    Vendor3Controller controller_vendor3;
    Vendor4Controller controller_vendor4;

    public Vendor5Controller() throws SQLException {
    }

    public void close(){
        if (this.controller_vendor4 != null){
            this.controller_vendor4.stage_this.close();
        }
        if (this.controller_vendor3 != null){
            this.controller_vendor3.stage_this.close();
        }

        if (this.controller_vendor2 != null){
            this.controller_vendor2.stage_this.close();
            this.controller_vendor1.stage_this.close();
        }

        if (this.controller_newExpenseConfirmation != null){
            this.controller_newExpenseConfirmation.stage_this.close();
        }

        this.controller_vendor0.stage_this.close();
        this.stage_this.close();
    }

    public void hover(){
        currentMessage = lbl_message.getText();
        lbl_message.setText("Vendor5 Message");
    }

    public void hoverExit(){
        lbl_message.setText(currentMessage);
    }

    public void submit() throws SQLException, IOException {
        String existingCompanyName = this.controller_newExpenseConfirmation == null ? null : this.controller_newExpenseConfirmation.cb_companyName.getSelectionModel().getSelectedItem();
        String companyName = this.controller_vendor1 == null ? null : this.controller_vendor1.tf_companyName.getText();
        String displayName = this.controller_vendor1 == null ? null :this.controller_vendor1.tf_displayName.getText();
        String firstname = this.controller_vendor1 == null ? null :this.controller_vendor1.tf_firstName.getText();
        String lastname = this.controller_vendor1 == null ? null :this.controller_vendor1.tf_lastName.getText();
        String mobile = this.controller_vendor2 == null ? null : this.controller_vendor2.tf_mobile.getText();
        String telephone = this.controller_vendor2 == null ? null :this.controller_vendor2.tf_telephone.getText();
        String email = this.controller_vendor2 == null ? null :this.controller_vendor2.tf_email.getText();
        String website = this.controller_vendor2 == null ? null :this.controller_vendor2.tf_website.getText();

        String expenseType = this.controller_vendor3.cb_type.getSelectionModel().getSelectedItem();
        String expenseTitle = this.controller_vendor3.tf_title.getText();
        String measurement = this.controller_vendor3.cb_measurement.getSelectionModel().getSelectedItem();
        Integer quantity = this.controller_vendor3.tf_quantity.getText().equals("") ? null : Integer.parseInt(this.controller_vendor3.tf_quantity.getText());
        Double totalItemCost = this.controller_vendor3.tf_totalItemCost.getText().equals("") ? null : Double.parseDouble(this.controller_vendor3.tf_totalItemCost.getText());
        Double costPerItem = quantity == null ? null : totalItemCost / quantity;
        String description = this.controller_vendor3.ta_description.getText();
        FileInputStream image = this.controller_vendor3.image;
        String imageName = this.controller_vendor3.tf_image.getText();

        String deliveredTo = this.tf_deliveredTo.getText();
        String deliveredFrom = this.tf_deliveredFrom.getText();
        String shipmentPreference = this.tf_shipmentPreference.getText();
        Date expectedDeliveryDate = this.dp_expectedDeliveryDate.getValue() == null ? null : Date.valueOf(this.dp_expectedDeliveryDate.getValue());
        Date paymentDate = this.dp_paymentDate.getValue() == null ? null : Date.valueOf(this.dp_paymentDate.getValue());
        String paymentTerms = this.cb_paymentTerms.getSelectionModel().getSelectedItem();
        String fees = this.tf_fees.getText();
        String trackingNumber = this.tf_trackingNumber.getText();

        String productType = this.controller_vendor4 == null ? null : this.controller_vendor4.tf_productType.getText();
        String productName = this.controller_vendor4 == null ? null : this.controller_vendor4.tf_productName.getText();
        String variant = this.controller_vendor4 == null ? null : this.controller_vendor4.tf_variant.getText();
        Double price = null;
        try {
            price = this.controller_vendor4 == null || this.controller_vendor4.tf_price.getText().equals("") ? null : Double.parseDouble(this.controller_vendor4.tf_price.getText());
        }catch (Exception e){
            
        }

        int criticalLevel = 0;

        this.resultSet = this.statement.executeQuery(String.format("SELECT * FROM Inventory WHERE ExpenseTitle = '%s'", expenseTitle));

        if (this.resultSet.next()){
            criticalLevel = this.resultSet.getInt("CriticalLevel");
        }

        Date expirationDate = this.controller_vendor4 == null ? null : this.controller_vendor4.dp_expirationDate.getValue() == null ? null :
                Date.valueOf(this.controller_vendor4.dp_expirationDate.getValue());

        if (paymentTerms.equals("Due on Receipt") && (!deliveredTo.equals("") || !deliveredFrom.equals("") || !shipmentPreference.equals("") || !trackingNumber.equals("") || expectedDeliveryDate != null || !fees.equals(""))){
            this.lbl_message.setText("Due on receipt payment doesn't go with delivery details");
            this.cb_paymentTerms.getSelectionModel().clearSelection();
            this.cb_paymentTerms.requestFocus();
        }else if (paymentTerms.equals("Cash on Delivery") && (deliveredTo.equals("") || deliveredFrom.equals("") || shipmentPreference.equals("") || expectedDeliveryDate == null)){
            if (deliveredTo.equals("")){
                this.lbl_message.setText("Delivery address is required for COD payment term");
                this.tf_deliveredTo.requestFocus();
            }else if (deliveredFrom.equals("")){
                this.lbl_message.setText("Sent from address is required for COD payment term");
                this.tf_deliveredFrom.requestFocus();
            }else if (shipmentPreference.equals("")){
                this.lbl_message.setText("Shipment preference is required for COD payment term");
                this.tf_shipmentPreference.requestFocus();
            }else {
                this.lbl_message.setText("Expected delivery date is required for COD payment term");
                this.dp_expectedDeliveryDate.requestFocus();
            }
        }else if ( (paymentTerms.equals("Cash on Delivery") || paymentTerms.equals("Payment in Advance"))  && ((!this.tf_trackingNumber.getText().equals("") || !this.tf_fees.getText().equals("")) && (this.tf_deliveredTo.getText().equals("") || this.tf_deliveredFrom.getText().equals("") || this.tf_shipmentPreference.getText().equals("") || this.dp_expectedDeliveryDate.getValue() == null)) ){
            this.lbl_message.setText("Tracking number and delivery fees must come with other delivery details");
            this.tf_deliveredTo.requestFocus();
        }else if (!paymentTerms.equals("Cash on Delivery") && paymentDate == null){
            this.lbl_message.setText("DOR and PIA must have a payment date");
            this.dp_paymentDate.requestFocus();
        }else if (paymentTerms.equals("Payment in Advance") && !deliveredTo.equals("") && (deliveredFrom.equals("") || shipmentPreference.equals("") || expectedDeliveryDate == null)){
            this.lbl_message.setText("Deliver address must come with other delivery details");
            this.tf_deliveredTo.requestFocus();
            this.tf_deliveredTo.clear();
            this.tf_deliveredFrom.clear();
            this.tf_shipmentPreference.clear();
            this.tf_trackingNumber.clear();
            this.tf_fees.clear();
            this.dp_expectedDeliveryDate.setValue(null);
        }else if (paymentTerms.equals("Payment in Advance") && !deliveredFrom.equals("") && deliveredTo.equals("")) {
            this.lbl_message.setText("Delivered-from address must come with other delivery details");
            this.tf_deliveredTo.requestFocus();
            this.tf_deliveredTo.clear();
            this.tf_deliveredFrom.clear();
            this.tf_shipmentPreference.clear();
            this.tf_trackingNumber.clear();
            this.tf_fees.clear();
            this.dp_expectedDeliveryDate.setValue(null);
        }else if (paymentTerms.equals("Payment in Advance") && !shipmentPreference.equals("") && deliveredTo.equals("")){
            this.lbl_message.setText("Shipment preference must come with other delivery details");
            this.tf_deliveredTo.requestFocus();
            this.tf_deliveredTo.clear();
            this.tf_deliveredFrom.clear();
            this.tf_shipmentPreference.clear();
            this.tf_trackingNumber.clear();
            this.tf_fees.clear();
            this.dp_expectedDeliveryDate.setValue(null);
        }else if (paymentTerms.equals("Payment in Advance") && expectedDeliveryDate != null && deliveredTo.equals("")){
            this.lbl_message.setText("Expected delivery date must come with other delivery details");
            this.tf_deliveredTo.requestFocus();
            this.tf_deliveredTo.clear();
            this.tf_deliveredFrom.clear();
            this.tf_shipmentPreference.clear();
            this.tf_trackingNumber.clear();
            this.tf_fees.clear();
            this.dp_expectedDeliveryDate.setValue(null);
        }else {
            if (!tf_fees.getText().equals("")) {
                try {
                    Double.parseDouble(fees);
                } catch (Exception e) {
                    this.lbl_message.setText("Fees should only be numerical");
                    this.tf_fees.clear();
                    this.tf_fees.requestFocus();
                    return;
                }
            }

            if (!tf_fees.getText().equals("") && Double.parseDouble(fees) < 0) {
                this.lbl_message.setText("Fees cannot be lower than 0");
                this.tf_fees.clear();
                this.tf_fees.requestFocus();
            } else {
                String productStatus = paymentTerms.equals("Due on Receipt") ? "Active" : "Inactive (To Be Delivered)";
                String expenseStatus = paymentTerms.equals("Due on Receipt") ? "Transaction Completed" : "Transaction on Process";

                Double feesDouble = this.tf_fees.getText().equals("") ? 0 : Double.parseDouble(this.tf_fees.getText());
                Double totalExpenseCost = totalItemCost + feesDouble;

                if (this.controller_newExpenseConfirmation != null) {

                    expenseTitle = expenseTitle.equals("") ? productType + ", " + productName + ", " + variant : expenseTitle;

                    if (existingCompanyName == null) {
                        this.statement.execute(String.format("INSERT INTO vendors (CompanyName, FirstName, LastName, DisplayName, Email, WorkPhone, PhoneNumber, Website) " +
                                "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');", companyName, firstname, lastname, displayName, email, telephone, mobile, website));

                        controller_mainWindow.loadVendorsTable();
                    } else {
                        companyName = existingCompanyName;
                    }

                    PreparedStatement pstmt = this.connection.prepareStatement(String.format("INSERT INTO `expenses` (`ExpenseNumber`, `Image`, `ImageName`, " +
                                    "`ExpenseType`, `CompanyName`, `ExpenseTitle`, `Measurement`, `Quantity`, `CostPerItem`, `TotalItemCost`, `Description`, `DeliverTo`, " +
                                    "`DeliveredFrom`, `ShipmentPreference`, `TrackingNumber`, `DeliveryFees`, `ExpectedDeliveryDate`, `DateDelivered`, " +
                                    "`PaymentDate`, `PaymentTerms`, `TotalExpenseCost`, `DateAdded`, `Status`) VALUES (NULL, ?, '%s', '%s', '%s', '%s', '%s', ?, " +
                                    "?, %.2f, '%s', '%s', '%s', '%s', '%s', %.2f, ?, NULL, ?, '%s', %.2f, current_timestamp(), '%s');",
                            imageName, expenseType, companyName, expenseTitle, measurement, totalItemCost, description, deliveredTo, deliveredFrom,
                            shipmentPreference, trackingNumber, feesDouble, paymentTerms, totalExpenseCost, expenseStatus));

                    pstmt.setBinaryStream(1, image);
                    pstmt.setObject(2, quantity, Types.INTEGER);
                    pstmt.setObject(3, costPerItem == null ? costPerItem : Double.parseDouble(String.format("%.2f", costPerItem)), Types.DOUBLE);
                    pstmt.setDate(4, expectedDeliveryDate);
                    pstmt.setDate(5, paymentDate);
                    pstmt.execute();

                    if (expenseType.equals("Product")) {
                        this.resultSet = this.statement.executeQuery("SELECT ExpenseNumber FROM `expenses` ORDER BY ExpenseNumber DESC LIMIT 1");
                        this.resultSet.next();
                        String productExpenseNumber = resultSet.getString(1);

                        pstmt = this.connection.prepareStatement(String.format("INSERT INTO inventory (ExpenseNumber, Image, ExpenseTitle, ProductType, ProductName, " +
                                        "Variant, Stock, Price, Description, ExpirationDate, CriticalLevel, DateAdded, Status) VALUES ('%s', ?, '%s', '%s', '%s', " +
                                        "'%s', %d, ?, '%s', ?, %d, current_timestamp(), '%s');", productExpenseNumber, expenseTitle, productType, productName,
                                variant, quantity, description, criticalLevel, productStatus));
                        pstmt.setBinaryStream(1, image);
                        pstmt.setObject(2, price, Types.DOUBLE);
                        pstmt.setDate(3, expirationDate);
                        pstmt.execute();

                        this.controller_mainWindow.loadInventoryTable();
                    }

                    this.controller_mainWindow.loadExpensesTable();

                    PopUpApplication popup = new PopUpApplication();
                    popup.start(new Stage());
                    popup.setData(this.controller_mainWindow.stage_this, "Notice", "Records successfully added", "Ok", "close");
                    this.close();
                } else {
                    this.resultSet = this.statement.executeQuery(String.format("SELECT * FROM `expenses` WHERE ExpenseTitle LIKE '%s' ORDER BY ExpenseNumber DESC LIMIT 1",
                            this.controller_vendor0.cb_expenseTitle.getSelectionModel().getSelectedItem()));
                    this.resultSet.next();

                    System.out.println(this.resultSet.getString("ExpenseNumber") + " " + this.resultSet.getString("Measurement") + this.resultSet.getString("Description"));

                    PreparedStatement pstmt = this.connection.prepareStatement(String.format("INSERT INTO `expenses` (`ExpenseNumber`, `Image`, `ImageName`, " +
                                    "`ExpenseType`, `CompanyName`, `ExpenseTitle`, `Measurement`, `Quantity`, `CostPerItem`, `TotalItemCost`, `Description`, `DeliverTo`, " +
                                    "`DeliveredFrom`, `ShipmentPreference`, `TrackingNumber`, `DeliveryFees`, `ExpectedDeliveryDate`, `DateDelivered`, " +
                                    "`PaymentDate`, `PaymentTerms`, `TotalExpenseCost`, `DateAdded`, `Status`) VALUES (NULL, ?, '%s', '%s', '%s', '%s', '%s', ?, " +
                                    "?, %.2f, '%s', '%s', '%s', '%s', '%s', %.2f, ?, NULL, ?, '%s', %.2f, current_timestamp(), '%s');",
                            this.resultSet.getString("ImageName"), this.resultSet.getString("ExpenseType"), this.resultSet.getString("CompanyName"),
                            this.resultSet.getString(("ExpenseTitle")), this.resultSet.getString("Measurement"), totalItemCost, this.resultSet.getString("Description"),
                            deliveredTo, deliveredFrom, shipmentPreference, trackingNumber, feesDouble, paymentTerms, totalExpenseCost, expenseStatus));

                    pstmt.setBinaryStream(1, this.resultSet.getBinaryStream("Image"));
                    pstmt.setObject(2, quantity, Types.INTEGER);
                    pstmt.setObject(3, costPerItem == null ? costPerItem : Double.parseDouble(String.format("%.2f", costPerItem)), Types.DOUBLE);
                    pstmt.setDate(4, expectedDeliveryDate);
                    pstmt.setDate(5, paymentDate);
                    pstmt.execute();

                    if (this.resultSet.getString("ExpenseType").equals("Product")) {
                        this.resultSet = this.statement.executeQuery(String.format("SELECT * FROM `inventory` WHERE ExpenseTitle = '%s' AND Status = 'Active' OR Status = 'Inactive (Out of Stock)';",
                                this.controller_vendor0.cb_expenseTitle.getSelectionModel().getSelectedItem()));

                        String productID = null;
                        boolean isEqual = false;

                        while (this.resultSet.next()) {
                            productID = this.resultSet.getString("ProductID");

                            if (expirationDate == null || expirationDate.equals(this.resultSet.getDate("ExpirationDate"))) {
                                isEqual = true;
                                break;
                            }
                        }

                        if (paymentTerms.equals("Due on Receipt") && productID != null && isEqual) {
                            this.statement.execute(String.format("UPDATE inventory SET Stock = Stock + %d WHERE ProductID = '%s'", quantity, productID));
                            this.statement.execute(String.format("UPDATE inventory SET Status = 'Active' WHERE ProductID = '%s'", productID));
                        } else {
                            if (paymentTerms.equals("Due on Receipt") && productID == null) {
                                productStatus = "Active";
                            }

                            this.resultSet = this.statement.executeQuery("SELECT ExpenseNumber FROM `expenses` ORDER BY ExpenseNumber DESC LIMIT 1");
                            this.resultSet.next();

                            String expenseNumber = this.resultSet.getString("ExpenseNumber");

                            this.resultSet = this.statement.executeQuery(String.format("SELECT * FROM `inventory` WHERE ExpenseTitle = '%s'", this.controller_vendor0.cb_expenseTitle.getSelectionModel().getSelectedItem()));
                            this.resultSet.next();

                            pstmt = this.connection.prepareStatement(String.format("INSERT INTO inventory (ExpenseNumber, Image, ExpenseTitle, ProductType, ProductName, " +
                                            "Variant, Stock, Price, Description, ExpirationDate, CriticalLevel, DateAdded, Status) VALUES ('%s', ?, '%s', '%s', '%s', " +
                                            "'%s', %d, ?, '%s', ?, %d, current_timestamp(), '%s');", expenseNumber, this.resultSet.getString("ExpenseTitle"),
                                    this.resultSet.getString("ProductType"), this.resultSet.getString("ProductName"), this.resultSet.getString("Variant"),
                                    quantity, this.resultSet.getString("Description"), this.resultSet.getInt("CriticalLevel"), productStatus));
                            pstmt.setBinaryStream(1, this.resultSet.getBinaryStream("Image"));
                            pstmt.setObject(2, this.resultSet.getDouble("Price"), Types.DOUBLE);
                            pstmt.setObject(3, expirationDate == null ? this.resultSet.getDate("ExpirationDate") : expirationDate);
                            pstmt.execute();
                        }

                        this.controller_mainWindow.loadInventoryTable();
                    }

                    this.controller_mainWindow.loadExpensesTable();

                    PopUpApplication popup = new PopUpApplication();
                    popup.start(new Stage());
                    popup.setData(this.controller_mainWindow.stage_this, "Notice", "Records successfully added", "Ok", "close");
                    this.close();
                }
            }
        }

        this.controller_mainWindow.dataChanged();
    }

    public int computeCriticalLevel(String expenseTitle) throws SQLException {
        this.resultSet = this.statement.executeQuery(String.format("SELECT CAST(COALESCE(SUM(Quantity) / DAY(LAST_DAY(CURRENT_DATE())), 0) AS INT) as AverageDemand " +
                "FROM sales WHERE Product = '%s' AND DATE_FORMAT(SaleDate, '%%Y-%%m') =" +
                " DATE_FORMAT(DATE_SUB(CURRENT_DATE(), INTERVAL 1 MONTH), '%%Y-%%m')", expenseTitle));
        this.resultSet.next();

        int averageDemand = this.resultSet.getInt("AverageDemand");
        int leadTime = 7;
        int second = averageDemand * leadTime;

        this.resultSet = this.statement.executeQuery(String.format("SELECT COALESCE(MAX(Quantity), 0) as MaxDemand " +
                "FROM sales WHERE Product = '%s' AND " +
                "DATE_FORMAT(SaleDate, '%%Y-%%m') = DATE_FORMAT(DATE_SUB(CURRENT_DATE(), INTERVAL 1 MONTH), '%%Y-%%m')", expenseTitle));
        this.resultSet.next();

        int maxDemand = this.resultSet.getInt("MaxDemand");
        int maxTime = 15;
        int first = maxDemand * maxTime;

        int low = first - second;

        return low;
    }

    public void back(){
        if (this.controller_vendor4 != null){
            this.controller_vendor4.stage_this.show();
        }else{
            this.controller_vendor3.stage_this.show();
        }

        this.stage_this.close();
    }

    public void setData(MainWindowController mw, Vendor0Controller v0, NewExpenseConfirmationController nec, VendorController v, Vendor2Controller v2, Vendor3Controller v3, Vendor4Controller v4, Stage stage_this){
        this.controller_mainWindow = mw;
        this.controller_vendor0 = v0;
        this.controller_newExpenseConfirmation = nec;
        this.controller_vendor1 = v;
        this.controller_vendor2 = v2;
        this.controller_vendor3 = v3;
        this.controller_vendor4 = v4;
        this.stage_this = stage_this;

        String measurement = v3.cb_measurement.getSelectionModel().getSelectedItem();
        if (!measurement.equals("by Item")){
            this.cb_paymentTerms.getSelectionModel().select("Due on Receipt");
            this.cb_paymentTerms.setDisable(true);
        }
    }

    public void changePaymentTerms(){
        String paymentTerms = cb_paymentTerms.getSelectionModel().getSelectedItem();
        if (paymentTerms.equals("Due on Receipt")){
            this.tf_fees.setDisable(true);
            this.tf_fees.clear();
            this.tf_deliveredTo.setDisable(true);
            this.tf_deliveredTo.clear();
            this.tf_deliveredFrom.setDisable(true);
            this.tf_deliveredFrom.clear();
            this.tf_shipmentPreference.setDisable(true);
            this.tf_shipmentPreference.clear();
            this.tf_trackingNumber.setDisable(true);
            this.tf_trackingNumber.clear();
            this.dp_expectedDeliveryDate.setDisable(true);
            this.dp_expectedDeliveryDate.setValue(null);
            this.dp_paymentDate.setDisable(false);
        }else if (paymentTerms.equals("Cash on Delivery")){
            this.tf_fees.setDisable(false);
            this.tf_deliveredTo.setDisable(false);
            this.tf_deliveredFrom.setDisable(false);
            this.tf_shipmentPreference.setDisable(false);
            this.tf_trackingNumber.setDisable(false);
            this.dp_expectedDeliveryDate.setDisable(false);
            this.dp_paymentDate.setValue(null);
            this.dp_paymentDate.setDisable(true);
        }else{
            this.tf_fees.setDisable(false);
            this.tf_deliveredTo.setDisable(false);
            this.tf_deliveredFrom.setDisable(false);
            this.tf_shipmentPreference.setDisable(false);
            this.tf_trackingNumber.setDisable(false);
            this.dp_expectedDeliveryDate.setDisable(false);
            this.dp_paymentDate.setDisable(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.resultSet = this.statement.executeQuery("SELECT DISTINCT DeliverTo, DeliveredFrom, ShipmentPreference FROM expenses");

            while (resultSet.next()){
                TextFields.bindAutoCompletion(this.tf_shipmentPreference, this.resultSet.getString("ShipmentPreference")).setDelay(0);
                TextFields.bindAutoCompletion(this.tf_deliveredTo, this.resultSet.getString("DeliverTo")).setDelay(0);
                TextFields.bindAutoCompletion(this.tf_deliveredFrom, this.resultSet.getString("DeliveredFrom")).setDelay(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        cb_paymentTerms.getItems().addAll("Due on Receipt", "Cash on Delivery", "Payment in Advance");
        cb_paymentTerms.getSelectionModel().select("Cash on Delivery");
        this.changePaymentTerms();
    }
}
