package com.example.se;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.ResourceBundle;

public class AddSaleController implements Initializable {
    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:8111/se_system", "root", "");
    Statement statement = connection.createStatement();
    ResultSet resultSet;

    Stage stage_this;

    MainWindowController controller_mainWindow;

    @FXML
    Button sales_pb_message, sales_pb_checkOut, sales_pb_close;

    @FXML
    Label sales_lbl_add, sales_lbl_message, sales_lbl_totalPrice, sales_lbl_selectProductMessage;

    @FXML
    TextField sales_tf_totalBill, sales_tf_quantity, sales_tf_price, sales_tf_change, sales_tf_cash;

    @FXML
    ComboBox<String> sales_cb_productName;

    @FXML
    ListView<String> sales_lv_cart;

    HashMap<String, OrderList> products = new HashMap<>();
    LinkedList<String> productsC = new LinkedList<>();

    //HashMap<Integer, String> showProductsMessage = new HashMap<>();
    String currentError = "";

    String receiptID = "";

    double totalBill;
    double change;
    double cash;

    static class Cell extends ListCell<String>{
        HBox hbox = new HBox();
        Button btn = new Button("Remove");
        Label label = new Label("");
        Label label2 = new Label("");
        Pane pane = new Pane();
        Pane pane2 = new Pane();

        MainWindowController mwc;
        AddSaleController asc;

        public Cell(MainWindowController mwc, AddSaleController asc){
            super();

            this.mwc = mwc;
            this.asc = asc;

            hbox.getChildren().addAll(label, pane2, label2, pane, btn);
            HBox.setHgrow(pane, Priority.ALWAYS);
            HBox.setHgrow(pane2, Priority.ALWAYS);

            btn.setOnAction(e -> {
                getListView().getItems().remove(getItem());

                double totalBill = 0;

                this.asc.productsC.clear();

                for (Map.Entry<String, OrderList> value : asc.products.entrySet()) {
                    totalBill += value.getValue().getPrice();
                    this.asc.productsC.add(value.getKey());
                }

                String text = this.asc.productsC.get(getIndex());

                asc.products.remove(text);
                asc.sales_cb_productName.getItems().remove(text);
                asc.sales_cb_productName.getItems().add(text);
                asc.sales_cb_productName.getSelectionModel().select(null);


                asc.sales_lbl_totalPrice.setText("Total Price: ₱0;");
                asc.sales_lbl_message.setText("Please put valid or select product");

                asc.productsC.remove(getIndex());

                asc.sales_tf_totalBill.setText("Total Bill: ₱" + totalBill);

                asc.totalBill = totalBill;
            });
        }

        @Override
        public void updateItem(String name, boolean empty){
            super.updateItem(name, empty);
            setText(null);
            setGraphic(null);

            if (name != null && !empty){
                String [] a = name.split("'-");
                label.setText(a[0]);
                label2.setText(a[1]);

                if (!this.asc.productsC.contains(a[0])){
                    this.asc.productsC.add(a[0]);
                }

                setGraphic(hbox);
            }
        }
    }

    public AddSaleController() throws SQLException {
    }

    public void setData(MainWindowController controller_mainWindow, Stage stage_this){
        this.controller_mainWindow = controller_mainWindow;
        this.stage_this = stage_this;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.sales_cb_productName.getItems().addAll(getActiveProducts());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.sales_cb_productName.requestFocus();

        this.sales_cb_productName.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                productNameChanged(newValue);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        this.sales_tf_quantity.textProperty().addListener((observable, oldValue, anewValue) -> {
            try {
                this.quantityChanged(anewValue);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        TextFields.bindAutoCompletion(this.sales_cb_productName.getEditor(), this.sales_cb_productName.getItems());

        this.sales_tf_price.textProperty().addListener((observable, oldValue, newValue) -> this.priceChanged(newValue));

        this.sales_tf_cash.textProperty().addListener((observable, oldValue, newValue) -> this.cashChanged(newValue));

        sales_lv_cart.setCellFactory(param -> new Cell(this.controller_mainWindow, this));
    }

    public void cashChanged(String newValue){
        this.sales_tf_cash.setText(newValue);

        try{
            Double.parseDouble(newValue);
        }catch (Exception e){
            this.sales_tf_change.setText("Change: ₱0");
            return;
        }

        if (Double.parseDouble(newValue) < this.totalBill){
            this.sales_tf_change.setText("Change: ₱0");
        }else {
            this.change = (Double.parseDouble(newValue) - this.totalBill);
            this.sales_tf_change.setText("Change: ₱" + this.change);
        }

        this.cash = Double.parseDouble(newValue);
    }

    public void checkOut() throws SQLException, IOException, DocumentException {
        if (this.products.isEmpty()){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Cart is empty. Please select a product first.",
                    "Ok", "close");
            return;
        }

        String cash = this.sales_tf_cash.getText();

        try{
            Double.parseDouble(cash);
        }catch (Exception e){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Cash should only consist of digits.",
                    "Ok", "close");

            return;
        }

        if (Double.parseDouble(cash) < this.totalBill){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Insufficient cash amount.",
                    "Ok", "close");

            return;
        }

        if (!this.products.isEmpty()){

            this.resultSet = this.statement.executeQuery("SELECT * FROM sales ORDER BY ReceiptID DESC;");

            if (this.resultSet.next()){
                double receipt = this.resultSet.getDouble("ReceiptID") + 1;
                this.receiptID = String.valueOf(receipt);
            }else {
                this.receiptID = "1";
            }

            for (OrderList value : this.products.values()) {
                String userID = this.controller_mainWindow.account_lbl_userID.getText().substring(2);
                String productName = value.getProductName();
                String quantity = String.valueOf(value.getQuantity());
                String totalPrice = String.valueOf(value.getPrice());

                this.resultSet = this.statement.executeQuery(String.format("SELECT * FROM inventory WHERE ExpenseTitle = '%s'", productName));
                this.resultSet.next();
                String expenseNumber = this.resultSet.getString("ExpenseNumber");

                this.resultSet = this.statement.executeQuery(String.format("SELECT * from expenses WHERE ExpenseNumber = '%s'", expenseNumber));
                this.resultSet.next();
                String measurement = this.resultSet.getString("Measurement");
                String price = String.valueOf(value.getPrice());

                if (measurement.equals("by Item")){
                    price = String.valueOf(value.getPrice() / value.getQuantity());
                }

                this.statement.execute(String.format("INSERT INTO `sales` (`SaleNumber`, `UserID`, `Product`, `Quantity`, `Price`, `TotalPrice`, `TotalBill`, `Cash`, " +
                        "`ChangeAmount`, `ReceiptID`, `SaleDate`) " +  "VALUES (NULL, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', current_timestamp());", userID,
                        productName, quantity, price, totalPrice, this.totalBill, this.cash, this.change, receiptID));

                this.resultSet = this.statement.executeQuery(String.format("SELECT * FROM inventory WHERE ExpenseTitle = '%s' AND Status = 'Active' ORDER BY ExpirationDate DESC", productName));

                LinkedList<String> pds = new LinkedList<>();
                LinkedList<Integer> stocks = new LinkedList<>();

                while (this.resultSet.next()){
                    String productID = this.resultSet.getString("ProductID");
                    int stock = this.resultSet.getInt("Stock");

                    pds.add(productID);
                    stocks.add(stock);
                }

                int quantity1 = Integer.parseInt(quantity);

                for (int i = 0; i < pds.size(); i++){
                    if (quantity1 >= stocks.get(i)){
                        this.statement.execute(String.format("UPDATE inventory SET Stock = '0' WHERE ProductId = '%s'", pds.get(i)));
                        this.statement.execute(String.format("UPDATE inventory SET Status = 'Inactive (Out of Stock)' WHERE ProductId = '%s'", pds.get(i)));
                        quantity1 = quantity1 - stocks.get(i);
                    }else if (quantity1 != 0){
                        this.statement.execute(String.format("UPDATE inventory SET Stock = Stock - %d WHERE ProductId = '%s'", quantity1, pds.get(i)));
                        quantity1 = 0;
                    }

                    if (checkCriticalLevel(pds.get(i))){
                        this.resultSet = this.statement.executeQuery(String.format("SELECT * FROM inventory WHERE ProductID = '%s'", pds.get(i)));
                        this.resultSet.next();
                        String expenseTitle = this.resultSet.getString("ExpenseTitle");

                        PopUpApplication popup = new PopUpApplication();
                        popup.start(new Stage());
                        popup.setData(this.stage_this, "Warning", expenseTitle + " is under critical level. Restock as soon as possible",
                                "Ok", "close");
                    }
                }

                this.resultSet = this.statement.executeQuery(String.format("SELECT SaleDate FROM sales WHERE Product = '%s'", productName));
                this.resultSet.next();
            }

            createReceipt();

            this.controller_mainWindow.loadInventoryTable();
            this.controller_mainWindow.loadSalesTable();
            this.controller_mainWindow.dataChanged();
            this.products = new HashMap<>();
            this.stage_this.close();
        }
    }

    public boolean checkCriticalLevel(String productID) throws SQLException {
        this.resultSet = this.statement.executeQuery(String.format("SELECT SUM(Stock) AS TotalStock, CriticalLevel FROM inventory WHERE ProductID = '%s' AND Status = 'Active'", productID));
        this.resultSet.next();

        return this.resultSet.getInt("TotalStock") <= this.resultSet.getInt("CriticalLevel");
    }

    public void createReceipt() throws IOException, DocumentException, SQLException {
        this.resultSet = this.statement.executeQuery("SELECT * FROM sales ORDER BY ReceiptID DESC;");
        this.resultSet.next();

        String receiptID = this.resultSet.getString("ReceiptID");

        this.resultSet = this.statement.executeQuery(String.format("SELECT * FROM sales WHERE ReceiptID = '%s'", receiptID));
        this.resultSet.next();

        String saleDate = this.resultSet.getString("SaleDate");
        String userID = this.resultSet.getString("UserID");

        this.resultSet = this.statement.executeQuery(String.format("SELECT * FROM user WHERE UserID = '%s'", userID));
        this.resultSet.next();

        String name = this.resultSet.getString("firstName") + " " + this.resultSet.getString("lastName");

        String dest = "C:\\PDF\\receipt.pdf";

        Document dc = new Document();
        PdfWriter.getInstance(dc, new FileOutputStream(dest));
        dc.open();

        Paragraph header = new Paragraph(new Phrase(10f, this.leftPad("MHAIKA'S STORE", 25),
                FontFactory.getFont(FontFactory.COURIER, 14.0f)));
        dc.add(header);

        dc.add(new Paragraph("\n"));

        Paragraph address = new Paragraph(new Phrase(10f, this.leftPad("Address: Mabolo St. Monark Tuyo, Balanga City", 0),
                FontFactory.getFont(FontFactory.COURIER, 11.0f)));
        dc.add(address);

        Paragraph tel = new Paragraph(new Phrase(10f, this.leftPad("Tel:     (+63)76-176-1590", 0),
                FontFactory.getFont(FontFactory.COURIER, 11.0f)));
        dc.add(tel);

        dc.add(new Paragraph(". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ."));

        dc.add(new Paragraph("\n"));

        Paragraph receiptIDP = new Paragraph(new Phrase(10f, this.rightPad("Receipt ID:", 12) + "   " + this.rightPad(receiptID, 3),
                FontFactory.getFont(FontFactory.COURIER, 11.0f)));
        dc.add(receiptIDP);

        Paragraph vendor = new Paragraph(new Phrase(10f, this.rightPad("Vendor Name:", 12) + "   " + this.rightPad(name, 30),
                FontFactory.getFont(FontFactory.COURIER, 11.0f)));
        dc.add(vendor);

        Paragraph sld = new Paragraph(new Phrase(10f, this.rightPad("Sale Date:", 12) + "   " + this.rightPad(saleDate.substring(0, 10), 22) + this.rightPad(saleDate.substring(11), 10),
                FontFactory.getFont(FontFactory.COURIER, 11.0f)));
        dc.add(sld);

        dc.add(new Paragraph(". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ."));

        dc.add(new Paragraph("\n"));

        this.resultSet = this.statement.executeQuery(String.format("SELECT * FROM sales WHERE ReceiptID = '%s'", receiptID));

        while (this.resultSet.next()){
            String productD = this.resultSet.getString("Product");
            String asdf = productD.substring(productD.indexOf(",") + 2);

            Paragraph prod = new Paragraph(new Phrase(10f, this.rightPad(asdf,
                    25) + this.rightPad("x" + (int)this.resultSet.getDouble("Quantity"), 9) +
                    this.leftPad("P" + this.resultSet.getString("TotalPrice"), 11),
                    FontFactory.getFont(FontFactory.COURIER, 11.0f)));
            dc.add(prod);
        }

        dc.add(new Paragraph(". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ."));

        dc.add(new Paragraph("\n"));

        Paragraph bill = new Paragraph(new Phrase(10f,  this.rightPad("Total", 25) + this.leftPad("P" + this.totalBill, 20),
                FontFactory.getFont(FontFactory.COURIER, 11.0f)));
        dc.add(bill);

        Paragraph cash = new Paragraph(new Phrase(10f,  this.rightPad("Cash", 25) + this.leftPad("P" + this.cash, 20),
                FontFactory.getFont(FontFactory.COURIER, 11.0f)));
        dc.add(cash);

        Paragraph changeP = new Paragraph(new Phrase(10f,  this.rightPad("Change", 25) + this.leftPad("P" + this.change, 20),
                FontFactory.getFont(FontFactory.COURIER, 11.0f)));
        dc.add(changeP);

        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + dest);
        dc.close();
    }

    private String leftPad(String text, int length) {
        StringBuilder textBuilder = new StringBuilder(text);
        for(int i = textBuilder.length(); i < length; i++){
            textBuilder.insert(0, " ");
        }
        text = textBuilder.toString();

        return text;
    }

    private String rightPad(String text, int length) {
        StringBuilder textBuilder = new StringBuilder(text);
        textBuilder.append(" ".repeat(Math.max(0, length - textBuilder.length())));
        text = textBuilder.toString();

        return text;
    }

    public void addToCart() throws IOException, SQLException {
        String product = this.sales_cb_productName.getSelectionModel().getSelectedItem();

        if (product == null || product.equals("")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Please select a product",
                    "Ok", "close");
        }else if (!currentError.equals("")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", this.currentError,
                    "Ok", "close");
        }else {
            String productName = this.sales_cb_productName.getSelectionModel().getSelectedItem();
            int quantity = Integer.parseInt(this.sales_tf_quantity.getText());
            double currentPrice = Double.parseDouble(this.sales_lbl_totalPrice.getText().substring(this.sales_lbl_totalPrice.getText().lastIndexOf("₱") + 1));

            this.resultSet = this.statement.executeQuery(String.format("SELECT * FROM inventory WHERE ExpenseTitle = '%s' AND Status = 'Active'", productName));
            int stock = 0;
            while (this.resultSet.next()){
                stock += this.resultSet.getInt("Stock");
            }


            OrderList entry = new OrderList(productName, quantity, currentPrice);

            if (this.products.containsKey(productName)){
                OrderList existingEntry = this.products.get(productName);
                existingEntry.setQuantity(entry.getQuantity() + existingEntry.getQuantity());

                if (existingEntry.getQuantity() == stock){
                    this.sales_cb_productName.getItems().remove(productName);
                }

                existingEntry.setPrice(existingEntry.getPrice() + currentPrice);
            }else {
                this.products.put(productName, entry);

                if (quantity == stock){
                    this.sales_cb_productName.getItems().remove(productName);
                }
            }

            ObservableList<String> orders = FXCollections.observableArrayList();

            double totalBill = 0;

            for (OrderList value : this.products.values()) {
                orders.add(value.getListViewRegex());
                totalBill += value.getPrice();
            }

            this.sales_tf_totalBill.setText("Total Bill: ₱" + totalBill);
            this.totalBill = totalBill;

            this.sales_cb_productName.getSelectionModel().select(null);
            this.sales_lv_cart.getItems().clear();
            this.sales_lv_cart.setItems(orders);
            this.sales_lbl_totalPrice.setText("Total Price: ₱0");
        }
    }

    public void priceChanged(String newValue){
        if (this.sales_tf_price.isDisabled()){
            return;
        }

        try{
            Double.parseDouble(newValue);
        }catch (Exception e){
            this.currentError = "Only digits are allowed for price";
            this.sales_lbl_selectProductMessage.setText("Only digits are allowed for price");
            this.sales_lbl_totalPrice.setText("Total Price: ₱0");
            return;
        }

        if (Double.parseDouble(newValue) < 1){
            this.currentError = "Price cannot be lower than 1";
            this.sales_lbl_selectProductMessage.setText("Price cannot be lower than 1");
            this.sales_lbl_totalPrice.setText("Total Price: ₱0");
            return;
        }

        String totalPrice = "Total Price: ₱" + newValue;
        this.sales_lbl_totalPrice.setText(totalPrice);
        this.currentError = "";
        this.sales_lbl_selectProductMessage.setText("Click the right arrow button to add on the cart");
    }

    public void quantityChanged(String newValue) throws SQLException {
        if (this.sales_tf_quantity.isDisabled()){
            return;
        }

        String productName = this.sales_cb_productName.getSelectionModel().getSelectedItem();

        this.resultSet = this.statement.executeQuery(String.format("SELECT * FROM inventory WHERE ExpenseTitle = '%s' AND Status = 'Active'", productName));
        int stock = 0;
        String expenseNumber = "";

        while (this.resultSet.next()){
            stock += this.resultSet.getInt("Stock");
            expenseNumber = this.resultSet.getString("ExpenseNumber");
        }

        this.resultSet = this.statement.executeQuery(String.format("SELECT * FROM expenses WHERE ExpenseNumber = '%s'", expenseNumber));
        this.resultSet.next();
        String measurement = this.resultSet.getString("Measurement");

        if (this.products.get(productName) != null){
            stock -= this.products.get(productName).getQuantity();
        }

        if (measurement.equals("by Amount")){
            this.sales_tf_price.setText("");
        }

        String message = "Quantity / Amount is invalid. Please enter a digit up to " + stock;

        try{
            Integer.parseInt(newValue);
        }catch (Exception e){
            this.currentError = message;
            this.sales_lbl_selectProductMessage.setText(message);
            this.sales_tf_price.setDisable(true);
            this.sales_lbl_totalPrice.setText("Total Price: ₱0");
            return;
        }

        if ( Integer.parseInt(newValue) <= 0 || newValue.contains(".") || Integer.parseInt(newValue) > stock){
            this.currentError = message;
            this.sales_lbl_selectProductMessage.setText(message);
            this.sales_lbl_totalPrice.setText("Total Price: ₱0");
            this.sales_tf_price.setDisable(true);
            return;
        }

        if (measurement.equals("by Item")){
            double total = Double.parseDouble(this.sales_tf_price.getText()) * Integer.parseInt(newValue);
            String totalPrice = "Total Price: ₱" + total;
            this.sales_lbl_totalPrice.setText(totalPrice);
            this.currentError = "";
            this.sales_lbl_selectProductMessage.setText("Click the right arrow button to add on the cart");
        }else {
            this.sales_tf_price.setDisable(false);
            this.currentError = "Price is needed for this type of product";
            this.sales_lbl_selectProductMessage.setText("Price is needed for this type of product");
        }
    }

    public void productNameChanged(String newValue) throws SQLException {
        if (newValue.equals("")){
            this.sales_cb_productName.getSelectionModel().select(null);
        }else {
            this.sales_cb_productName.getSelectionModel().select(newValue);
        }

        this.sales_tf_price.setDisable(true);
        this.sales_tf_quantity.setDisable(true);
        this.sales_tf_price.clear();
        this.sales_tf_quantity.clear();

        this.resultSet = this.statement.executeQuery(String.format("SELECT * FROM inventory WHERE ExpenseTitle = '%s' AND Status = 'Active'", newValue));

        if (!this.resultSet.next()){
            String message = "Please put valid or select a product";
            this.currentError = message;
            this.sales_lbl_selectProductMessage.setText(message);
            this.sales_tf_quantity.setPromptText("Quantity / Amount");
            this.sales_tf_quantity.setDisable(true);
            this.sales_tf_price.setText("");
        }else {
            this.sales_tf_quantity.setDisable(false);

            this.currentError = "Please enter a quantity / amount";
            this.sales_lbl_selectProductMessage.setText("Please enter a quantity / amount");

            this.resultSet = this.statement.executeQuery(String.format("SELECT * FROM inventory WHERE ExpenseTitle = '%s' AND Status = 'Active'", newValue));
            int stock = 0;
            String expenseNumber = "";
            Double price = (double) 0;

            while (this.resultSet.next()){
                stock += this.resultSet.getInt("Stock");
                expenseNumber = this.resultSet.getString("ExpenseNumber");
                price = this.resultSet.getDouble("Price");
            }

            this.resultSet = this.statement.executeQuery(String.format("SELECT Measurement FROM expenses WHERE ExpenseNumber = '%s'", expenseNumber));
            this.resultSet.next();

            String measurement = this.resultSet.getString("Measurement");

            if (this.products.get(newValue) != null){
                stock -= this.products.get(newValue).getQuantity();
            }

            String promptText;

            if (measurement.equals("by Item")){
                promptText = "Quantity " + "(Stock: " + stock + ")";
                this.sales_tf_quantity.setPromptText(promptText);
                this.sales_tf_price.setText(String.valueOf(price));
            }else {
                promptText = "Amount: " + "(Stock: " + stock + ")";
                this.sales_tf_quantity.setPromptText(promptText);
                this.sales_tf_price.setText("");
            }
        }
    }

    public ObservableList<String> getActiveProducts() throws SQLException {
        this.resultSet = this.statement.executeQuery("SELECT * FROM inventory WHERE Status = 'Active' GROUP BY ExpenseTitle");

        ObservableList<String> products = FXCollections.observableArrayList();

        while(this.resultSet.next()){
            String productName = this.resultSet.getString("ExpenseTitle");
            products.add(productName);
        }

        return products;
    }

    public void close(){
        this.stage_this.close();
    }
}
