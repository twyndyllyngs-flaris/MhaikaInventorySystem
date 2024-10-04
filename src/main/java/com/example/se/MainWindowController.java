package com.example.se;

import com.itextpdf.text.*;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTreeTableCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.effect.Effect;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Polyline;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@SuppressWarnings("rawtypes")
public class MainWindowController implements Initializable {
    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/se_system", "root", "");
    Statement statement = connection.createStatement();
    ResultSet rs;

    @FXML
    Label pane_top, lbl_roleIcon, lbl_name, lbl_windowTitle, lbl_expenses, lbl_inventory, lbl_charts, lbl_transactions, lbl_account, current, expenses_message, inventory_message,
    lbl_vendors, lbl_accounts, vendors_message, accounts_message, account_lbl_name, account_lbl_role, account_lbl_userID, account_lbl_age, account_lbl_sex,
            account_lbl_dateCreated, account_lbl_birthDatePatch, sales_message, charts_lbl_chartTitle, charts_message, charts_lbl_compare;

    @FXML
    Button pb_logout,  pb_close, pb_minimize, expenses_button_clearEndingDate,expenses_button_clearSearch, expenses_button_message,expenses_button_clearStartingDate,
            inventory_button_message, vendors_button_message, accounts_button_message, account_button_accountDetails, account_button_contactAddress,
    account_button_username, account_button_password, account_button_firstName, account_button_lastName, account_button_birthDate, account_button_phone,
            account_button_telephone, account_button_email, account_button_province, account_button_city, account_button_barangay, account_button_street,
            account_button_postalCode, sales_button_message, sales_button_add, sales_button_showReceipt, sales_button_toInventory, sales_button_toVendors,
    sales_button_save, sales_button_clearSearch, sales_button_clearEndingDate, sales_button_clearStartingDate, charts_button_clearItems, charts_button_add,
            charts_button_message;

    @FXML
    ListView<String> charts_lv_items;

    @FXML
    AnchorPane ap_inventory, ap_expenses, ap_account, ap_vendors, ap_accounts, ap_sales, ap_charts;

    @FXML
    TreeTableView<ExpensesTable> expenses_ttv_main;
    @FXML
    TreeTableView<InventoryTable> inventory_ttv_main;
    @FXML
    TreeTableView<VendorsTable> vendors_ttv_main;
    @FXML
    TreeTableView<AccountsTable> accounts_ttv_main;
    @FXML
    TreeTableView<SalesTable> sales_ttv_main;

    @FXML
    TreeTableColumn<ExpensesTable, String> expenses_col_expenseNumber, expenses_col_type, expenses_col_expenseTitle, expenses_col_companyName,
            expenses_col_costPerItem, expenses_col_quantity, expenses_col_totalItemCost, expenses_col_description, expenses_col_deliverTo, expenses_col_deliveredFrom,
            expenses_col_shipmentPreference, expenses_col_trackingNumber, expenses_col_deliveryFees, expenses_col_measurement,
            expenses_col_paymentTerms, expenses_col_totalExpenseCost, expenses_col_dateAdded, expenses_col_status;
    @FXML
    TreeTableColumn<InventoryTable, String> inventory_col_productID, inventory_col_expenseNumber, inventory_col_expenseTitle, inventory_col_productType,
            inventory_col_productName, inventory_col_variant, inventory_col_stock, inventory_col_price, inventory_col_description, inventory_col_criticalLevel,
            inventory_col_dateAdded, inventory_col_status;
    @FXML
    TreeTableColumn<VendorsTable, String> vendors_col_companyName, vendors_col_firstName, vendors_col_lastName, vendors_col_displayName, vendors_col_email,
            vendors_col_workPhone, vendors_col_phoneNumber, vendors_col_website;

    @FXML
    TreeTableColumn<AccountsTable, String> accounts_col_userID, accounts_col_role, accounts_col_username, accounts_col_password, accounts_col_firstName, accounts_col_lastName,
            accounts_col_phoneNumber, accounts_col_telephone, accounts_col_email, accounts_col_dateCreated, accounts_col_status;

    @FXML
    TreeTableColumn<SalesTable, String> sales_col_saleNumber, sales_col_userID, sales_col_productName, sales_col_quantity, sales_col_price, sales_col_totalPrice,
            sales_col_receiptID, sales_col_saleDate, sales_col_totalBill, sales_col_changeAmount, sales_col_cash;

    @FXML
    TreeTableColumn<ExpensesTable, Date> expenses_col_dateDelivered, expenses_col_expectedDeliveryDate, expenses_col_paymentDate;

    @FXML
    TreeTableColumn<InventoryTable, Date> inventory_col_expirationDate;

    TreeItem<ExpensesTable> root = new TreeItem<>(new ExpensesTable("expenseNumber", "type", "companyName", "expenseTitle",
            "measurement", "quantity", "costPerItem","totalItemCost", "description", " deliverTo",
            "deliveredFrom", "shipmentPreference", "trackingNumber", "deliveryFees",
            "expectedDeliveryDate", "dateDelivered", "paymentDate",  "paymentTerms",
            "totalExpenseCost"," dateAdded", "status"));

    TreeItem<InventoryTable> inventoryRoot = new TreeItem<>(new InventoryTable("productID", "expenseNumber", "expenseTitle",
            "productType", "productName", "variant", "stock", "price", "description", "expirationDate", "criticalLevel",
            "dateAdded", "status"));

    TreeItem<VendorsTable> vendorsRoot = new TreeItem<>(new VendorsTable("companyName", "firstName", "lastName",
            "displayName", "email", "workPhone", "phoneNumber", "website"));

    TreeItem<AccountsTable> accountsRoot = new TreeItem<>(new AccountsTable("role", "userID", "username", "password", "firstname",
            "lastname", "phoneNumber", "telephone", "email", "dateCreated", "status"));

    TreeItem<SalesTable> salesRoot = new TreeItem<>(new SalesTable("saleNumber", "sellerID", "product", "quantity", "price",
            "totalPrice", "totalBill", "cash", "changeAmount", "receiptID", "saleDate"));

    Callback<TreeTableColumn<ExpensesTable, Date>, TreeTableCell<ExpensesTable, Date>> dateCellFactory
            = (TreeTableColumn<ExpensesTable, Date> param) -> new DateEditingCell();

    Callback<TreeTableColumn<InventoryTable, Date>, TreeTableCell<InventoryTable, Date>> inventory_dateCellFactory
            = (TreeTableColumn<InventoryTable, Date> param) -> new InventoryDateEditingCell();

    @FXML
    CheckBox expenses_cb_includeCancelledRecords, expenses_cb_searchExactMatch, inventory_cb_includeCancelledRecords, inventory_cb_searchExactMatch,
            vendors_cb_searchExactMatch, accounts_cb_searchExactMatch, accounts_cb_includeDisabledAccounts, sales_cb_searchExactMatch,  charts_ckb_except;

    @FXML
    ComboBox<String> expenses_cb_column, expenses_cb_dateColumn, inventory_cb_column, vendors_cb_column, inventory_cb_dateColumn, accounts_cb_column, sales_cb_column,
            charts_cb_chartType, charts_cb_data, charts_cb_item, charts_cb_time, charts_cb_date;

    @FXML
    TextField expenses_tf_search, inventory_tf_search, vendors_tf_search, accounts_tf_search, account_tf_username, account_tf_password, account_tf_firstName,
            account_tf_lastName, account_tf_phone, account_tf_telephone, account_tf_email, account_tf_province, account_tf_city, account_tf_barangay,
            account_tf_street, account_tf_postalCode, sales_tf_search;
    @FXML
    DatePicker expenses_dp_startingDate, expenses_dp_endingDate, inventory_dp_startingDate, inventory_dp_endingDate, accounts_dp_startingDate, accounts_dp_endingDate,
    account_dp_birthDate, sales_dp_endingDate, sales_dp_startingDate;

    @FXML
    Polyline account_pl_accountUser, account_pl_contactAddress, account_pl_name;

    @FXML
    AreaChart charts_areaChart;

    @FXML
    CategoryAxis charts_areaChartCategoryAxis;
    @FXML
    NumberAxis charts_areaChart_numberAxis;

    Effect shadow;

    String currentUsername, currentPassword, currentTitle, expenses_selectedExpenseNumber, expenses_oldStatus, expenses_newStatus, vendors_selected_companyName,
            expenses_newPaymentDate, account_usernameMessage, account_passwordMessage, account_firstNameMessage, account_lastNameMessage, account_birthDateMessage,
    account_phoneMessage, account_telephoneMessage, account_emailMessage, account_provinceMessage, account_cityMessage, account_barangayMessage, account_streetMessage,
            account_postalCodeMessage;
    
    String currentExpensesTableQuery = "SELECT * FROM expenses WHERE Status NOT LIKE '%%Cancelled%%'";
    String currentInventoryTableQuery = "SELECT * FROM inventory WHERE Status NOT LIKE '%%Inactive%%'";
    String currentVendorsTableQuery = "SELECT * FROM vendors";
    String currentAccountsTableQuery = """
            SELECT *
            FROM accounts
            INNER JOIN user
            ON accounts.username=user.username
            WHERE accounts.Status = 'Active'""";
    String currentSalesTableQuery = "SELECT * FROM sales";

    String role;

    boolean account_accountUserStart = false, account_contactAddressStart = false;

    Boolean account_usernameValid = null, account_passwordValid = null, account_firstNameValid = null, account_lastNameValid = null, account_birthDateValid = null,
            account_phoneValid = null, account_telephoneValid = null, account_emailValid = null, account_provinceValid = null, account_cityValid = null,
            account_barangayValid = null, account_streetValid = null, account_postalCodeValid = null;

    Stage stage_this;

    Date expenses_oldDateDelivered, expenses_newDateDelivered;

    LinkedList<String> expenses_messages = new LinkedList<>();
    int expenses_currentMessageIndex = 1;

    LinkedList<String> inventory_messages = new LinkedList<>();
    int inventory_currentMessageIndex = 1;

    LinkedList<String> vendors_messages = new LinkedList<>();
    int vendors_currentMessageIndex = 1;

    LinkedList<String> accounts_messages = new LinkedList<>();
    int accounts_currentMessageIndex = 1;

    LinkedList<String> sales_messages = new LinkedList<>();
    int sales_currentMessageIndex = 1;

    LinkedList<String> charts_messages = new LinkedList<>();
    int charts_currentMessageIndex = 1;

    LinkedList<String> account_ll_accountUser = new LinkedList<>();

    LinkedList<String> account_ll_contactAddress = new LinkedList<>();

    ExpensesTable currentRow;

    TreeMap<String, String> items = new TreeMap<>();
    LinkedList<String> itemsC = new LinkedList<>();

    public MainWindowController() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pane_top.toFront();
        lbl_roleIcon.toFront();
        lbl_name.toFront();
        lbl_windowTitle.toFront();
        pb_minimize.toFront();
        pb_logout.toFront();
        pb_close.toFront();
        shadow = lbl_expenses.getEffect();
        current = lbl_expenses;
        lbl_expenses.toFront();
        lbl_inventory.toFront();
        lbl_transactions.toFront();
        lbl_account.toFront();
        lbl_vendors.toFront();
        lbl_accounts.toFront();
        lbl_charts.toFront();

        // expenses
        ObservableList<TreeTableColumn<ExpensesTable, ?>> columns = this.expenses_ttv_main.getColumns();

        for (TreeTableColumn<ExpensesTable, ?> column : columns) {
            if (column.getText().equals("Date Added") || column.getText().equals("Date Delivered") ||
                    column.getText().equals("Expected Delivery Date") || column.getText().equals("Date Delivered") || column.getText().equals("Payment Date")) {
                expenses_cb_dateColumn.getItems().addAll(column.getText());
            } else {
                expenses_cb_column.getItems().addAll(column.getText());
            }
        }

        expenses_cb_column.getSelectionModel().select("Expense Title");
        expenses_cb_dateColumn.getSelectionModel().select("Date Added");

        this.expenses_tf_search.textProperty().addListener((observable, oldValue, newValue) -> this.search());

        this.expenses_messages.add("To search for records with no value, type \"null\" on the search field");
        this.expenses_messages.add("Date-delivered field will automatically set to current date when the edit comes directly from status field");

        this.loadExpensesTable();

        // inventory
        ObservableList<TreeTableColumn<InventoryTable, ?>> inventoryColumns = this.inventory_ttv_main.getColumns();

        for (TreeTableColumn<InventoryTable, ?> column : inventoryColumns) {
            if (column.getText().equals("Date Added") || column.getText().equals("Expiration Date")){
                inventory_cb_dateColumn.getItems().addAll(column.getText());
            }else {
                inventory_cb_column.getItems().addAll(column.getText());
            }
        }

        inventory_cb_column.getSelectionModel().select("Product Name");
        inventory_cb_dateColumn.getSelectionModel().select("Date Added");

        this.inventory_tf_search.textProperty().addListener((observable, oldValue, newValue) -> this.searchInventory());

        this.inventory_messages.add("Date filters are for date-added field");
        this.inventory_messages.add("To search for records with no value, type \"null\" on the search field");

        //this.loadInventoryTable();

        // vendors
        ObservableList<TreeTableColumn<VendorsTable, ?>> vendorsColumns = this.vendors_ttv_main.getColumns();

        for (TreeTableColumn<VendorsTable, ?> vendorColumns : vendorsColumns) {
            vendors_cb_column.getItems().addAll(vendorColumns.getText());
        }

        vendors_cb_column.getSelectionModel().select("Company Name");

        this.vendors_tf_search.textProperty().addListener((observable, oldValue, newValue) -> this.searchVendors());

        this.vendors_messages.add("To search for records with no value, type \"null\" on the search field");
        this.vendors_messages.add("Supplier records comes with expenses and inventory records");

        this.loadVendorsTable();

        // accounts
        ObservableList<TreeTableColumn<AccountsTable, ?>> accountsColumns = this.accounts_ttv_main.getColumns();

        for (TreeTableColumn<AccountsTable, ?> accountColumns : accountsColumns) {
            if (accountColumns.getText().equals("Date Created"))
                continue;

            accounts_cb_column.getItems().addAll(accountColumns.getText());
        }

        accounts_cb_column.getSelectionModel().select("Username");

        this.accounts_tf_search.textProperty().addListener((observable, oldValue, newValue) -> this.searchAccounts());

        this.accounts_messages.add("Date filters are solely for \"Date Created\" field");
        this.accounts_messages.add("To search for records with no value, type \"null\" on the search field");

        this.loadAccountsTable();

        // account
        this.account_tf_username.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                this.account_usernameChange();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        this.account_tf_password.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                this.account_passwordChange();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        this.account_tf_firstName.textProperty().addListener((observable, oldValue, newValue) -> this.account_firstNameChange());
        this.account_tf_lastName.textProperty().addListener((observable, oldValue, newValue) -> this.account_lastNameChange());
        this.account_tf_phone.textProperty().addListener((observable, oldValue, newValue) -> this.account_phoneChange());
        this.account_tf_telephone.textProperty().addListener((observable, oldValue, newValue) -> this.account_telephoneChange());
        this.account_tf_email.textProperty().addListener((observable, oldValue, newValue) -> this.account_emailChange());
        this.account_tf_province.textProperty().addListener((observable, oldValue, newValue) -> this.account_provinceChange());
        this.account_tf_city.textProperty().addListener((observable, oldValue, newValue) -> this.account_cityChange());
        this.account_tf_barangay.textProperty().addListener((observable, oldValue, newValue) -> this.account_barangayChange());
        this.account_tf_street.textProperty().addListener((observable, oldValue, newValue) -> this.account_streetChange());
        this.account_tf_postalCode.textProperty().addListener((observable, oldValue, newValue) -> this.account_postalCodeChange());

        // sales
        ObservableList<TreeTableColumn<SalesTable, ?>> salesColumns = this.sales_ttv_main.getColumns();

        for (TreeTableColumn<SalesTable, ?> saleColumns : salesColumns) {
            if (saleColumns.getText().equals("Sale Date"))
                continue;
            
            if (saleColumns.getText().equals("Quantity / Amount"))
                sales_cb_column.getItems().addAll("Quantity");
            else 
                sales_cb_column.getItems().addAll(saleColumns.getText());
        }

        sales_cb_column.getSelectionModel().select("Product");

        this.sales_tf_search.textProperty().addListener((observable, oldValue, newValue) -> this.searchSales());

        this.sales_messages.add("Date filters are solely for \"Sale Date\" field");
        this.sales_messages.add("To search for records with no value, type \"null\" on the search field");

        this.loadSalesTable();

        //charts
        //restrictDatePicker(charts_dp_startDate, LocalDate.of(2018, Month.JANUARY, 1), LocalDate.of(2019, Month.JANUARY, 1));
        //this.charts_dp_startDate.setValue(LocalDate.of(2018, Month.JANUARY, 1));
        this.charts_cb_data.getItems().addAll("Income", "Loss", "Sales", "Expenses");
        this.charts_cb_data.getSelectionModel().select("Sales");
        this.charts_cb_chartType.getItems().addAll("Area Chart", "Bar Chart", "Pie Chart");
        this.charts_cb_chartType.getSelectionModel().select("Area Chart");

        charts_lv_items.setCellFactory(param -> new Cell(this));

        this.charts_messages.add("Check \"Include all except\" to select everything in the item dropdown");
        this.charts_messages.add("Add item in the 'COMPARE ITEMS' list to generate a chart based on given items");

        try {
            this.dataChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // update critical level
        LocalDate currentdate = LocalDate.now();
        int currentDay = currentdate.getDayOfMonth();

        try {
            if (currentDay != 1){
                LinkedList<String> titles = new LinkedList<>();

                this.rs = this.statement.executeQuery("SELECT * FROM inventory WHERE Status NOT LIKE '%To Be Delivered%' AND Status NOT LIKE '%Cancelled%' GROUP By ExpenseTitle;");

                while (rs.next()){
                    titles.add(this.rs.getString("ExpenseTitle"));
                }

                for (int i = 0; i < titles.size(); i++){
                    int cl = computeCriticalLevel(titles.get(i));
                    this.statement.execute(String.format("UPDATE inventory SET CriticalLevel = %d WHERE ExpenseTitle = '%s'", cl, titles.get(i)));
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static class Cell extends ListCell<String>{
        HBox hbox = new HBox();
        Button btn = new Button("Remove");
        Label label = new Label("");
        Pane pane = new Pane();


        MainWindowController mwc;

        public Cell(MainWindowController mwc){
            super();

            this.mwc = mwc;

            hbox.getChildren().addAll(label, pane, btn);
            HBox.setHgrow(pane, Priority.ALWAYS);

            btn.setOnAction(e -> {
                getListView().getItems().remove(getItem());

                this.mwc.itemsC.clear();

                for (Map.Entry<String, String> value : mwc.items.entrySet()) {
                    this.mwc.itemsC.add(value.getKey());
                }

                String text = this.mwc.itemsC.get(getIndex());

                this.mwc.items.remove(text);
                this.mwc.charts_cb_item.getItems().add(text);
                this.mwc.itemsC.remove(getIndex());

                try {
                    this.mwc.setTimeBounds();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
        }

        @Override
        public void updateItem(String name, boolean empty){
            super.updateItem(name, empty);
            setText(null);
            setGraphic(null);

            if (name != null && !empty){
                label.setText(name);
                setGraphic(hbox);

                if (!this.mwc.itemsC.contains(name)){
                    this.mwc.itemsC.add(name);
                }
            }
        }
    }

    public void charts_message(){
        this.charts_message.setText(this.charts_messages.get(this.charts_currentMessageIndex));

        if (charts_messages.size() == this.charts_currentMessageIndex + 1)
            this.charts_currentMessageIndex = 0;
        else
            this.charts_currentMessageIndex += 1;
    }

    public void changeAge(String username) throws SQLException {
        this.rs = this.statement.executeQuery(String.format("SELECT * FROM user WHERE Username = '%s'", username));
        this.rs.next();
        Date bd = this.rs.getDate("BirthDate");
        LocalDate birhtdate = bd.toLocalDate();
        LocalDate now = LocalDate.now();
        String birthdateString = birhtdate.toString();
        int age = (int)ChronoUnit.YEARS.between(birhtdate, now);

        this.statement.execute(String.format("UPDATE user SET Age = %d WHERE Username = '%s';", age, username));
    }

    public boolean checkCriticalLevel(String expenseTitle) throws SQLException {
        String sql = String.format("SELECT SUM(Stock) AS TotalStock, CriticalLevel FROM inventory WHERE ExpenseTitle = '%s' AND Status = 'Active'", expenseTitle);

        this.rs = this.statement.executeQuery(sql);
        this.rs.next();

        int ts = this.rs.getInt("TotalStock");
        int cl = this.rs.getInt("CriticalLevel");

        return ts <= cl ;
    }

    public int computeCriticalLevel(String expenseTitle) throws SQLException {
        System.out.println(expenseTitle);
        this.rs = this.statement.executeQuery(String.format("SELECT CAST(COALESCE(SUM(Quantity) / DAY(LAST_DAY(CURRENT_DATE())), 0) AS INT) as AverageDemand " +
                "FROM sales WHERE Product = '%s' AND DATE_FORMAT(SaleDate, '%%Y-%%m') =" +
                " DATE_FORMAT(DATE_SUB(CURRENT_DATE(), INTERVAL 1 MONTH), '%%Y-%%m')", expenseTitle));
        this.rs.next();

        System.out.println(String.format("SELECT CAST(COALESCE(SUM(Quantity) / DAY(LAST_DAY(CURRENT_DATE())), 0) AS INT) as AverageDemand " +
                "FROM sales WHERE Product = '%s' AND DATE_FORMAT(SaleDate, '%%Y-%%m') =" +
                " DATE_FORMAT(DATE_SUB(CURRENT_DATE(), INTERVAL 1 MONTH), '%%Y-%%m')", expenseTitle));

        int averageDemand = this.rs.getInt("AverageDemand");
        int leadTime = 7;
        int second = averageDemand * leadTime;

        System.out.println(second);

        this.rs = this.statement.executeQuery(String.format("SELECT COALESCE(MAX(Quantity), 0) as MaxDemand " +
                "FROM sales WHERE Product = '%s' AND " +
                "DATE_FORMAT(SaleDate, '%%Y-%%m') = DATE_FORMAT(DATE_SUB(CURRENT_DATE(), INTERVAL 1 MONTH), '%%Y-%%m')", expenseTitle));
        this.rs.next();

        int maxDemand = this.rs.getInt("MaxDemand");
        int maxTime = 15;
        int first = maxDemand * maxTime;

        System.out.println(first);

        int low = first - second;

        if (second == 0){
            low = maxDemand;
        }

        return low;
    }

    public void dateChanged() throws SQLException {
        if (this.charts_cb_date.getSelectionModel().getSelectedItem() == null){
            return;
        }
        this.charts_areaChart.getData().clear();
        changeChartTitle();
        loadAreaChart();
    }

    public void except() throws SQLException {
        setTimeBounds();
    }

    public void TimeChanged() throws SQLException {
        this.charts_areaChart.getData().clear();

        String time = this.charts_cb_time.getSelectionModel().getSelectedItem();

        if (time == null || time.equals("")){
            return;
        }

        this.charts_cb_date.getItems().clear();
        changeChartTitle();

        String data = this.charts_cb_data.getSelectionModel().getSelectedItem();
        String dataSQL;
        String fieldName;
        String dateName;

        if (data.equals("Expenses")){
            dataSQL = "SELECT * FROM expenses WHERE Status = 'Transaction Completed'";
            fieldName = "ExpenseTitle";
            dateName = "DateAdded";
        }else if (data.equals("Sales")){
            dataSQL = "SELECT * FROM sales";
            fieldName = "Product";
            dateName = "SaleDate";
        }else {
            dataSQL = "SELECT * FROM Inventory WHERE Status NOT LIKE '%Cancelled%'";
            fieldName = "ExpenseTitle";
            dateName = "DateAdded";
        }

        if (this.items.isEmpty()){
            this.rs = this.statement.executeQuery(dataSQL + " ORDER BY " + dateName + " ASC");
        }else {
            String conjunction;

            if (!dataSQL.toString().contains("WHERE")){
                conjunction = "WHERE";
            }else{
                conjunction = "AND";
            }

            if (this.charts_ckb_except.isSelected()){
                ObservableList<String> items = this.charts_cb_item.getItems();

                for (int i = 0; i < items.size(); i++){
                    dataSQL += String.format(" %s %s = '%s'", conjunction, fieldName, items.get(i));
                    conjunction = "OR";
                }
            }else {
                for (Map.Entry mapElement : this.items.entrySet()) {
                    dataSQL += String.format(" %s %s = '%s'", conjunction, fieldName, mapElement.getValue());
                    conjunction = "OR";
                }
            }

            this.rs = this.statement.executeQuery(dataSQL + " ORDER BY " + dateName + " ASC");
        }

        TreeMap<String, String> dates = new TreeMap<>();


        if (time.equals("Yearly")){
            this.charts_cb_date.setDisable(true);
            this.charts_cb_date.getItems().clear();
            loadAreaChart();
            return;
        } else if (time.equals("Monthly")){
            while (rs.next()){
                String str = rs.getString(dateName).substring(0, 4);

                dates.put(str, str);
            }
        }else if (time.equals("Daily")){
            while (rs.next()){
                String str = rs.getString(dateName).substring(0, 7);

                dates.put(str, str);
            }
        }

        for (Map.Entry<String, String> mapElement : dates.entrySet()) {
            this.charts_cb_date.getItems().add(mapElement.getValue());
        }

        this.charts_cb_date.setDisable(false);
    }

    public void setTimeBounds() throws SQLException {
        this.charts_cb_time.getItems().clear();
        this.charts_cb_date.getItems().clear();
        this.charts_cb_date.setDisable(true);
        this.charts_areaChart.getData().clear();

        String data = this.charts_cb_data.getSelectionModel().getSelectedItem();
        String dataSQL;
        String fieldName;
        String dateName;

        if (data.equals("Expenses")){
            dataSQL = "SELECT * FROM expenses WHERE Status = 'Transaction Completed'";
            fieldName = "ExpenseTitle";
            dateName = "DateAdded";
        }else if (data.equals("Sales")){
            dataSQL = "SELECT * FROM sales";
            fieldName = "Product";
            dateName = "SaleDate";
        }else {
            dataSQL = "SELECT * FROM Inventory WHERE Status NOT LIKE '%Cancelled%'";
            fieldName = "ExpenseTitle";
            dateName = "DateAdded";
        }

        if (this.items.isEmpty()){
            this.rs = this.statement.executeQuery(dataSQL);

            HashMap<String, String> year = new HashMap<>();
            HashMap<String, String> yearMonth = new HashMap<>();

            while (rs.next()){
                String dateStr = this.rs.getString(dateName).substring(0, 4);
                year.put(dateStr, dateStr);

                dateStr = this.rs.getString(dateName).substring(0, 7);
                yearMonth.put(dateStr, dateStr);
            }

            if (year.size() > 1){
                this.charts_cb_time.getItems().add("Yearly");
            }

            if (yearMonth.size() > 1){
                this.charts_cb_time.getItems().add("Monthly");
            }

            if (year.size() >= 1 || yearMonth.size() >= 1){
                this.charts_cb_time.getItems().add("Daily");
            }
        }else {
            String conjunction;

            if (!dataSQL.toString().contains("WHERE")){
                conjunction = "WHERE";
            }else{
                conjunction = "AND";
            }

            if (this.charts_ckb_except.isSelected()){
                ObservableList<String> items = this.charts_cb_item.getItems();

                if (items.size() == 0)
                    return;

                for (int i = 0; i < items.size(); i++){
                    dataSQL += String.format(" %s %s = '%s'", conjunction, fieldName, items.get(i));
                    conjunction = "OR";
                }
            }else {
                for (Map.Entry mapElement : this.items.entrySet()) {
                    dataSQL += String.format(" %s %s = '%s'", conjunction, fieldName, mapElement.getValue());
                    conjunction = "OR";
                }
            }

            this.rs = this.statement.executeQuery(dataSQL);

            HashMap<String, String> year = new HashMap<>();
            HashMap<String, String> yearMonth = new HashMap<>();

            while (rs.next()){
                String dateStr = this.rs.getString(dateName).substring(0, 4);
                year.put(dateStr, dateStr);

                dateStr = this.rs.getString(dateName).substring(0, 7);
                yearMonth.put(dateStr, dateStr);
            }

            if (year.size() > 1){
                this.charts_cb_time.getItems().add("Yearly");
            }

            if (yearMonth.size() > 1){
                this.charts_cb_time.getItems().add("Monthly");
            }

            if (year.size() >= 1 || yearMonth.size() >= 1){
                this.charts_cb_time.getItems().add("Daily");
            }
        }
    }

    public void loadAreaChart() throws SQLException {
        String data = this.charts_cb_data.getSelectionModel().getSelectedItem();
        String time = this.charts_cb_time.getSelectionModel().getSelectedItem();
        String date = this.charts_cb_date.getSelectionModel().getSelectedItem();

        String conjunction = "";

        if (data.equals("Sales")){
            if (time.equals("Yearly")){
                if (!items.isEmpty()){
                    if (this.charts_ckb_except.isSelected()){
                        ObservableList<String> items = this.charts_cb_item.getItems();
                        LinkedList<String> years = new LinkedList<>();

                        this.rs = this.statement.executeQuery("SELECT DISTINCT year(SaleDate) AS YearDate FROM `sales` ORDER BY SaleDate ASC");

                        while (rs.next()){
                            years.add(this.rs.getString("YearDate"));
                        }

                        for (int i = 0; i < items.size(); i++){
                            XYChart.Series series = new XYChart.Series();
                            series.setName(items.get(i).trim());

                            for (int j = 0; j < years.size(); j++){
                                String sql = String.format("SELECT Product, year(SaleDate) as YearDate, SUM(Quantity) as NumberOfSales FROM sales WHERE " +
                                        "Product = '%s' AND year(SaleDate) = %s GROUP BY year(SaleDate)", items.get(i), years.get(j));

                                this.rs = this.statement.executeQuery(sql);

                                if (this.rs.next()){
                                    series.getData().add(new XYChart.Data(rs.getString("YearDate"), rs.getInt("NumberOfSales")));
                                }else {
                                    series.getData().add(new XYChart.Data(years.get(j), 0));
                                }
                            }

                            this.charts_areaChart.getData().addAll(series);
                        }
                    }else {
                        LinkedList<String> years = new LinkedList<>();

                        this.rs = this.statement.executeQuery("SELECT DISTINCT year(SaleDate) AS YearDate FROM `sales` ORDER BY SaleDate ASC");

                        while (rs.next()){
                            years.add(this.rs.getString("YearDate"));
                        }

                        for (Map.Entry<String, String> mapElement : this.items.entrySet()) {
                            XYChart.Series series = new XYChart.Series();
                            series.setName(mapElement.getValue().trim());

                            for (int j = 0; j < years.size(); j++){
                                String sql = String.format("SELECT Product, year(SaleDate) as YearDate, SUM(Quantity) as NumberOfSales FROM sales WHERE " +
                                        "Product = '%s' AND year(SaleDate) = %s GROUP BY year(SaleDate)", mapElement.getValue(), years.get(j));

                                this.rs = this.statement.executeQuery(sql);

                                if (this.rs.next()){
                                    series.getData().add(new XYChart.Data(rs.getString("YearDate"), rs.getInt("NumberOfSales")));
                                }else {
                                    series.getData().add(new XYChart.Data(years.get(j), 0));
                                }
                            }

                            this.charts_areaChart.getData().addAll(series);
                        }
                    }
                }else {
                    ObservableList<String> items = this.charts_cb_item.getItems();
                    LinkedList<String> years = new LinkedList<>();

                    this.rs = this.statement.executeQuery("SELECT DISTINCT year(SaleDate) AS YearDate FROM `sales` ORDER BY SaleDate ASC");

                    while (rs.next()){
                        years.add(this.rs.getString("YearDate"));
                    }

                    for (int i = 0; i < items.size(); i++){
                        XYChart.Series series = new XYChart.Series();
                        series.setName(items.get(i).trim());

                        for (int j = 0; j < years.size(); j++){
                            String sql = String.format("SELECT Product, year(SaleDate) as YearDate, SUM(Quantity) as NumberOfSales FROM sales WHERE " +
                                    "Product = '%s' AND year(SaleDate) = %s GROUP BY year(SaleDate)", items.get(i), years.get(j));

                            this.rs = this.statement.executeQuery(sql);

                            if (this.rs.next()){
                                series.getData().add(new XYChart.Data(rs.getString("YearDate"), rs.getInt("NumberOfSales")));
                            }else {
                                series.getData().add(new XYChart.Data(years.get(j), 0));
                            }
                        }

                        this.charts_areaChart.getData().addAll(series);
                    }
                }
            }else if (time.equals("Monthly")){
                if (!items.isEmpty()){
                    if (this.charts_ckb_except.isSelected()){
                        ObservableList<String> items = this.charts_cb_item.getItems();
                        String year = this.charts_cb_date.getSelectionModel().getSelectedItem();

                        for (int i = 0; i < items.size(); i++){
                            XYChart.Series series = new XYChart.Series();
                            series.setName(items.get(i).trim());

                            for (int j = 0; j < 12; j++){
                                String yearMonth = year + "-";

                                if (String.valueOf(j+1).length() == 1){
                                    yearMonth += "0" + (j+1);
                                }else {
                                    yearMonth += (j+1);
                                }

                                String sql = String.format("SELECT Product, DATE_FORMAT(SaleDate, '%%Y-%%m') AS YearMonth, SUM(Quantity) as NumberOfSales FROM sales WHERE Product = '%s' AND DATE_FORMAT(SaleDate, '%%Y-%%m') = '%s' GROUP BY DATE_FORMAT(SaleDate, '%%Y-%%m')", items.get(i), yearMonth);

                                this.rs = this.statement.executeQuery(sql);

                                if (this.rs.next()){
                                    series.getData().add(new XYChart.Data(rs.getString("YearMonth"), rs.getInt("NumberOfSales")));
                                }else {
                                    series.getData().add(new XYChart.Data(yearMonth, 0));
                                }
                            }

                            this.charts_areaChart.getData().addAll(series);
                        }
                    }else {
                        String year = this.charts_cb_date.getSelectionModel().getSelectedItem();

                        for (Map.Entry<String, String> mapElement : this.items.entrySet()) {
                            XYChart.Series series = new XYChart.Series();
                            series.setName(mapElement.getValue().trim());

                            for (int j = 0; j < 12; j++){
                                String yearMonth = year + "-";

                                if (String.valueOf(j+1).length() == 1){
                                    yearMonth += "0" + (j+1);
                                }else {
                                    yearMonth += (j+1);
                                }

                                String sql = String.format("SELECT Product, DATE_FORMAT(SaleDate, '%%Y-%%m') AS YearMonth, SUM(Quantity) as NumberOfSales FROM sales WHERE Product = '%s' AND DATE_FORMAT(SaleDate, '%%Y-%%m') = '%s' GROUP BY DATE_FORMAT(SaleDate, '%%Y-%%m')", mapElement.getValue(), yearMonth);

                                this.rs = this.statement.executeQuery(sql);

                                if (this.rs.next()){
                                    series.getData().add(new XYChart.Data(rs.getString("YearMonth"), rs.getInt("NumberOfSales")));
                                }else {
                                    series.getData().add(new XYChart.Data(yearMonth, 0));
                                }
                            }

                            this.charts_areaChart.getData().addAll(series);
                        }
                    }
                }else {
                    ObservableList<String> items = this.charts_cb_item.getItems();
                    String year = this.charts_cb_date.getSelectionModel().getSelectedItem();

                    for (int i = 0; i < items.size(); i++){
                        XYChart.Series series = new XYChart.Series();
                        series.setName(items.get(i).trim());

                        for (int j = 0; j < 12; j++){
                            String yearMonth = year + "-";

                            if (String.valueOf(j+1).length() == 1){
                                yearMonth += "0" + (j+1);
                            }else {
                                yearMonth += (j+1);
                            }

                            String sql = String.format("SELECT Product, DATE_FORMAT(SaleDate, '%%Y-%%m') AS YearMonth, SUM(Quantity) as NumberOfSales FROM sales WHERE Product = '%s' AND DATE_FORMAT(SaleDate, '%%Y-%%m') = '%s' GROUP BY DATE_FORMAT(SaleDate, '%%Y-%%m')", items.get(i), yearMonth);

                            this.rs = this.statement.executeQuery(sql);

                            if (this.rs.next()){
                                series.getData().add(new XYChart.Data(rs.getString("YearMonth"), rs.getInt("NumberOfSales")));
                            }else {
                                series.getData().add(new XYChart.Data(yearMonth, 0));
                            }
                        }

                        this.charts_areaChart.getData().addAll(series);
                    }
                }
            }else{
                if (!items.isEmpty()){
                    if (this.charts_ckb_except.isSelected()){
                        ObservableList<String> items = this.charts_cb_item.getItems();

                        for (int i = 0; i < items.size(); i++){
                            XYChart.Series series = new XYChart.Series();
                            series.setName(items.get(i).trim());

                            String yearMonth = this.charts_cb_date.getSelectionModel().getSelectedItem();

                            int size = 30;

                            if (yearMonth.startsWith("01", 5) || yearMonth.startsWith("03", 5) || yearMonth.startsWith("05", 5) ||
                                    yearMonth.startsWith("07", 5) || yearMonth.startsWith("08", 5) || yearMonth.startsWith("10", 5) ||
                                    yearMonth.startsWith("12", 5)){
                                size = 31;
                            }else if (yearMonth.startsWith("02", 5)){
                                if (Integer.parseInt(yearMonth.substring(0, 4)) % 4 == 0){
                                    size = 29;
                                }else {
                                    size = 28;
                                }
                            }

                            for (int j = 0; j < size; j++){
                                yearMonth = this.charts_cb_date.getSelectionModel().getSelectedItem() + "-";

                                if (String.valueOf(j+1).length() == 1){
                                    yearMonth += "0" + (j+1);
                                }else {
                                    yearMonth += (j+1);
                                }

                                String sql = String.format("SELECT Product, DATE_FORMAT(SaleDate, '%%Y-%%m-%%d') AS YearMonth, SUM(Quantity) as NumberOfSales FROM sales WHERE Product = '%s' AND DATE_FORMAT(SaleDate, '%%Y-%%m-%%d') = '%s' GROUP BY DATE_FORMAT(SaleDate, '%%Y-%%m-%%d');", items.get(i), yearMonth);

                                this.rs = this.statement.executeQuery(sql);

                                if (this.rs.next()){
                                    series.getData().add(new XYChart.Data(String.valueOf(j), rs.getInt("NumberOfSales")));
                                }else {
                                    series.getData().add(new XYChart.Data(String.valueOf(j), 0));
                                }
                            }

                            this.charts_areaChart.getData().addAll(series);
                        }
                    }else {
                        ObservableList<String> items = this.charts_cb_item.getItems();

                        for (Map.Entry<String, String> mapElement : this.items.entrySet()) {
                            XYChart.Series series = new XYChart.Series();
                            series.setName(mapElement.getValue().trim());

                            String yearMonth = this.charts_cb_date.getSelectionModel().getSelectedItem();

                            int size = 30;

                            if (yearMonth.startsWith("01", 5) || yearMonth.startsWith("03", 5) || yearMonth.startsWith("05", 5) ||
                                    yearMonth.startsWith("07", 5) || yearMonth.startsWith("08", 5) || yearMonth.startsWith("10", 5) ||
                                    yearMonth.startsWith("12", 5)){
                                size = 31;
                            }else if (yearMonth.startsWith("02", 5)){
                                if (Integer.parseInt(yearMonth.substring(0, 4)) % 4 == 0){
                                    size = 29;
                                }else {
                                    size = 28;
                                }
                            }

                            for (int j = 0; j < size; j++){
                                yearMonth = this.charts_cb_date.getSelectionModel().getSelectedItem() + "-";

                                if (String.valueOf(j+1).length() == 1){
                                    yearMonth += "0" + (j+1);
                                }else {
                                    yearMonth += (j+1);
                                }

                                String sql = String.format("SELECT Product, DATE_FORMAT(SaleDate, '%%Y-%%m-%%d') AS YearMonth, SUM(Quantity) as NumberOfSales FROM sales WHERE Product = '%s' AND DATE_FORMAT(SaleDate, '%%Y-%%m-%%d') = '%s' GROUP BY DATE_FORMAT(SaleDate, '%%Y-%%m-%%d');", mapElement.getValue(), yearMonth);

                                this.rs = this.statement.executeQuery(sql);

                                if (this.rs.next()){
                                    series.getData().add(new XYChart.Data(String.valueOf(j), rs.getInt("NumberOfSales")));
                                }else {
                                    series.getData().add(new XYChart.Data(String.valueOf(j), 0));
                                }
                            }

                            this.charts_areaChart.getData().addAll(series);
                        }
                    }
                }else {
                    ObservableList<String> items = this.charts_cb_item.getItems();

                    for (int i = 0; i < items.size(); i++){
                        XYChart.Series series = new XYChart.Series();
                        series.setName(items.get(i).trim());

                        String yearMonth = this.charts_cb_date.getSelectionModel().getSelectedItem();

                        int size = 30;

                        if (yearMonth.startsWith("01", 5) || yearMonth.startsWith("03", 5) || yearMonth.startsWith("05", 5) ||
                                yearMonth.startsWith("07", 5) || yearMonth.startsWith("08", 5) || yearMonth.startsWith("10", 5) ||
                                yearMonth.startsWith("12", 5)){
                             size = 31;
                        }else if (yearMonth.startsWith("02", 5)){
                            if (Integer.parseInt(yearMonth.substring(0, 4)) % 4 == 0){
                                size = 29;
                            }else {
                                size = 28;
                            }
                        }

                        for (int j = 0; j < size; j++){
                            yearMonth = this.charts_cb_date.getSelectionModel().getSelectedItem() + "-";

                            if (String.valueOf(j+1).length() == 1){
                                yearMonth += "0" + (j+1);
                            }else {
                                yearMonth += (j+1);
                            }

                            String sql = String.format("SELECT Product, DATE_FORMAT(SaleDate, '%%Y-%%m-%%d') AS YearMonth, SUM(Quantity) as NumberOfSales FROM sales WHERE Product = '%s' AND DATE_FORMAT(SaleDate, '%%Y-%%m-%%d') = '%s' GROUP BY DATE_FORMAT(SaleDate, '%%Y-%%m-%%d');", items.get(i), yearMonth);

                            this.rs = this.statement.executeQuery(sql);

                            if (this.rs.next()){
                                series.getData().add(new XYChart.Data(String.valueOf(j+1), rs.getInt("NumberOfSales")));
                            }else {
                                series.getData().add(new XYChart.Data(String.valueOf(j+1), 0));
                            }
                        }

                        this.charts_areaChart.getData().addAll(series);
                    }
                }
            }
        }
    }

    public void clearItems() throws SQLException {
        if (this.itemsC.isEmpty())
            return;

        this.charts_lv_items.getItems().clear();

        for (String s : this.itemsC) {
            this.charts_cb_item.getItems().add(s);
        }

        this.itemsC.clear();
        this.items.clear();

        setTimeBounds();

        // load chart
    }

    public void addItem() throws IOException, SQLException {
        String item = this.charts_cb_item.getSelectionModel().getSelectedItem();

        if (item == null){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "No item is selected",
                    "Ok", "close");
        }else {
            this.items.put(item, item);

            this.charts_cb_item.getItems().remove(item);

            ObservableList<String> itemsList = FXCollections.observableArrayList();

            itemsList.addAll(this.items.values());

            this.charts_cb_item.getSelectionModel().select(null);
            this.charts_lv_items.getItems().clear();
            this.charts_lv_items.setItems(itemsList);

            setTimeBounds();
            // loadChart
        }
    }

    public void chartTypeChanged() {
        String chartType = this.charts_cb_chartType.getSelectionModel().getSelectedItem();

        if (chartType.equals("Pie Chart")){
            this.charts_lv_items.setDisable(true);
            this.charts_cb_item.setDisable(true);
            this.charts_ckb_except.setDisable(true);
            this.charts_button_add.setDisable(true);
            this.charts_button_clearItems.setDisable(true);
            return;
        }

        this.charts_cb_item.setDisable(false);
        this.charts_ckb_except.setDisable(false);
        this.charts_button_add.setDisable(false);
        this.charts_button_clearItems.setDisable(false);
        this.charts_lv_items.setDisable(false);
    }

    public void dataChanged() throws SQLException {
        setTimeBounds();
        setItems();
        changeChartTitle();
    }

    public void setItems() throws SQLException {
        this.charts_ckb_except.setSelected(false);
        this.charts_lv_items.getItems().clear();
        this.charts_cb_item.getItems().clear();
        this.items.clear();
        this.itemsC.clear();

        String data = this.charts_cb_data.getSelectionModel().getSelectedItem();
        String sql;

        if (data.equals("Sales"))
            sql = "SELECT * FROM sales GROUP BY Product";
        else if (data.equals("Expenses"))
            sql = "SELECT * FROM expenses WHERE Status = 'Transaction Completed' GROUP BY ExpenseTitle";
        else
            sql = "SELECT * FROM inventory WHERE Status NOT LIKE '%To Be Delivered%' GROUP BY ExpenseTitle";

        this.rs = this.statement.executeQuery(sql);

        while (this.rs.next()){
            if (data.equals("Sales"))
                this.charts_cb_item.getItems().add(this.rs.getString("Product"));
            else
                this.charts_cb_item.getItems().add(this.rs.getString("ExpenseTitle"));
        }
    }

    public void changeChartTitle(){
        String data = this.charts_cb_data.getSelectionModel().getSelectedItem();

        String text;

        if (data.equals("Income")){
            text = "Income";
        }else if (data.equals("Loss")){
            text = "Loss";
        }else if (data.equals("Sales")){
            text = "Number of sales made";
        }else {
            text = "Business Expenses";
        }

        String time = this.charts_cb_time.getSelectionModel().getSelectedItem();
        String date = this.charts_cb_date.getSelectionModel().getSelectedItem();

        if (date != null){
            text += String.format(" (%s, %s)", time, date);
        }else if (time != null && time.equals("Yearly")){
            text += String.format(" (%s, %s)", time, "All Time");
        }

        this.charts_lbl_chartTitle.setText(text);
    }

    public void restrictDatePicker(DatePicker datePicker, LocalDate minDate, LocalDate maxDate) {
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(minDate)) {
                            setDisable(true);
                        } else if (item.isAfter(maxDate)) {
                            setDisable(true);
                        }
                    }
                };
            }
        };
        datePicker.setDayCellFactory(dayCellFactory);
    }

    public void createReceipt() throws IOException, DocumentException, SQLException {
        if (this.sales_ttv_main.getSelectionModel().getSelectedItem() == null){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Please select a record",
                    "Ok", "close");

            return;
        }

        SalesTable row = this.sales_ttv_main.getSelectionModel().getSelectedItem().getValue();

        String receiptID = row.getReceiptID();
        String saleDate = row.getSaleDate();
        String userID = row.getUserID();

        this.rs = this.statement.executeQuery(String.format("SELECT * FROM user WHERE UserID = '%s'", userID));
        this.rs.next();

        String name = this.rs.getString("firstName") + " " + this.rs.getString("lastName");

        FileChooser asdf = new FileChooser();
        asdf.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        File file =  asdf.showSaveDialog(null);

        if (file == null)
            return;

        Document dc = new Document();
        PdfWriter.getInstance(dc, new FileOutputStream(file.getPath()));
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

        this.rs = this.statement.executeQuery(String.format("SELECT * FROM sales WHERE ReceiptID = '%s'", receiptID));

        while (this.rs.next()){
            String productD = this.rs.getString("Product");
            String asdfa = productD.substring(productD.indexOf(",") + 2);

            Paragraph prod = new Paragraph(new Phrase(10f, this.rightPad(asdfa,
                    25) + this.rightPad("x" + (int)this.rs.getDouble("Quantity"), 9) +
                    this.leftPad("P" + this.rs.getString("TotalPrice"), 11),
                    FontFactory.getFont(FontFactory.COURIER, 11.0f)));
            dc.add(prod);
        }

        dc.add(new Paragraph(". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ."));

        dc.add(new Paragraph("\n"));

        Paragraph bill = new Paragraph(new Phrase(10f,  this.rightPad("Total", 25) + this.leftPad("P" + row.getTotalBill(), 20),
                FontFactory.getFont(FontFactory.COURIER, 11.0f)));
        dc.add(bill);

        Paragraph cash = new Paragraph(new Phrase(10f,  this.rightPad("Cash", 25) + this.leftPad("P" + row.getCash(), 20),
                FontFactory.getFont(FontFactory.COURIER, 11.0f)));
        dc.add(cash);

        Paragraph changeP = new Paragraph(new Phrase(10f,  this.rightPad("Change", 25) + this.leftPad("P" + row.getChangeAmount(), 20),
                FontFactory.getFont(FontFactory.COURIER, 11.0f)));
        dc.add(changeP);

        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + file.getPath());
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

    public void sales_add() throws IOException {
        AddSaleApplication application = new AddSaleApplication();
        application.start(new Stage());
        application.setData(this);
    }

    public void sales_users() throws IOException {
        if (this.sales_ttv_main.getSelectionModel().getSelectedItem() == null){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Please select a record",
                    "Ok", "close");
        }else {
            SalesTable row = this.sales_ttv_main.getSelectionModel().getSelectedItem().getValue();

            this.accounts_cb_column.getSelectionModel().select("User ID");
            this.accounts_tf_search.setText(row.getUserID());
            this.accounts_cb_searchExactMatch.setSelected(true);
            this.searchAccounts();
            this.show_accounts();
        }
    }

    public void sales_inventory() throws IOException {
        if (this.sales_ttv_main.getSelectionModel().getSelectedItem() == null){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Please select a record",
                    "Ok", "close");
        }else {
            SalesTable row = this.sales_ttv_main.getSelectionModel().getSelectedItem().getValue();

            this.inventory_clearStartingDate();
            this.inventory_clearEndingDate();
            this.inventory_cb_column.getSelectionModel().select("Product Name");
            this.inventory_tf_search.setText(row.getProductName());
            this.inventory_cb_includeCancelledRecords.setSelected(true);
            this.inventory_cb_searchExactMatch.setSelected(true);
            this.searchInventory();
            this.show_inventory();
        }
    }

    public void sales_clearStartingDate(){
        this.sales_dp_startingDate.setValue(null);
    }

    public void sales_clearEndingDate(){
        this.sales_dp_endingDate.setValue(null);
    }

    public void sales_clearSearch(){
        this.sales_tf_search.clear();
    }

    public void sales_message(){
        this.sales_message.setText(this.sales_messages.get(this.sales_currentMessageIndex));

        if (sales_messages.size() == this.sales_currentMessageIndex + 1)
            this.sales_currentMessageIndex = 0;
        else
            this.sales_currentMessageIndex += 1;
    }

    public void clearSalesSelection(){
        Platform.runLater(() -> this.sales_ttv_main.getSelectionModel().clearSelection());
    }

    public void loadSalesTable(){
        this.salesRoot.getChildren().clear();

        try{
            rs = statement.executeQuery(this.currentSalesTableQuery);

            while (rs.next()){
                TreeItem<SalesTable> column = new TreeItem<>(new SalesTable(rs.getString("SaleNumber"), rs.getString("UserID"), rs.getString("Product"),
                        rs.getString("Quantity"), rs.getString("Price"), rs.getString("TotalPrice"), rs.getString("TotalBill"),
                        rs.getString("Cash"), rs.getString("ChangeAmount"),  rs.getString("ReceiptID"), rs.getString("SaleDate")));

                salesRoot.getChildren().add(column);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sales_col_saleNumber.setCellValueFactory(salesTableStringCellDataFeatures -> salesTableStringCellDataFeatures.getValue().getValue().saleNumberProperty());
        sales_col_userID.setCellValueFactory(salesTableStringCellDataFeatures -> salesTableStringCellDataFeatures.getValue().getValue().userIDProperty());
        sales_col_productName.setCellValueFactory(salesTableStringCellDataFeatures -> salesTableStringCellDataFeatures.getValue().getValue().productNameProperty());
        sales_col_quantity.setCellValueFactory(salesTableStringCellDataFeatures -> salesTableStringCellDataFeatures.getValue().getValue().quantityProperty());
        sales_col_price.setCellValueFactory(salesTableStringCellDataFeatures -> salesTableStringCellDataFeatures.getValue().getValue().priceProperty());
        sales_col_totalPrice.setCellValueFactory(salesTableStringCellDataFeatures -> salesTableStringCellDataFeatures.getValue().getValue().totalPriceProperty());
        sales_col_receiptID.setCellValueFactory(salesTableStringCellDataFeatures -> salesTableStringCellDataFeatures.getValue().getValue().receiptIDProperty());
        sales_col_saleDate.setCellValueFactory(salesTableStringCellDataFeatures -> salesTableStringCellDataFeatures.getValue().getValue().saleDateProperty());
        sales_col_totalBill.setCellValueFactory(salesTableStringCellDataFeatures -> salesTableStringCellDataFeatures.getValue().getValue().totalBillProperty());
        sales_col_changeAmount.setCellValueFactory(salesTableStringCellDataFeatures -> salesTableStringCellDataFeatures.getValue().getValue().changeAmountProperty());
        sales_col_cash.setCellValueFactory(salesTableStringCellDataFeatures -> salesTableStringCellDataFeatures.getValue().getValue().cashProperty());

        this.sales_ttv_main.setEditable(true);
        this.sales_ttv_main.setRoot(salesRoot);
        this.sales_ttv_main.setShowRoot(false);
    }

    public void searchSales(){
        String column = this.sales_cb_column.getSelectionModel().getSelectedItem();
        String toSearch = this.sales_tf_search.getText();
        LocalDate date = this.sales_dp_startingDate.getValue();
        String startingDate = date == null ? null : date.toString();
        date = this.sales_dp_endingDate.getValue();
        String endingDate = date == null ? null : date.toString();
        boolean searchByMatch = this.sales_cb_searchExactMatch.isSelected();

        String sql = "SELECT * FROM sales";

        if (startingDate != null)
            sql += String.format(" WHERE SaleDate >= '%s'", startingDate.replaceAll(" ", ""));

        String a = startingDate != null ? "AND" : "WHERE";

        if (endingDate != null)
                sql += String.format(" %s SaleDate <= '%s'", a, endingDate.replaceAll(" ", ""));

        a = startingDate != null || endingDate != null ? "AND" : "WHERE";

        if (!toSearch.equals("")){
            if (searchByMatch){
                sql += String.format(" %s %s = '%s'", a, column.replaceAll(" ", ""), toSearch.trim());
            }else{
                sql += String.format(" %s %s LIKE '%%%s%%'", a, column.replaceAll(" ", ""), toSearch.trim());
            }
        }

        this.currentSalesTableQuery = sql;

        this.loadSalesTable();
    }

    public void account_showPostalCodeMessage() throws IOException {
        if (!this.account_postalCodeMessage.equals("")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Message", this.account_postalCodeMessage + ". Revert to original?",
                    "\uE149", "revert postal code");
        }
    }

    public void account_postalCodeChange() {
        if (!this.account_contactAddressStart){
            return;
        }

        String newValue = this.account_tf_postalCode.getText();

        if (newValue.equals(this.account_ll_contactAddress.get(6))){
            this.account_button_postalCode.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_postalCode.setVisible(false);
            this.account_postalCodeMessage = "";
            this.account_postalCodeValid = null;
        }else if (newValue.equals("")){
            this.account_button_postalCode.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_postalCode.setVisible(true);
            this.account_postalCodeMessage = "Postal Code cannot be empty";
            this.account_postalCodeValid = false;
        }else {
            try{
                Integer.parseInt(newValue);
            }catch (Exception e){
                this.account_button_postalCode.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
                this.account_button_postalCode.setVisible(true);
                this.account_postalCodeMessage = "Postal Code must only consist of digits";
                this.account_postalCodeValid = false;
                return;
            }

            if (newValue.length() != 4){
                this.account_button_postalCode.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
                this.account_button_postalCode.setVisible(true);
                this.account_postalCodeMessage = "Postal code can only contain 4 digits";
                this.account_postalCodeValid = false;
            }else {
                this.account_button_postalCode.getStyleClass().add("account-button-valid");
                this.account_button_postalCode.setVisible(true);
                this.account_postalCodeMessage = "";
                this.account_postalCodeValid = true;
            }
        }
    }

    public void account_showStreetMessage() throws IOException {
        if (!this.account_streetMessage.equals("")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Message", this.account_streetMessage + ". Revert to original?",
                    "\uE149", "revert street");
        }
    }

    public void account_streetChange() {
        if (!this.account_contactAddressStart){
            return;
        }

        String newValue = this.account_tf_street.getText();

        if (newValue.equals(this.account_ll_contactAddress.get(6))){
            this.account_button_street.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_street.setVisible(false);
            this.account_streetMessage = "";
            this.account_streetValid = null;
        }else if (newValue.equals("")){
            this.account_button_street.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_street.setVisible(true);
            this.account_streetMessage = "Street cannot be empty";
            this.account_streetValid = false;
        }else if (newValue.length() < 3){
            this.account_button_street.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_street.setVisible(true);
            this.account_streetMessage = "Street must contain at least 3 characters";
            this.account_streetValid = false;
        }else {
            this.account_button_street.getStyleClass().add("account-button-valid");
            this.account_button_street.setVisible(true);
            this.account_streetMessage = "";
            this.account_streetValid = true;
        }
    }

    public void account_showBarangayMessage() throws IOException {
        if (!this.account_barangayMessage.equals("")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Message", this.account_barangayMessage + ". Revert to original?",
                    "\uE149", "revert barangay");
        }
    }

    public void account_barangayChange() {
        if (!this.account_contactAddressStart){
            return;
        }

        String newValue = this.account_tf_barangay.getText();

        if (newValue.equals(this.account_ll_contactAddress.get(5))){
            this.account_button_barangay.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_barangay.setVisible(false);
            this.account_barangayMessage = "";
            this.account_barangayValid = null;
        }else if (newValue.equals("")){
            this.account_button_barangay.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_barangay.setVisible(true);
            this.account_barangayMessage = "Barangay cannot be empty";
            this.account_barangayValid = false;
        }else if (newValue.length() < 3){
            this.account_button_barangay.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_barangay.setVisible(true);
            this.account_barangayMessage = "Barangay must contain at least 3 characters";
            this.account_barangayValid = false;
        }else {
            this.account_button_barangay.getStyleClass().add("account-button-valid");
            this.account_button_barangay.setVisible(true);
            this.account_barangayMessage = "";
            this.account_barangayValid = true;
        }
    }

    public void account_showCityMessage() throws IOException {
        if (!this.account_cityMessage.equals("")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Message", this.account_cityMessage + ". Revert to original?",
                    "\uE149", "revert city");
        }
    }

    public void account_cityChange() {
        if (!this.account_contactAddressStart){
            return;
        }

        String newValue = this.account_tf_city.getText();

        if (newValue.equals(this.account_ll_contactAddress.get(4))){
            this.account_button_city.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_city.setVisible(false);
            this.account_cityMessage = "";
            this.account_cityValid = null;
        }else if (newValue.equals("")){
            this.account_button_city.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_city.setVisible(true);
            this.account_cityMessage = "City cannot be empty";
            this.account_cityValid = false;
        }else if (newValue.length() < 3){
            this.account_button_city.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_city.setVisible(true);
            this.account_cityMessage = "City must contain at least 3 characters";
            this.account_cityValid = false;
        }else {
            this.account_button_city.getStyleClass().add("account-button-valid");
            this.account_button_city.setVisible(true);
            this.account_cityMessage = "";
            this.account_cityValid = true;
        }
    }

    public void account_showProvinceMessage() throws IOException {
        if (!this.account_provinceMessage.equals("")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Message", this.account_provinceMessage + ". Revert to original?",
                    "\uE149", "revert province");
        }
    }

    public void account_provinceChange() {
        if (!this.account_contactAddressStart){
            return;
        }

        String newValue = this.account_tf_province.getText();

        if (newValue.equals(this.account_ll_contactAddress.get(3))){
            this.account_button_province.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_province.setVisible(false);
            this.account_provinceMessage = "";
            this.account_provinceValid = null;
        }else if (newValue.equals("")){
            this.account_button_province.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_province.setVisible(true);
            this.account_provinceMessage = "Province cannot be empty";
            this.account_provinceValid = false;
        }else if (newValue.length() < 3){
            this.account_button_province.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_province.setVisible(true);
            this.account_provinceMessage = "Province must contain at least 3 characters";
            this.account_provinceValid = false;
        }else {
            this.account_button_province.getStyleClass().add("account-button-valid");
            this.account_button_province.setVisible(true);
            this.account_provinceMessage = "";
            this.account_provinceValid = true;
        }
    }

    public void account_showEmailMessage() throws IOException {
        if (!this.account_emailMessage.equals("")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Message", this.account_emailMessage + ". Revert to original?",
                    "\uE149", "revert email");
        }
    }

    public void account_emailChange() {
        if (!this.account_contactAddressStart){
            return;
        }

        String newValue = this.account_tf_email.getText();

        if (newValue.equals(this.account_ll_contactAddress.get(2))){
            this.account_button_email.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_email.setVisible(false);
            this.account_emailMessage = "";
            this.account_emailValid = null;
        }else if (newValue.equals("")){
            this.account_button_email.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_email.setVisible(true);
            this.account_emailMessage = "Email cannot be empty";
            this.account_emailValid = false;
        }else if (!newValue.contains("@") || !newValue.contains(".")){
            this.account_button_email.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_email.setVisible(true);
            this.account_emailMessage = "Email must contain '@' and '.'";
            this.account_emailValid = false;
        }else if (newValue.length() < 10){
            this.account_button_email.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_email.setVisible(true);
            this.account_emailMessage = "Email must contain at least 10 characters";
            this.account_emailValid = false;
        }else {
            this.account_button_email.getStyleClass().add("account-button-valid");
            this.account_button_email.setVisible(true);
            this.account_emailMessage = "";
            this.account_emailValid = true;
        }
    }

    public void account_showTelephoneMessage() throws IOException {
        if (!this.account_telephoneMessage.equals("")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Message", this.account_telephoneMessage + ". Revert to original?",
                    "\uE149", "revert telephone");
        }
    }

    public void account_telephoneChange() {
        if (!this.account_contactAddressStart){
            return;
        }

        String newValue = this.account_tf_telephone.getText();

        if (newValue.equals(this.account_ll_contactAddress.get(1))){
            this.account_button_telephone.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_telephone.setVisible(false);
            this.account_telephoneMessage = "";
            this.account_telephoneValid = null;

            if (this.account_tf_telephone.getText().equals("") && this.account_phoneValid != null && this.account_tf_phone.getText().equals("")){
                this.account_button_phone.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
                this.account_button_phone.setVisible(true);
                this.account_phoneMessage = "Either phone number or telephone must be filled at all times";
                this.account_phoneValid = false;
            }else if (this.account_phoneValid != null && this.account_tf_phone.getText().equals("")){
                this.account_button_phone.getStyleClass().add("account-button-valid");
                this.account_button_phone.setVisible(true);
                this.account_phoneMessage = "";
                this.account_phoneValid = true;
            }
        }else if (newValue.equals("")){
            if (this.account_tf_phone.getText().equals("")){
                this.account_button_telephone.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
                this.account_button_telephone.setVisible(true);
                this.account_telephoneMessage = "Either phone number or telephone must be filled at all times";
                this.account_telephoneValid = false;
            }else {
                this.account_button_telephone.getStyleClass().add("account-button-valid");
                this.account_button_telephone.setVisible(true);
                this.account_telephoneMessage = "";
                this.account_telephoneValid = true;

            }

            if (this.account_phoneValid != null && this.account_tf_phone.getText().equals("")){
                this.account_button_phone.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
                this.account_button_phone.setVisible(true);
                this.account_phoneMessage = "Either phone number or telephone must be filled at all times";
                this.account_phoneValid = false;
            }
        }else {
            if ((this.account_phoneValid != null && !this.account_phoneValid) && this.account_tf_phone.getText().equals("")){
                this.account_button_phone.getStyleClass().add("account-button-valid");
                this.account_button_phone.setVisible(true);
                this.account_phoneMessage = "";
                this.account_phoneValid = true;
            }

            this.account_button_telephone.getStyleClass().add("account-button-valid");
            this.account_button_telephone.setVisible(true);
            this.account_telephoneMessage = "";
            this.account_telephoneValid = true;
        }
    }

    public void account_showPhoneMessage() throws IOException {
        if (!this.account_phoneMessage.equals("")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Message", this.account_phoneMessage + ". Revert to original?",
                    "\uE149", "revert phone");
        }
    }

    public void account_phoneChange() {
        if (!this.account_contactAddressStart){
            return;
        }

        String newValue = this.account_tf_phone.getText();

        if (newValue.equals(this.account_ll_contactAddress.get(0))){
            this.account_button_phone.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_phone.setVisible(false);
            this.account_phoneMessage = "";
            this.account_phoneValid = null;

            if (this.account_tf_phone.getText().equals("") && this.account_telephoneValid != null && this.account_tf_telephone.getText().equals("")){
                this.account_button_telephone.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
                this.account_button_telephone.setVisible(true);
                this.account_telephoneMessage = "Either phone number or telephone must be filled at all times";
                this.account_telephoneValid = false;
            }else if (this.account_telephoneValid != null && this.account_tf_telephone.getText().equals("")){
                this.account_button_telephone.getStyleClass().add("account-button-valid");
                this.account_button_telephone.setVisible(true);
                this.account_telephoneMessage = "";
                this.account_telephoneValid = true;
            }
        }else if (newValue.equals("")){
            if (this.account_tf_telephone.getText().equals("")){
                this.account_button_phone.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
                this.account_button_phone.setVisible(true);
                this.account_phoneMessage = "Either phone number or telephone must be filled at all times";
                this.account_phoneValid = false;
            }else {
                this.account_button_phone.getStyleClass().add("account-button-valid");
                this.account_button_phone.setVisible(true);
                this.account_phoneMessage = "";
                this.account_phoneValid = true;
            }

            if (this.account_telephoneValid != null && this.account_tf_telephone.getText().equals("")){
                this.account_button_telephone.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
                this.account_button_telephone.setVisible(true);
                this.account_telephoneMessage = "Either phone number or telephone must be filled at all times";
                this.account_telephoneValid = false;
            }
        }else {
            if ((this.account_telephoneValid != null && !this.account_telephoneValid) && this.account_tf_telephone.getText().equals("")){
                this.account_button_telephone.getStyleClass().add("account-button-valid");
                this.account_button_telephone.setVisible(true);
                this.account_telephoneMessage = "";
                this.account_telephoneValid = true;
            }

            try {
                Long.parseLong(newValue);
            }catch (Exception e){
                this.account_button_phone.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
                this.account_button_phone.setVisible(true);
                this.account_phoneMessage = "Phone number must consist of digits only";
                this.account_phoneValid = false;
                return;
            }

            if (newValue.length() != 11){
                this.account_button_phone.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
                this.account_button_phone.setVisible(true);
                this.account_phoneMessage = "Phone number must consist of 11 digits";
                this.account_phoneValid = false;
            }else {
                this.account_button_phone.getStyleClass().add("account-button-valid");
                this.account_button_phone.setVisible(true);
                this.account_phoneMessage = "";
                this.account_phoneValid = true;
            }
        }
    }

    public void account_editContactAddress() throws IOException, SQLException {
        if (!this.account_contactAddressStart){
            //  
            Effect effect = this.account_pl_name.getEffect();
            this.account_pl_contactAddress.setEffect(effect);

            this.account_tf_phone.setEditable(true);
            this.account_tf_telephone.setEditable(true);
            this.account_tf_email.setEditable(true);
            this.account_tf_province.setEditable(true);
            this.account_tf_city.setEditable(true);
            this.account_tf_barangay.setEditable(true);
            this.account_tf_street.setEditable(true);
            this.account_tf_postalCode.setEditable(true);

            this.account_tf_phone.getStyleClass().add("account-account-user-tf");
            this.account_tf_telephone.getStyleClass().add("account-account-user-tf");
            this.account_tf_email.getStyleClass().add("account-account-user-tf");
            this.account_tf_province.getStyleClass().add("account-account-user-tf");
            this.account_tf_city.getStyleClass().add("account-account-user-tf");
            this.account_tf_barangay.getStyleClass().add("account-account-user-tf");
            this.account_tf_street.getStyleClass().add("account-account-user-tf");
            this.account_tf_postalCode.getStyleClass().add("account-account-user-tf");

            this.account_ll_contactAddress.add(this.account_tf_phone.getText());
            this.account_ll_contactAddress.add(this.account_tf_telephone.getText());
            this.account_ll_contactAddress.add(this.account_tf_email.getText());
            this.account_ll_contactAddress.add(this.account_tf_province.getText());
            this.account_ll_contactAddress.add(this.account_tf_city.getText());
            this.account_ll_contactAddress.add(this.account_tf_barangay.getText());
            this.account_ll_contactAddress.add(this.account_tf_street.getText());
            this.account_ll_contactAddress.add(this.account_tf_postalCode.getText());

            this.account_button_contactAddress.setText("\uE10B");
            account_contactAddressStart = true;
        }else {
            if ( (this.account_phoneValid != null && !this.account_phoneValid) || (this.account_telephoneValid != null && !this.account_telephoneValid)
                    || (this.account_emailValid != null && !this.account_emailValid) || (this.account_provinceValid != null && !this.account_provinceValid)
                    || (this.account_cityValid != null && !this.account_cityValid) || (this.account_barangayValid != null && !this.account_barangayValid)
                    || (this.account_streetValid != null && !this.account_streetValid) || (this.account_postalCodeValid != null && !this.account_postalCodeValid)){
                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Error", "One or more fields are invalid. Click the red button for message or click the button to revert.",
                        "\uE149", "revert contact-address");
            }else if (this.account_phoneValid == null && this.account_telephoneValid == null && this.account_emailValid == null && this.account_provinceValid == null
                    && this.account_cityValid == null && this.account_barangayValid == null && this.account_streetValid == null && this.account_postalCodeValid == null){
                this.account_resetContactAddressLabel();
            }else {
                if (Boolean.TRUE.equals(this.account_phoneValid)){
                    String newPhone = this.account_tf_phone.getText();

                    this.statement.execute(String.format("UPDATE user SET PhoneNumber = '%s' WHERE Username = '%s'", newPhone, this.currentUsername));
                    this.account_tf_phone.setText(newPhone);
                }

                if (Boolean.TRUE.equals(this.account_telephoneValid)){
                    String newTelephone = this.account_tf_telephone.getText();

                    this.statement.execute(String.format("UPDATE user SET Telephone = '%s' WHERE Username = '%s'", newTelephone, this.currentUsername));
                    this.account_tf_telephone.setText(newTelephone);
                }

                if (Boolean.TRUE.equals(this.account_emailValid)){
                    String newTelephone = this.account_tf_email.getText();

                    this.statement.execute(String.format("UPDATE user SET Email = '%s' WHERE Username = '%s'", newTelephone, this.currentUsername));
                    this.account_tf_email.setText(newTelephone);
                }

                if (Boolean.TRUE.equals(this.account_provinceValid)){
                    String newProvince = this.account_tf_province.getText();

                    this.statement.execute(String.format("UPDATE user SET Province = '%s' WHERE Username = '%s'", newProvince, this.currentUsername));
                    this.account_tf_province.setText(newProvince);
                }

                if (Boolean.TRUE.equals(this.account_cityValid)){
                    String newCity = this.account_tf_city.getText();

                    this.statement.execute(String.format("UPDATE user SET City = '%s' WHERE Username = '%s'", newCity, this.currentUsername));
                    this.account_tf_city.setText(newCity);
                }

                if (Boolean.TRUE.equals(this.account_barangayValid)){
                    String newBarangay = this.account_tf_barangay.getText();

                    this.statement.execute(String.format("UPDATE user SET Barangay = '%s' WHERE Username = '%s'", newBarangay, this.currentUsername));
                    this.account_tf_barangay.setText(newBarangay);
                }

                if (Boolean.TRUE.equals(this.account_streetValid)){
                    String newStreet = this.account_tf_street.getText();

                    this.statement.execute(String.format("UPDATE user SET StreetName = '%s' WHERE Username = '%s'", newStreet, this.currentUsername));
                    this.account_tf_street.setText(newStreet);
                }

                if (Boolean.TRUE.equals(this.account_postalCodeValid)){
                    String newPostalCode = this.account_tf_postalCode.getText();

                    this.statement.execute(String.format("UPDATE user SET PostalCode = '%s' WHERE Username = '%s'", newPostalCode, this.currentUsername));
                    this.account_tf_postalCode.setText(newPostalCode);
                }

                this.account_resetContactAddressLabel();

                this.loadAccountsTable();
                // reload transaction table

                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Success", "Field(s) successfully changed",
                        "Ok", "close");
            }
        }
    }

    public void account_contactAddressHoverExit(){
        if (!account_contactAddressStart){
            this.account_pl_contactAddress.setEffect(null);
        }
    }

    public void account_contactAddressHover(){
        if (!account_accountUserStart){
            Effect effect = this.account_pl_name.getEffect();
            this.account_pl_contactAddress.setEffect(effect);
        }
    }

    public void account_resetContactAddressLabel(){
        this.account_pl_contactAddress.setEffect(null);

        this.account_tf_phone.setEditable(false);
        this.account_tf_telephone.setEditable(false);
        this.account_tf_email.setEditable(false);
        this.account_tf_province.setEditable(false);
        this.account_tf_city.setEditable(false);
        this.account_tf_barangay.setEditable(false);
        this.account_tf_street.setEditable(false);
        this.account_tf_postalCode.setEditable(false);

        this.account_tf_phone.getStyleClass().removeIf(style -> style.equals("account-account-user-tf"));
        this.account_tf_telephone.getStyleClass().removeIf(style -> style.equals("account-account-user-tf"));
        this.account_tf_email.getStyleClass().removeIf(style -> style.equals("account-account-user-tf"));
        this.account_tf_province.getStyleClass().removeIf(style -> style.equals("account-account-user-tf"));
        this.account_tf_city.getStyleClass().removeIf(style -> style.equals("account-account-user-tf"));
        this.account_tf_barangay.getStyleClass().removeIf(style -> style.equals("account-account-user-tf"));
        this.account_tf_street.getStyleClass().removeIf(style -> style.equals("account-account-user-tf"));
        this.account_tf_postalCode.getStyleClass().removeIf(style -> style.equals("account-account-user-tf"));

        this.account_button_phone.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
        this.account_button_telephone.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
        this.account_button_email.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
        this.account_button_province.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
        this.account_button_city.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
        this.account_button_barangay.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
        this.account_button_street.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
        this.account_button_postalCode.getStyleClass().removeIf(style -> style.equals("account-button-valid"));

        this.account_button_phone.setVisible(false);
        this.account_button_telephone.setVisible(false);
        this.account_button_email.setVisible(false);
        this.account_button_province.setVisible(false);
        this.account_button_city.setVisible(false);
        this.account_button_barangay.setVisible(false);
        this.account_button_street.setVisible(false);
        this.account_button_postalCode.setVisible(false);

        account_phoneValid = null;
        account_telephoneValid = null;
        account_emailValid = null;
        account_provinceValid = null;
        account_cityValid = null;
        account_barangayValid = null;
        account_streetValid = null;
        account_postalCodeValid = null;

        account_phoneMessage = "";
        account_telephoneMessage = "";
        account_emailMessage = "";
        account_provinceMessage = "";
        account_cityMessage = "";
        account_barangayMessage = "";
        account_streetMessage = "";
        account_postalCodeMessage = "";

        this.account_ll_contactAddress = new LinkedList<>();

        this.account_button_contactAddress.setText("\uE104");
        account_contactAddressStart = false;

        this.account_lbl_name.requestFocus();
    }

    public void account_showBirthDateMessage() throws IOException {
        if (!this.account_birthDateMessage.equals("")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Message", this.account_birthDateMessage + ". Revert to original?",
                    "\uE149", "revert birth date");
        }
    }

    public void account_birthDateChange() {
        if (!this.account_accountUserStart){
            return;
        }

        String newValue = this.account_dp_birthDate.getValue().toString();

        if (newValue.equals(this.account_ll_accountUser.get(4))){
            this.account_button_birthDate.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_birthDate.setVisible(false);
            this.account_birthDateMessage = "";
            this.account_birthDateValid = null;
        }else {
            this.account_button_birthDate.getStyleClass().add("account-button-valid");
            this.account_button_birthDate.setVisible(true);
            this.account_birthDateMessage = "";
            this.account_birthDateValid = true;
        }
    }

    public void account_showLastNameMessage() throws IOException {
        if (!this.account_lastNameMessage.equals("")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Message", this.account_lastNameMessage + ". Revert to original?",
                    "\uE149", "revert last name");
        }
    }

    public void account_lastNameChange() {
        if (!this.account_accountUserStart){
            return;
        }

        String newValue = this.account_tf_lastName.getText();

        if (newValue.equals(this.account_ll_accountUser.get(3))){
            this.account_button_lastName.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_lastName.setVisible(false);
            this.account_lastNameMessage = "";
            this.account_lastNameValid = null;
        }else if (newValue.equals("")){
            this.account_button_lastName.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_lastName.setVisible(true);
            this.account_lastNameMessage = "Lastname cannot be empty";
            this.account_lastNameValid = false;
        }else if (newValue.length() < 3){
            this.account_button_lastName.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_lastName.setVisible(true);
            this.account_lastNameMessage = "Lastname must contain at least 3 characters";
            this.account_lastNameValid = false;
        }else {
            this.account_button_lastName.getStyleClass().add("account-button-valid");
            this.account_button_lastName.setVisible(true);
            this.account_lastNameMessage = "";
            this.account_lastNameValid = true;
        }
    }

    public void account_showFirstNameMessage() throws IOException {
        if (!this.account_firstNameMessage.equals("")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Message", this.account_firstNameMessage + ". Revert to original?",
                    "\uE149", "revert first name");
        }
    }

    public void account_firstNameChange() {
        if (!this.account_accountUserStart){
            return;
        }

        String newValue = this.account_tf_firstName.getText();

        if (newValue.equals(this.account_ll_accountUser.get(2))){
            this.account_button_firstName.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_firstName.setVisible(false);
            this.account_firstNameMessage = "";
            this.account_firstNameValid = null;
        }else if (newValue.equals("")){
            this.account_button_firstName.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_firstName.setVisible(true);
            this.account_firstNameMessage = "Firstname cannot be empty";
            this.account_firstNameValid = false;
        }else if (newValue.length() < 3){
            this.account_button_firstName.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_firstName.setVisible(true);
            this.account_firstNameMessage = "Firstname must contain at least 3 characters";
            this.account_firstNameValid = false;
        }else {
            this.account_button_firstName.getStyleClass().add("account-button-valid");
            this.account_button_firstName.setVisible(true);
            this.account_firstNameMessage = "";
            this.account_firstNameValid = true;
        }
    }

    public void account_showPasswordMessage() throws IOException {
        if (!this.account_passwordMessage.equals("")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Message", this.account_passwordMessage + ". Revert to original?",
                    "\uE149", "revert password");
        }
    }
    
    public void account_passwordChange() throws SQLException {
        if (!this.account_accountUserStart){
            return;
        }

        String newValue = this.account_tf_password.getText();

        if (newValue.equals(this.account_ll_accountUser.get(1))){
            this.account_button_password.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_password.setVisible(false);
            this.account_passwordMessage = "";
            this.account_passwordValid = null;
        }else if (newValue.equals("")){
            this.account_button_password.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_password.setVisible(true);
            this.account_passwordMessage = "Password cannot be empty";
            this.account_passwordValid = false;
        }else if (newValue.length() < 8){
            this.account_button_password.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_password.setVisible(true);
            this.account_passwordMessage = "Password must be at least 8 characters long";
            this.account_passwordValid = false;
        }else {
            this.account_button_password.getStyleClass().add("account-button-valid");
            this.account_button_password.setVisible(true);
            this.account_passwordMessage = "";
            this.account_passwordValid = true;
        }
    }
    
    public void account_showUsernameMessage() throws IOException {
        if (!this.account_usernameMessage.equals("")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Message", this.account_usernameMessage + ". Revert to original?",
                    "\uE149", "revert username");
        }
    }

    public void account_usernameChange() throws SQLException {
        if (!this.account_accountUserStart){
            return;
        }

        String newValue = this.account_tf_username.getText();

        if (newValue.equals(this.account_ll_accountUser.get(0))){
            this.account_button_username.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_username.setVisible(false);
            this.account_usernameMessage = "";
            this.account_usernameValid = null;
        }else if (newValue.equals("")){
            this.account_button_username.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_username.setVisible(true);
            this.account_usernameMessage = "Username cannot be empty";
            this.account_usernameValid = false;
        }else if (newValue.length() < 5 || newValue.length() > 16){
            this.account_button_username.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
            this.account_button_username.setVisible(true);
            this.account_usernameMessage = "Username must have 5-16 characters";
            this.account_usernameValid = false;
        }else {
            this.rs = this.statement.executeQuery(String.format("SELECT * FROM accounts WHERE Username = '%s'", newValue));

            if (rs.next()){
                this.account_button_username.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
                this.account_tf_username.setVisible(true);
                this.account_usernameMessage = "Username already taken";
                this.account_usernameValid = false;
            }else {
                this.account_button_username.getStyleClass().add("account-button-valid");
                this.account_button_username.setVisible(true);
                this.account_usernameMessage = "";
                this.account_usernameValid = true;
            }
        }
    }

    public void account_editAccountUserDetails() throws IOException, SQLException {
        if (!this.account_accountUserStart){
            //  
            Effect effect = this.account_pl_name.getEffect();
            this.account_pl_accountUser.setEffect(effect);

            this.account_tf_username.setEditable(true);
            this.account_tf_password.setEditable(true);
            this.account_tf_firstName.setEditable(true);
            this.account_tf_lastName.setEditable(true);
            this.account_lbl_birthDatePatch.setVisible(false);

            this.account_tf_username.getStyleClass().add("account-account-user-tf");
            this.account_tf_password.getStyleClass().add("account-account-user-tf");
            this.account_tf_firstName.getStyleClass().add("account-account-user-tf");
            this.account_tf_lastName.getStyleClass().add("account-account-user-tf");
            this.account_dp_birthDate.getStyleClass().add("account-account-user-dp");

            this.account_ll_accountUser.add(this.account_tf_username.getText());
            this.account_ll_accountUser.add(this.account_tf_password.getText());
            this.account_ll_accountUser.add(this.account_tf_firstName.getText());
            this.account_ll_accountUser.add(this.account_tf_lastName.getText());
            this.account_ll_accountUser.add(this.account_dp_birthDate.getValue().toString());

            this.account_button_accountDetails.setText("\uE10B");
            account_accountUserStart = true;
        }else {
            if ( (this.account_usernameValid != null && !this.account_usernameValid) || (this.account_passwordValid != null && !this.account_passwordValid)
            || (this.account_firstNameValid != null && !this.account_firstNameValid) || (this.account_lastNameValid != null && !this.account_lastNameValid)
            || (this.account_birthDateValid != null && !this.account_birthDateValid)){
                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Error", "One or more fields are invalid. Click the red button for message or click the button to revert.",
                        "\uE149", "revert account-user");
            }else if (this.account_usernameValid == null && this.account_passwordValid == null && this.account_firstNameValid == null && this.account_lastNameValid == null
            && this.account_birthDateValid == null){
                this.account_resetAccountUserLabel();
            }else {
                if (Boolean.TRUE.equals(this.account_usernameValid)){
                    String newUsername = this.account_tf_username.getText();
                    String oldUsername = this.account_ll_accountUser.get(0);

                    this.statement.execute(String.format("UPDATE accounts SET Username = '%s' WHERE Username = '%s'", newUsername, oldUsername));
                    this.account_tf_username.setText(newUsername);
                    this.currentUsername = newUsername;
                }

                if (Boolean.TRUE.equals(this.account_passwordValid)){
                    String newPassword = this.account_tf_password.getText();

                    this.statement.execute(String.format("UPDATE accounts SET Password = '%s' WHERE Username = '%s'", newPassword, this.currentUsername));
                    this.account_tf_password.setText(newPassword);
                    this.currentPassword = newPassword;
                }

                if (Boolean.TRUE.equals(this.account_firstNameValid)){
                    String newFirstName = this.account_tf_firstName.getText();

                    this.statement.execute(String.format("UPDATE user SET FirstName = '%s' WHERE Username = '%s'", newFirstName, this.currentUsername));
                    this.account_tf_firstName.setText(newFirstName);

                    String name = newFirstName + " " + this.account_tf_lastName.getText();
                    this.account_lbl_name.setText(name.toUpperCase(Locale.ROOT));
                    this.lbl_name.setText(name);
                }

                if (Boolean.TRUE.equals(this.account_lastNameValid)){
                    String newLastName = this.account_tf_lastName.getText();

                    this.statement.execute(String.format("UPDATE user SET LastName = '%s' WHERE Username = '%s'", newLastName, this.currentUsername));
                    this.account_tf_lastName.setText(newLastName);

                    String name = this.account_tf_firstName.getText() + " " + newLastName;
                    this.account_lbl_name.setText(name.toUpperCase(Locale.ROOT));
                    this.lbl_name.setText(name);
                }

                if (Boolean.TRUE.equals(this.account_birthDateValid)){
                    String newBirthDate = this.account_dp_birthDate.getValue().toString();

                    LocalDate birhtdate = this.account_dp_birthDate.getValue();

                    LocalDate now = LocalDate.now();
                    int age = (int) ChronoUnit.YEARS.between(birhtdate, now);

                    this.statement.execute(String.format("UPDATE user SET BirthDate = '%s' WHERE Username = '%s'", newBirthDate, this.currentUsername));
                    this.statement.execute(String.format("UPDATE user SET Age = %d WHERE Username = '%s'", age, this.currentUsername));

                    this.account_lbl_age.setText(String.valueOf(age));
                }

                this.account_resetAccountUserLabel();

                this.loadAccountsTable();
                // reload transaction table

                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Success", "Field(s) successfully changed",
                        "Ok", "close");
            }
        }
    }

    public void account_resetAccountUserLabel(){
        this.account_pl_accountUser.setEffect(null);

        this.account_tf_username.setEditable(false);
        this.account_tf_password.setEditable(false);
        this.account_tf_firstName.setEditable(false);
        this.account_tf_lastName.setEditable(false);
        this.account_lbl_birthDatePatch.setVisible(true);

        this.account_tf_username.getStyleClass().removeIf(style -> style.equals("account-account-user-tf"));
        this.account_tf_password.getStyleClass().removeIf(style -> style.equals("account-account-user-tf"));
        this.account_tf_firstName.getStyleClass().removeIf(style -> style.equals("account-account-user-tf"));
        this.account_tf_lastName.getStyleClass().removeIf(style -> style.equals("account-account-user-tf"));
        this.account_dp_birthDate.getStyleClass().removeIf(style -> style.equals("account-account-user-dp"));

        this.account_button_username.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
        this.account_button_password.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
        this.account_button_firstName.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
        this.account_button_lastName.getStyleClass().removeIf(style -> style.equals("account-button-valid"));
        this.account_button_birthDate.getStyleClass().removeIf(style -> style.equals("account-button-valid"));

        this.account_button_username.setVisible(false);
        this.account_button_password.setVisible(false);
        this.account_button_firstName.setVisible(false);
        this.account_button_lastName.setVisible(false);
        this.account_button_birthDate.setVisible(false);

        account_usernameValid = null;
        account_passwordValid = null;
        account_firstNameValid = null;
        account_lastNameValid = null;
        account_birthDateValid = null;

        account_usernameMessage = "";
        account_passwordMessage = "";
        account_firstNameMessage = "";
        account_lastNameMessage = "";
        account_birthDateMessage = "";


        this.account_ll_accountUser = new LinkedList<>();

        this.account_button_accountDetails.setText("\uE104");
        account_accountUserStart = false;

        this.account_lbl_name.requestFocus();
    }

    public void setAccountLabels() throws SQLException {
        this.rs = this.statement.executeQuery(String.format("""
                SELECT *
                FROM accounts
                INNER JOIN user
                ON accounts.username=user.username
                WHERE accounts.Username = '%s';""", this.currentUsername));

        if (this.rs.next()){
            String name = rs.getString("FirstName") + " " + rs.getString("LastName");
            this.account_lbl_name.setText(name.toUpperCase(Locale.ROOT));
            this.account_lbl_role.setText(rs.getString("Role").toUpperCase(Locale.ROOT));
            this.account_lbl_userID.setText("# " + rs.getString("UserID"));
            this.account_lbl_age.setText(rs.getString("Age"));
            this.account_lbl_sex.setText(rs.getString("Sex"));
            this.account_lbl_dateCreated.setText(rs.getString("DateCreated"));
            this.account_tf_username.setText(rs.getString("Username"));
            this.account_tf_password.setText(rs.getString("Password"));
            this.account_tf_firstName.setText(rs.getString("FirstName"));
            this.account_tf_lastName.setText(rs.getString("LastName"));
            this.account_tf_phone.setText(rs.getString("PhoneNumber"));
            this.account_tf_telephone.setText(rs.getString("Telephone"));
            this.account_tf_email.setText(rs.getString("Email"));
            this.account_tf_province.setText(rs.getString("Province"));
            this.account_tf_city.setText(rs.getString("City"));
            this.account_tf_barangay.setText(rs.getString("Barangay"));
            this.account_tf_street.setText(rs.getString("StreetName"));
            this.account_tf_postalCode.setText(rs.getString("PostalCode"));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(rs.getString("BirthDate"), formatter);
            this.account_dp_birthDate.setValue(localDate);
        }
    }

    public void account_accountUserHoverExit(){
        if (!account_accountUserStart){
            this.account_pl_accountUser.setEffect(null);
        }
    }

    public void account_accountUserHover(){
        if (!account_accountUserStart){
            Effect effect = this.account_pl_name.getEffect();
            this.account_pl_accountUser.setEffect(effect);
        }
    }

    public void accounts_statusStart() throws IOException {
        AccountsTable row = this.accounts_ttv_main.getSelectionModel().getSelectedItem().getValue();

        if (row.getRole().equals("Administrator")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Cannot disable administrator accounts",
                    "Ok", "close");

            this.loadAccountsTable();
        }
    }

    public void accounts_statusCommit(TreeTableColumn.CellEditEvent<AccountsTable, String> event) throws IOException, SQLException {
        AccountsTable row = this.accounts_ttv_main.getSelectionModel().getSelectedItem().getValue();

        if (!event.getOldValue().equals(event.getNewValue())){
            this.statement.execute(String.format("UPDATE accounts SET Status = '%s' WHERE Username = '%s'", event.getNewValue(), row.getUsername()));

            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Success", "Status successfully changed",
                    "Ok", "close");

            this.loadAccountsTable();
        }
    }

    public void accounts_passwordStart() throws IOException {
        AccountsTable row = this.accounts_ttv_main.getSelectionModel().getSelectedItem().getValue();

        if (row.getRole().equals("Administrator") && !row.getUsername().equals(this.currentUsername)){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Cannot edit administrator accounts",
                    "Ok", "close");

            this.loadAccountsTable();
        }
    }

    public void accounts_passwordCommit(TreeTableColumn.CellEditEvent<AccountsTable, String> event) throws IOException, SQLException {
        AccountsTable row = this.accounts_ttv_main.getSelectionModel().getSelectedItem().getValue();

        if (!event.getOldValue().equals(event.getNewValue())){
            if (event.getNewValue().equals("")){
                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Error", "Password cannot be empty",
                        "Ok", "close");
            } else if (event.getNewValue().length() < 8){
                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Error", "Password must have at least 8 characters",
                        "Ok", "close");
            }else{
                this.statement.execute(String.format("UPDATE accounts SET Password = '%s' WHERE Username = '%s'", event.getNewValue(), row.getUsername()));

                this.currentPassword = event.getNewValue();

                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Success", "Password successfully changed",
                        "Ok", "close");

                this.setAccountLabels();
            }

            this.loadAccountsTable();
        }
    }

    public void accounts_usernameStart() throws IOException {
        AccountsTable row = this.accounts_ttv_main.getSelectionModel().getSelectedItem().getValue();

        if (row.getRole().equals("Administrator") && !row.getUsername().equals(this.currentUsername)){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Cannot edit administrator accounts",
                    "Ok", "close");

            this.loadAccountsTable();
        }
    }

    public void accounts_usernameCommit(TreeTableColumn.CellEditEvent<AccountsTable, String> event) throws IOException, SQLException {
        if (!event.getOldValue().equals(event.getNewValue())){
            if (event.getNewValue().equals("")){
                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Error", "username cannot be empty",
                        "Ok", "close");
            } else if (event.getNewValue().length() < 5 || event.getNewValue().length() > 16){
                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Error", "Only between and including 5-16 characters are allowed",
                        "Ok", "close");
            }else{
                this.rs = this.statement.executeQuery(String.format("SELECT * FROM accounts WHERE Username = '%s'", event.getNewValue()));

                if (rs.next()){
                    PopUpApplication popup = new PopUpApplication();
                    popup.start(new Stage());
                    popup.setData(this.stage_this, "Error", "Username is already taken",
                            "Ok", "close");
                }else {
                    this.statement.execute(String.format("UPDATE accounts SET Username = '%s' WHERE Username = '%s'", event.getNewValue(), event.getOldValue()));

                    this.currentUsername = event.getNewValue();

                    PopUpApplication popup = new PopUpApplication();
                    popup.start(new Stage());
                    popup.setData(this.stage_this, "Success", "Username successfully changed",
                            "Ok", "close");

                    this.setAccountLabels();
                }
            }

            this.loadAccountsTable();
        }
    }

    public void accounts_add() throws IOException {
        SignUp2Application s2 = new SignUp2Application();
        s2.start(new Stage());
        s2.setData(this);
    }

    public void accounts_clearStartingDate(){
        this.accounts_dp_startingDate.setValue(null);
    }

    public void accounts_clearEndingDate(){
        this.accounts_dp_endingDate.setValue(null);
    }

    public void accounts_clearSearch(){
        this.accounts_tf_search.clear();
    }

    public void accounts_message(){
        this.accounts_message.setText(this.accounts_messages.get(this.accounts_currentMessageIndex));

        if (accounts_messages.size() == this.accounts_currentMessageIndex + 1)
            this.accounts_currentMessageIndex = 0;
        else
            this.accounts_currentMessageIndex += 1;
    }

    public void clearAccountsSelection(){
        Platform.runLater(() -> this.accounts_ttv_main.getSelectionModel().clearSelection());
    }

    public void accounts_sales() throws IOException {
        if (this.accounts_ttv_main.getSelectionModel().getSelectedItem() == null) {
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Please select a record",
                    "Ok", "close");
        }else {
            AccountsTable row = this.accounts_ttv_main.getSelectionModel().getSelectedItem().getValue();

            this.sales_clearStartingDate();
            this.sales_clearEndingDate();
            this.sales_cb_column.getSelectionModel().select("User ID");
            this.sales_tf_search.setText(row.getUserID());
            this.sales_cb_searchExactMatch.setSelected(true);
            this.searchSales();
            this.show_transactions();
        }
    }

    public void loadAccountsTable(){
        this.accountsRoot.getChildren().clear();

        try{
            rs = statement.executeQuery(this.currentAccountsTableQuery);

            while (rs.next()){
                TreeItem<AccountsTable> column = new TreeItem<>(new AccountsTable(rs.getString("Role"), rs.getString("UserID"),  rs.getString("Username"),
                        rs.getString("Password"), rs.getString("FirstName"), rs.getString("LastName"), rs.getString("PhoneNumber"),
                        rs.getString("Telephone"), rs.getString("Email"), rs.getString("DateCreated"), rs.getString("Status")));

                accountsRoot.getChildren().add(column);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        accounts_col_role.setCellValueFactory(accountsTableStringCellDataFeatures -> accountsTableStringCellDataFeatures.getValue().getValue().roleProperty());
        accounts_col_userID.setCellValueFactory(accountsTableStringCellDataFeatures -> accountsTableStringCellDataFeatures.getValue().getValue().userIDProperty());
        accounts_col_username.setCellValueFactory(accountsTableStringCellDataFeatures -> accountsTableStringCellDataFeatures.getValue().getValue().usernameProperty());
        accounts_col_password.setCellValueFactory(accountsTableStringCellDataFeatures -> accountsTableStringCellDataFeatures.getValue().getValue().passwordProperty());
        accounts_col_firstName.setCellValueFactory(accountsTableStringCellDataFeatures -> accountsTableStringCellDataFeatures.getValue().getValue().firstnameProperty());
        accounts_col_lastName.setCellValueFactory(accountsTableStringCellDataFeatures -> accountsTableStringCellDataFeatures.getValue().getValue().lastnameProperty());
        accounts_col_phoneNumber.setCellValueFactory(accountsTableStringCellDataFeatures -> accountsTableStringCellDataFeatures.getValue().getValue().phoneNumberProperty());
        accounts_col_telephone.setCellValueFactory(accountsTableStringCellDataFeatures -> accountsTableStringCellDataFeatures.getValue().getValue().telephoneProperty());
        accounts_col_email.setCellValueFactory(accountsTableStringCellDataFeatures -> accountsTableStringCellDataFeatures.getValue().getValue().emailProperty());
        accounts_col_dateCreated.setCellValueFactory(accountsTableStringCellDataFeatures -> accountsTableStringCellDataFeatures.getValue().getValue().dateCreatedProperty());
        accounts_col_status.setCellValueFactory(accountsTableStringCellDataFeatures -> accountsTableStringCellDataFeatures.getValue().getValue().statusProperty());

        accounts_col_username.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        accounts_col_password.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        accounts_col_status.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn("Active", "Disabled"));

        this.accounts_ttv_main.setEditable(true);
        this.accounts_ttv_main.setRoot(accountsRoot);
        this.accounts_ttv_main.setShowRoot(false);
    }

    public void searchAccounts(){
        String column = this.accounts_cb_column.getSelectionModel().getSelectedItem();
        String toSearch = this.accounts_tf_search.getText();
        LocalDate date = this.accounts_dp_startingDate.getValue();
        String startingDate = date == null ? null : date.toString();
        date = this.accounts_dp_endingDate.getValue();
        String endingDate = date == null ? null : date.toString();
        boolean isChecked = this.accounts_cb_includeDisabledAccounts.isSelected();
        boolean searchByMatch = this.accounts_cb_searchExactMatch.isSelected();

        String from = "user.";

        if (column.equals("Role") || column.equals("Username") || column.equals("Password") || column.equals("Status")){
            from = "accounts.";
        }

        String sql = """
                SELECT *
                 FROM accounts
                 INNER JOIN user
                 ON accounts.username=user.username WHERE""";

        if (isChecked)
            sql += " accounts.Status LIKE '%%'";
        else
            sql += " accounts.Status NOT LIKE '%Disabled%'";

        if (startingDate != null)
            sql += String.format(" AND accounts.DateCreated >= '%s'", startingDate.replaceAll(" ", ""));

        if (endingDate != null)
            sql += String.format(" AND accounts.DateCreated <= '%s'", endingDate.replaceAll(" ", ""));

        if (!toSearch.equals("")){
            if (toSearch.toLowerCase(Locale.ROOT).equals("Null".toLowerCase(Locale.ROOT))){
                sql += String.format(" AND %s%s = '' OR %s IS NULL", from, column.replaceAll(" ", ""), column.replaceAll(" ", ""));
            }else {
                if (searchByMatch){
                    sql += String.format(" AND %s%s = '%s'", from, column.replaceAll(" ", ""), toSearch.trim());
                }else{
                    sql += String.format(" AND %s%s LIKE '%%%s%%'", from,  column.replaceAll(" ", ""), toSearch.trim());
                }
            }
        }

        this.currentAccountsTableQuery = sql;

        this.loadAccountsTable();
    }

    public void clearVendorsSelection(){
        Platform.runLater(() -> this.vendors_ttv_main.getSelectionModel().clearSelection());
    }

    public void vendors_expenses() throws IOException {
        if (this.vendors_ttv_main.getSelectionModel().getSelectedItem() == null){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Please select a record",
                    "Ok", "close");
        }else {
            VendorsTable row = this.vendors_ttv_main.getSelectionModel().getSelectedItem().getValue();

            this.expenses_tf_search.setText(row.getCompanyName());
            this.expenses_cb_column.getSelectionModel().select("Company Name");
            this.expenses_clearStartingDate();
            this.expenses_clearEndingDate();
            this.expenses_cb_includeCancelledRecords.setSelected(true);
            this.expenses_cb_searchExactMatch.setSelected(true);
            this.search();
            this.show_expenses();
        }
    }

    public void vendors_websiteCommit(TreeTableColumn.CellEditEvent<VendorsTable, String> event) throws IOException, SQLException {
        VendorsTable row = this.vendors_ttv_main.getSelectionModel().getSelectedItem().getValue();

        if (!event.getOldValue().equals(event.getNewValue())){
            if (event.getNewValue().equals("")){
                this.statement.execute(String.format("UPDATE vendors SET Website = '%s' WHERE CompanyName = '%s'",
                        event.getNewValue(), row.getCompanyName()));

                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Success", "Website successfully changed",
                        "Ok", "close");
            } else if (!event.getNewValue().contains(".")){
                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Error", "Website is invalid",
                        "Ok", "close");
            }else if (event.getNewValue().length() < 5){
                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Error", "Website is invalid",
                        "Ok", "close");
            }else{
                this.statement.execute(String.format("UPDATE vendors SET Website = '%s' WHERE CompanyName = '%s'",
                        event.getNewValue(), row.getCompanyName()));

                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Success", "Website successfully changed",
                        "Ok", "close");
            }
        }

        this.loadVendorsTable();
    }

    public void vendors_phoneNumberCommit(TreeTableColumn.CellEditEvent<VendorsTable, String> event) throws IOException, SQLException {
        VendorsTable row = this.vendors_ttv_main.getSelectionModel().getSelectedItem().getValue();

        if (!event.getOldValue().equals(event.getNewValue())){
            if (event.getNewValue().equals("") && row.getWorkPhone().equals("")){
                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Error", "Either work-phone or phone-number must keep filled at all times",
                        "Ok", "close");
            }else if (event.getNewValue().equals("") && !row.getWorkPhone().equals("")){
                this.statement.execute(String.format("UPDATE vendors SET PhoneNumber = '%s' WHERE CompanyName = '%s'",
                        event.getNewValue(), row.getCompanyName()));

                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Success", "Phone-number successfully changed",
                        "Ok", "close");
            }else {
                try{
                    Long.parseLong(event.getNewValue());
                }catch (Exception e){
                    PopUpApplication popup = new PopUpApplication();
                    popup.start(new Stage());
                    popup.setData(this.stage_this, "Error", "Phone-number must only consist of digits",
                            "Ok", "close");
                    this.loadVendorsTable();
                    return;
                }

                if (event.getNewValue().length() != 11){
                    PopUpApplication popup = new PopUpApplication();
                    popup.start(new Stage());
                    popup.setData(this.stage_this, "Error", "Phone-number must contain 11 digits",
                            "Ok", "close");
                }else {
                    this.statement.execute(String.format("UPDATE vendors SET PhoneNumber = '%s' WHERE CompanyName = '%s'",
                            event.getNewValue(), row.getCompanyName()));

                    PopUpApplication popup = new PopUpApplication();
                    popup.start(new Stage());
                    popup.setData(this.stage_this, "Success", "Phone-number successfully changed",
                            "Ok", "close");
                }
            }
        }

        this.loadVendorsTable();
    }

    public void vendors_workPhoneCommit(TreeTableColumn.CellEditEvent<VendorsTable, String> event) throws IOException, SQLException {
        VendorsTable row = this.vendors_ttv_main.getSelectionModel().getSelectedItem().getValue();

        if (!event.getOldValue().equals(event.getNewValue())){
            if (event.getNewValue().equals("") && row.getPhoneNumber().equals("")){
                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Error", "Either work-phone or phone-number must keep filled at all times",
                        "Ok", "close");
            }else {
                this.statement.execute(String.format("UPDATE vendors SET WorkPhone = '%s' WHERE CompanyName = '%s'",
                        event.getNewValue(), row.getCompanyName()));

                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Success", "Work-phone successfully changed",
                        "Ok", "close");
            }
        }

        this.loadVendorsTable();
    }

    public void vendors_emailCommit(TreeTableColumn.CellEditEvent<VendorsTable, String> event) throws IOException, SQLException {
        VendorsTable row = this.vendors_ttv_main.getSelectionModel().getSelectedItem().getValue();

        if (!event.getOldValue().equals(event.getNewValue())){
            if (event.getNewValue().equals("")){
                this.statement.execute(String.format("UPDATE vendors SET Email = '%s' WHERE CompanyName = '%s'",
                        event.getNewValue(), row.getCompanyName()));

                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Success", "Email successfully changed",
                        "Ok", "close");
            } else if (!event.getNewValue().contains("@") || !event.getNewValue().contains(".")){
                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Error", "Email is invalid",
                        "Ok", "close");
            }else if (event.getNewValue().length() < 10){
                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Error", "Email is invalid",
                        "Ok", "close");
            }else{
                this.statement.execute(String.format("UPDATE vendors SET Email = '%s' WHERE CompanyName = '%s'",
                        event.getNewValue(), row.getCompanyName()));

                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Success", "Email successfully changed",
                        "Ok", "close");
            }
        }

        this.loadVendorsTable();
    }

    public void vendors_displayNameCommit(TreeTableColumn.CellEditEvent<VendorsTable, String> event) throws IOException, SQLException {
        VendorsTable row = this.vendors_ttv_main.getSelectionModel().getSelectedItem().getValue();

        if (!event.getOldValue().equals(event.getNewValue())){
            this.statement.execute(String.format("UPDATE vendors SET displayName = '%s' WHERE CompanyName = '%s'",
                    event.getNewValue(), row.getCompanyName()));

            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Success", "Display name successfully changed",
                    "Ok", "close");

            this.loadVendorsTable();

        }
    }

    public void vendors_lastNameStart() throws IOException {
        VendorsTable row = this.vendors_ttv_main.getSelectionModel().getSelectedItem().getValue();
        this.vendors_selected_companyName = row.getCompanyName();

        if (row.getLastName().equals("")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Only records with values can be edited",
                    "Ok", "close");

            this.loadVendorsTable();
        }
    }

    public void vendors_lastNameCommit(TreeTableColumn.CellEditEvent<VendorsTable, String> event) throws IOException, SQLException {
        VendorsTable row = this.vendors_ttv_main.getSelectionModel().getSelectedItem().getValue();

        if (!event.getOldValue().equals(event.getNewValue())){
            if (event.getNewValue().equals("")){
                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Confirmation", "Clearing the first name will also clear the last name. Continue?",
                        "Continue", "vendor name");
            }else {
                this.statement.execute(String.format("UPDATE vendors SET lastName = '%s' WHERE CompanyName = '%s'",
                        event.getNewValue(), row.getCompanyName()));

                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Success", "Firstname successfully changed",
                        "Ok", "close");

                this.loadVendorsTable();
            }
        }
    }

    public void vendors_firstNameStart() throws IOException {
        VendorsTable row = this.vendors_ttv_main.getSelectionModel().getSelectedItem().getValue();
        this.vendors_selected_companyName = row.getCompanyName();

        if (row.getFirstName().equals("")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Only records with values can be edited",
                    "Ok", "close");

            this.loadVendorsTable();
        }
    }

    public void vendors_firstNameCommit(TreeTableColumn.CellEditEvent<VendorsTable, String> event) throws IOException, SQLException {
        VendorsTable row = this.vendors_ttv_main.getSelectionModel().getSelectedItem().getValue();

        if (!event.getOldValue().equals(event.getNewValue())){
            if (event.getNewValue().equals("")){
                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Confirmation", "Clearing the first name will also clear the last name. Continue?",
                        "Continue", "vendor name");
            }else {
                this.statement.execute(String.format("UPDATE vendors SET firstName = '%s' WHERE CompanyName = '%s'",
                        event.getNewValue(), row.getCompanyName()));

                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Success", "Firstname successfully changed",
                        "Ok", "close");

                this.loadVendorsTable();
            }
        }
    }

    public void vendors_clearSearch(){
        this.vendors_tf_search.clear();
    }

    public void vendors_message(){
        this.vendors_message.setText(this.vendors_messages.get(this.vendors_currentMessageIndex));

        if (vendors_messages.size() == this.vendors_currentMessageIndex + 1)
            this.vendors_currentMessageIndex = 0;
        else
            this.vendors_currentMessageIndex += 1;
    }

    public void searchVendors(){
        String column = this.vendors_cb_column.getSelectionModel().getSelectedItem();
        String toSearch = this.vendors_tf_search.getText();

        boolean searchByMatch = this.vendors_cb_searchExactMatch.isSelected();

        String sql = "SELECT * FROM vendors";

        if (!toSearch.equals("")){
            if (toSearch.toLowerCase(Locale.ROOT).equals("Null".toLowerCase(Locale.ROOT))){
                sql += String.format(" WHERE %s = '' OR %s IS NULL", column.replaceAll(" ", ""), column.replaceAll(" ", ""));
            }else {
                if (searchByMatch){
                    sql += String.format(" WHERE %s = '%s'", column.replaceAll(" ", ""), toSearch.trim());
                }else{
                    sql += String.format(" WHERE %s LIKE '%%%s%%'", column.replaceAll(" ", ""), toSearch.trim());
                }
            }
        }

        this.currentVendorsTableQuery = sql;

        this.loadVendorsTable();
    }

    public void loadVendorsTable(){
        this.vendorsRoot.getChildren().clear();

        try{
            rs = statement.executeQuery(this.currentVendorsTableQuery);

            while (rs.next()){
                TreeItem<VendorsTable> column = new TreeItem<>(new VendorsTable(rs.getString("CompanyName"), rs.getString("FirstName"),
                        rs.getString("LastName"), rs.getString("DisplayName"), rs.getString("Email"), rs.getString("WorkPhone"),
                        rs.getString("PhoneNumber"), rs.getString("Website")));

                vendorsRoot.getChildren().add(column);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        vendors_col_companyName.setCellValueFactory(vendorsTableStringCellDataFeatures -> vendorsTableStringCellDataFeatures.getValue().getValue().companyNameProperty());
        vendors_col_firstName.setCellValueFactory(vendorsTableStringCellDataFeatures -> vendorsTableStringCellDataFeatures.getValue().getValue().firstNameProperty());
        vendors_col_lastName.setCellValueFactory(vendorsTableStringCellDataFeatures -> vendorsTableStringCellDataFeatures.getValue().getValue().lastNameProperty());
        vendors_col_displayName.setCellValueFactory(vendorsTableStringCellDataFeatures -> vendorsTableStringCellDataFeatures.getValue().getValue().displayNameProperty());
        vendors_col_email.setCellValueFactory(vendorsTableStringCellDataFeatures -> vendorsTableStringCellDataFeatures.getValue().getValue().emailProperty());
        vendors_col_workPhone.setCellValueFactory(vendorsTableStringCellDataFeatures -> vendorsTableStringCellDataFeatures.getValue().getValue().workPhoneProperty());
        vendors_col_phoneNumber.setCellValueFactory(vendorsTableStringCellDataFeatures -> vendorsTableStringCellDataFeatures.getValue().getValue().phoneNumberProperty());
        vendors_col_website.setCellValueFactory(vendorsTableStringCellDataFeatures -> vendorsTableStringCellDataFeatures.getValue().getValue().websiteProperty());

        vendors_col_companyName.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        vendors_col_firstName.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        vendors_col_lastName.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        vendors_col_displayName.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        vendors_col_email.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        vendors_col_workPhone.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        vendors_col_phoneNumber.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        vendors_col_website.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());

        this.vendors_ttv_main.setEditable(true);
        this.vendors_ttv_main.setRoot(vendorsRoot);
        this.vendors_ttv_main.setShowRoot(false);
    }

    public void inventory_expenses() throws IOException {
        if (this.inventory_ttv_main.getSelectionModel().getSelectedItem() == null){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Please select a record",
                    "Ok", "close");
        }else {
            InventoryTable row = this.inventory_ttv_main.getSelectionModel().getSelectedItem().getValue();

            this.expenses_tf_search.setText(row.getExpenseTitle());
            this.expenses_cb_column.getSelectionModel().select("Expense Title");
            this.expenses_clearStartingDate();
            this.expenses_clearEndingDate();
            this.expenses_cb_includeCancelledRecords.setSelected(true);
            this.expenses_cb_searchExactMatch.setSelected(true);
            this.search();
            this.show_expenses();
        }
    }

    public void inventory_priceStart() throws IOException {
        InventoryTable row = this.inventory_ttv_main.getSelectionModel().getSelectedItem().getValue();

        if (row.getPrice() == null || row.getPrice().equals("")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Products measured by amount cannot have price. Price are set on transaction",
                    "Ok", "close");

            this.loadInventoryTable();
        }
    }

    public void inventory_priceCommit(TreeTableColumn.CellEditEvent<InventoryTable, String> event) throws IOException, SQLException {
        InventoryTable row = this.inventory_ttv_main.getSelectionModel().getSelectedItem().getValue();

        if (event.getNewValue().equals("")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "This field cannot be empty",
                    "Ok", "close");
        }else if (!event.getOldValue().equals(event.getNewValue())){
            try{
                Double.parseDouble(event.getNewValue());
            }catch (Exception e){
                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Error", "Price must only consist of digits",
                        "Ok", "close");
                this.loadInventoryTable();
                return;
            }

            if (Double.parseDouble(event.getNewValue()) < 1){
                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Error", "Price cannot be lower than 1",
                        "Ok", "close");
            }else {
                // change delivery fees
                this.statement.execute(String.format("UPDATE inventory SET Price = %.2f WHERE ProductID = '%s'", Double.parseDouble(event.getNewValue()), row.getProductID()));

                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Success", "Price successfully changed",
                        "Ok", "close");
            }
        }

        this.loadInventoryTable();
    }

    public void inventory_variantCommit(TreeTableColumn.CellEditEvent<InventoryTable, String> event) throws IOException, SQLException {
        InventoryTable row = this.inventory_ttv_main.getSelectionModel().getSelectedItem().getValue();

        if (event.getNewValue().equals("")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "This field cannot be empty",
                    "Ok", "close");
        }else if (!event.getOldValue().equals(event.getNewValue())){
            // check existing record
            this.rs = this.statement.executeQuery(String.format("SELECT * FROM inventory WHERE ProductType = '%s' AND ProductName = '%s' AND Variant = '%s'",
                    row.getProductType(), row.getProductName(), event.getNewValue()));

            if (rs.next()){
                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Error", "Existing record.",
                        "Ok", "close");
            }else {
                this.statement.execute(String.format("UPDATE inventory SET Variant = '%s' WHERE productID = '%s'",
                        event.getNewValue(), row.getProductID()));

                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Success", "Variant successfully changed",
                        "Ok", "close");
            }
        }

        this.loadInventoryTable();
    }

    public void inventory_productNameCommit(TreeTableColumn.CellEditEvent<InventoryTable, String> event) throws IOException, SQLException {
        InventoryTable row = this.inventory_ttv_main.getSelectionModel().getSelectedItem().getValue();

        if (event.getNewValue().equals("")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "This field cannot be empty",
                    "Ok", "close");
        }else if (!event.getOldValue().equals(event.getNewValue())){
            // check existing record
            this.rs = this.statement.executeQuery(String.format("SELECT * FROM inventory WHERE ProductType = '%s' AND ProductName = '%s' AND Variant = '%s'",
                    row.getProductType(), event.getNewValue(), row.getVariant()));

            if (rs.next()){
                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Error", "Existing record.",
                        "Ok", "close");
            }else {
                this.statement.execute(String.format("UPDATE inventory SET ProductName = '%s' WHERE productID = '%s'",
                        event.getNewValue(), row.getProductID()));

                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Success", "Product name successfully changed",
                        "Ok", "close");
            }
        }

        this.loadSalesTable();
        this.loadInventoryTable();
        this.dataChanged();
    }

    public void inventory_productTypeCommit(TreeTableColumn.CellEditEvent<InventoryTable, String> event) throws IOException, SQLException {
        InventoryTable row = this.inventory_ttv_main.getSelectionModel().getSelectedItem().getValue();

        if (event.getNewValue().equals("")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "This field cannot be empty",
                    "Ok", "close");
        }else if (!event.getOldValue().equals(event.getNewValue())){
            // check existing record
            this.rs = this.statement.executeQuery(String.format("SELECT * FROM inventory WHERE ProductType = '%s' AND ProductName = '%s' AND Variant = '%s'",
                    event.getNewValue(), row.getProductName(), row.getVariant()));

            if (rs.next()){
                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Error", "Existing record.",
                        "Ok", "close");
            }else {
                this.statement.execute(String.format("UPDATE inventory SET ProductType = '%s' WHERE productID = '%s'",
                        event.getNewValue(), row.getProductID()));

                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Success", "Product type successfully changed",
                        "Ok", "close");
            }
        }

        this.loadInventoryTable();
    }

    public void inventory_expirationDateStart() throws IOException {
        InventoryTable row = this.inventory_ttv_main.getSelectionModel().getSelectedItem().getValue();

        if (row.getExpirationDate() == null || String.valueOf(row.getExpirationDate()).equals("")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Cannot change date of empty expiration date",
                    "Ok", "close");

            this.loadInventoryTable();
        }
    }

    public void inventory_expirationDateCommit(TreeTableColumn.CellEditEvent<InventoryTable, Date> event) throws SQLException, IOException {
        InventoryTable row = this.inventory_ttv_main.getSelectionModel().getSelectedItem().getValue();

        this.statement.execute(String.format("UPDATE inventory SET ExpirationDate = '%s' WHERE ProductID = '%s'",
                event.getNewValue(), row.getProductID()));

        PopUpApplication popup = new PopUpApplication();
        popup.start(new Stage());
        popup.setData(this.stage_this, "Success", "Expiration Date successfully changed",
                "Ok", "close");

        this.loadInventoryTable();
    }

    public void showProductImage() throws IOException, SQLException {
        if (this.inventory_ttv_main.getSelectionModel().getSelectedItem() != null){
            TreeItem<InventoryTable> selectedRow = this.inventory_ttv_main.getSelectionModel().getSelectedItem();
            InventoryTable record = selectedRow.getValue();

            ShowProductApplication application = new ShowProductApplication();
            application.start(new Stage());
            application.setData(record.getProductID(), record.getExpenseTitle());
        }else {
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Please select a record in the table", "Ok", "close");
        }
    }

    public void inventory_add() throws IOException {
        Vendor0Application vendor0 = new Vendor0Application();
        vendor0.start(new Stage());
        vendor0.setData(this, "inventory");
    }

    public void inventory_message(){
        this.inventory_message.setText(this.inventory_messages.get(this.inventory_currentMessageIndex));

        if (inventory_messages.size() == this.inventory_currentMessageIndex + 1)
            this.inventory_currentMessageIndex = 0;
        else
            this.inventory_currentMessageIndex += 1;
    }

    public void inventory_clearStartingDate(){
        this.inventory_dp_startingDate.setValue(null);
    }

    public void inventory_clearEndingDate(){
        this.inventory_dp_endingDate.setValue(null);
    }

    public void inventory_clearSearch(){
        this.inventory_tf_search.clear();
    }

    public void clearInventorySelection(){
        Platform.runLater(() -> this.inventory_ttv_main.getSelectionModel().clearSelection());
    }

    public void inventory_sales() throws IOException {
        if (this.inventory_ttv_main.getSelectionModel().getSelectedItem() == null) {
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Please select a record",
                    "Ok", "close");
        }else {
            InventoryTable row = this.inventory_ttv_main.getSelectionModel().getSelectedItem().getValue();

            this.sales_clearStartingDate();
            this.sales_clearEndingDate();
            this.sales_cb_column.getSelectionModel().select("Product");
            this.sales_tf_search.setText(row.getExpenseTitle());
            this.sales_cb_searchExactMatch.setSelected(true);
            this.searchSales();
            this.show_transactions();
        }
    }

    public void searchInventory(){
        String column = this.inventory_cb_column.getSelectionModel().getSelectedItem();
        String toSearch = this.inventory_tf_search.getText();
        LocalDate date = this.inventory_dp_startingDate.getValue();
        String startingDate = date == null ? null : date.toString();
        date = this.inventory_dp_endingDate.getValue();
        String endingDate = date == null ? null : date.toString();
        boolean isChecked = this.inventory_cb_includeCancelledRecords.isSelected();
        boolean searchByMatch = this.inventory_cb_searchExactMatch.isSelected();
        String dateColumn = this.inventory_cb_dateColumn.getSelectionModel().getSelectedItem();

        String sql = "SELECT * FROM inventory WHERE";

        if (isChecked)
            sql += " Status LIKE '%%'";
        else
            sql += " Status NOT LIKE '%Inactive%'";

        if (startingDate != null)
            sql += String.format(" AND %s >= '%s'", dateColumn.replaceAll(" ", ""), startingDate.replaceAll(" ", ""));

        if (endingDate != null)
            sql += String.format(" AND %s <= '%s'", dateColumn.replaceAll(" ", ""), endingDate.replaceAll(" ", ""));

        if (!toSearch.equals("")){
            if (toSearch.toLowerCase(Locale.ROOT).equals("Null".toLowerCase(Locale.ROOT))){
                sql += String.format(" AND %s = '' OR %s IS NULL", column.replaceAll(" ", ""), column.replaceAll(" ", ""));
            }else {
                if (searchByMatch){
                    sql += String.format(" AND %s = '%s'", column.replaceAll(" ", ""), toSearch.trim());
                }else{
                    sql += String.format(" AND %s LIKE '%%%s%%'", column.replaceAll(" ", ""), toSearch.trim());
                }
            }
        }

        this.currentInventoryTableQuery = sql;

        this.loadInventoryTable();
    }

    public void loadInventoryTable(){
        this.inventoryRoot.getChildren().clear();

        try{
            rs = statement.executeQuery(this.currentInventoryTableQuery);

            while (rs.next()){
                TreeItem<InventoryTable> column = new TreeItem<>(new InventoryTable(rs.getString("ProductID"), rs.getString("ExpenseNumber"),
                        rs.getString("ExpenseTitle"), rs.getString("ProductType"), rs.getString("ProductName"), rs.getString("Variant"),
                        rs.getString("Stock"), rs.getString("Price"), rs.getString("Description"), rs.getString("ExpirationDate"),
                        rs.getString("CriticalLevel"), rs.getString("DateAdded"), rs.getString("Status")));

                inventoryRoot.getChildren().add(column);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        inventory_col_productID.setCellValueFactory(inventoryTableStringCellDataFeatures -> inventoryTableStringCellDataFeatures.getValue().getValue().productIDProperty());
        inventory_col_expenseNumber.setCellValueFactory(inventoryTableStringCellDataFeatures -> inventoryTableStringCellDataFeatures.getValue().getValue().expenseNumberProperty());
        inventory_col_expenseTitle.setCellValueFactory(inventoryTableStringCellDataFeatures -> inventoryTableStringCellDataFeatures.getValue().getValue().expenseTitleProperty());
        inventory_col_productType.setCellValueFactory(inventoryTableStringCellDataFeatures -> inventoryTableStringCellDataFeatures.getValue().getValue().productTypeProperty());
        inventory_col_productName.setCellValueFactory(inventoryTableStringCellDataFeatures -> inventoryTableStringCellDataFeatures.getValue().getValue().productNameProperty());
        inventory_col_variant.setCellValueFactory(inventoryTableStringCellDataFeatures -> inventoryTableStringCellDataFeatures.getValue().getValue().variantProperty());
        inventory_col_stock.setCellValueFactory(inventoryTableStringCellDataFeatures -> inventoryTableStringCellDataFeatures.getValue().getValue().stockProperty());
        inventory_col_price.setCellValueFactory(inventoryTableStringCellDataFeatures -> inventoryTableStringCellDataFeatures.getValue().getValue().priceProperty());
        inventory_col_description.setCellValueFactory(inventoryTableStringCellDataFeatures -> inventoryTableStringCellDataFeatures.getValue().getValue().descriptionProperty());
        inventory_col_expirationDate.setCellValueFactory(inventoryTableDateCellDataFeatures -> inventoryTableDateCellDataFeatures.getValue().getValue().expirationDateProperty());
        inventory_col_criticalLevel.setCellValueFactory(inventoryTableStringCellDataFeatures -> inventoryTableStringCellDataFeatures.getValue().getValue().criticalLevelProperty());
        inventory_col_dateAdded.setCellValueFactory(inventoryTableStringCellDataFeatures -> inventoryTableStringCellDataFeatures.getValue().getValue().dateAddedProperty());
        inventory_col_status.setCellValueFactory(inventoryTableStringCellDataFeatures -> inventoryTableStringCellDataFeatures.getValue().getValue().statusProperty());

        if (!this.role.equals("Employee")){
            inventory_col_expenseTitle.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
            inventory_col_productType.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
            inventory_col_productName.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
            inventory_col_variant.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
            inventory_col_price.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
            inventory_col_expirationDate.setCellFactory(this.inventory_dateCellFactory);
        }


        this.inventory_ttv_main.setEditable(true);
        this.inventory_ttv_main.setRoot(inventoryRoot);
        this.inventory_ttv_main.setShowRoot(false);
    }

    public void expenses_vendors() throws IOException {
        if (this.expenses_ttv_main.getSelectionModel().getSelectedItem() == null){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Please select a record",
                    "Ok", "close");
        }else {
            ExpensesTable row = this.expenses_ttv_main.getSelectionModel().getSelectedItem().getValue();

            this.vendors_cb_column.getSelectionModel().select("Company Name");
            this.vendors_tf_search.setText(row.getCompanyName());
            this.vendors_cb_searchExactMatch.setSelected(true);
            this.searchVendors();
            this.show_vendors();
        }
    }

    public void expenses_inventory() throws IOException {
        if (this.expenses_ttv_main.getSelectionModel().getSelectedItem() == null){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Please select a record",
                    "Ok", "close");
        }else if (!this.expenses_ttv_main.getSelectionModel().getSelectedItem().getValue().getType().equals("Product")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Item is not of type product",
                    "Ok", "close");
        }else {
            ExpensesTable row = this.expenses_ttv_main.getSelectionModel().getSelectedItem().getValue();

            this.inventory_clearStartingDate();
            this.inventory_clearEndingDate();
            this.inventory_cb_column.getSelectionModel().select("Expense Title");
            this.inventory_tf_search.setText(row.getExpenseTitle());
            this.inventory_cb_includeCancelledRecords.setSelected(true);
            this.inventory_cb_searchExactMatch.setSelected(true);
            this.searchInventory();
            this.show_inventory();
        }
    }

    public void expenses_paymentDateStart() throws IOException {
        ExpensesTable row = this.expenses_ttv_main.getSelectionModel().getSelectedItem().getValue();

        this.expenses_selectedExpenseNumber = row.getExpenseNumber();
        this.currentRow = row;

        if (row.getStatus().equals("Cancelled")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Cannot change this cell when the record was cancelled",
                    "Ok", "close");

            loadExpensesTable();
        }
    }

    public void expenses_paymentDateCommit(TreeTableColumn.CellEditEvent<ExpensesTable, Date> event) throws IOException, SQLException {
        this.expenses_newPaymentDate = String.valueOf(event.getNewValue());

        this.statement.execute(String.format("UPDATE expenses SET PaymentDate = '%s' WHERE ExpenseNumber = '%s'", event.getNewValue(),
                this.currentRow.getExpenseNumber()));

        if (currentRow.getStatus().equals("Transaction on Process") && currentRow.getPaymentTerms().equals("Cash on Delivery")) {
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Success", "Date delivered and status will also be updated. Continue?",
                    "Continue", "payment date set");
        }else if (currentRow.getStatus().equals("Transaction Completed") && currentRow.getPaymentTerms().equals("Cash on Delivery")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Success", "Date delivered will also be updated. Continue?",
                    "Continue", "payment date change");
        }else {
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
                popup.setData(this.stage_this, "Success", "Payment date successfully changed",
                    "Ok", "close");
        }

        this.loadExpensesTable();
    }

    public void expenses_expectedDeliveryDateStart() throws IOException {
        ExpensesTable row = this.expenses_ttv_main.getSelectionModel().getSelectedItem().getValue();

        this.expenses_selectedExpenseNumber = row.getExpenseNumber();

        if (row.getStatus().equals("Cancelled")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Cannot change this cell when the record was cancelled",
                    "Ok", "close");

            loadExpensesTable();
        }else if (row.getPaymentTerms().equals("Due on Receipt")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Due on receipt items cannot have a value for this field",
                    "Ok", "close");

            loadExpensesTable();
        }
    }

    public void expenses_expectedDeliveryDateCommit(TreeTableColumn.CellEditEvent<ExpensesTable, Date> event) throws IOException, SQLException {
        this.statement.execute(String.format("UPDATE expenses SET ExpectedDeliveryDate = '%s' WHERE ExpenseNumber = '%s'", event.getNewValue(),
                this.expenses_selectedExpenseNumber));

        PopUpApplication popup = new PopUpApplication();
        popup.start(new Stage());
        popup.setData(this.stage_this, "Success", "Expected delivery date successfully changed",
                "Ok", "close");

        this.loadExpensesTable();
    }

    public void expenses_expectedDeliveryDateCancel(){
        System.out.println("3");
    }

    public void expenses_deliveryFeesStart() throws IOException {
        ExpensesTable row = this.expenses_ttv_main.getSelectionModel().getSelectedItem().getValue();

        this.expenses_selectedExpenseNumber = row.getExpenseNumber();

        if (row.getStatus().equals("Cancelled")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Cannot edit this field when the record was cancelled",
                    "Ok", "close");

            this.loadExpensesTable();
        }else if (row.getPaymentTerms().equals("Due on Receipt")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Due on receipt transactions cannot have delivery details",
                    "Ok", "close");

            this.loadExpensesTable();
        }
    }

    public void expenses_deliveryFeesCommit(TreeTableColumn.CellEditEvent<ExpensesTable, String> event) throws IOException, SQLException {

        if (event.getNewValue().equals("")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "This field cannot be empty",
                    "Ok", "close");
        }else if (!event.getOldValue().equals(event.getNewValue())){
            try{
                Double.parseDouble(event.getNewValue());
            }catch (Exception e){
                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Error", "Delivery fees must only consist of digits",
                        "Ok", "close");
                this.loadExpensesTable();
                return;
            }

            if (Double.parseDouble(event.getNewValue()) < 0){
                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Error", "Delivery fees cannot be lower than 0",
                        "Ok", "close");
            }else {
                // change delivery fees
                this.statement.execute(String.format("UPDATE expenses SET DeliveryFees = %.2f WHERE ExpenseNumber = '%s'", Double.parseDouble(event.getNewValue()), this.expenses_selectedExpenseNumber));

                // change total item cost
                double forTotalExpenseCost = Double.parseDouble(event.getNewValue()) - Double.parseDouble(event.getOldValue()); // 25 5 30 = 25 3 28, 3-5 = -2 = 30 + - 2
                this.statement.execute(String.format("UPDATE expenses SET TotalExpenseCost = TotalExpenseCost + %.2f WHERE ExpenseNumber = '%s'", forTotalExpenseCost, this.expenses_selectedExpenseNumber));

                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Success", "Delivery fees and total expense cost successfully changed",
                        "Ok", "close");
            }
        }

        this.loadExpensesTable();
        this.dataChanged();
    }

    public void expenses_deliveryFeesCancel(){

    }

    public void expenses_trackingNumberStart() throws IOException {
        ExpensesTable row = this.expenses_ttv_main.getSelectionModel().getSelectedItem().getValue();

        this.expenses_selectedExpenseNumber = row.getExpenseNumber();

        if (row.getStatus().equals("Cancelled")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Cannot edit this field when the record was cancelled",
                    "Ok", "close");

            this.loadExpensesTable();
        }else if (row.getPaymentTerms().equals("Due on Receipt")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Due on receipt transactions cannot have delivery details",
                    "Ok", "close");

            this.loadExpensesTable();
        }
    }

    public void expenses_trackingNumberCommit(TreeTableColumn.CellEditEvent<ExpensesTable, String> event) throws IOException, SQLException {
        if (!event.getOldValue().equals(event.getNewValue())){
            this.statement.execute(String.format("UPDATE expenses SET TrackingNumber = '%s' WHERE ExpenseNumber = '%s'", event.getNewValue(), this.expenses_selectedExpenseNumber));

            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Success", "Field successfully changed",
                    "Ok", "close");
        }

        this.loadExpensesTable();
    }

    public void expenses_trackingNumberCancel(){

    }

    public void expenses_shipmentPreferenceStart() throws IOException {
        ExpensesTable row = this.expenses_ttv_main.getSelectionModel().getSelectedItem().getValue();

        this.expenses_selectedExpenseNumber = row.getExpenseNumber();

        if (row.getStatus().equals("Cancelled")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Cannot edit this field when the record was cancelled",
                    "Ok", "close");

            this.loadExpensesTable();
        }else if (row.getPaymentTerms().equals("Due on Receipt")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Due on receipt transactions cannot have delivery details",
                    "Ok", "close");

            this.loadExpensesTable();
        }
    }

    public void expenses_shipmentPreferenceCommit(TreeTableColumn.CellEditEvent<ExpensesTable, String> event) throws IOException, SQLException {
        if (event.getNewValue().equals("")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "This field cannot be empty",
                    "Ok", "close");
        }else if (!event.getOldValue().equals(event.getNewValue())){
            this.statement.execute(String.format("UPDATE expenses SET ShipmentPreference = '%s' WHERE ExpenseNumber = '%s'", event.getNewValue(), this.expenses_selectedExpenseNumber));

            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Success", "Field successfully changed",
                    "Ok", "close");
        }

        this.loadExpensesTable();
    }

    public void expenses_shipmentPreferenceCancel(){

    }

    public void expense_deliveredFromStart() throws IOException {
        ExpensesTable row = this.expenses_ttv_main.getSelectionModel().getSelectedItem().getValue();

        this.expenses_selectedExpenseNumber = row.getExpenseNumber();

        if (row.getStatus().equals("Cancelled")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Cannot edit this field when the record was cancelled",
                    "Ok", "close");

            this.loadExpensesTable();
        }else if (row.getPaymentTerms().equals("Due on Receipt")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Due on receipt transactions cannot have delivery details",
                    "Ok", "close");

            this.loadExpensesTable();
        }
    }

    public void expense_deliveredFromCommit(TreeTableColumn.CellEditEvent<ExpensesTable, String> event) throws IOException, SQLException {
        if (event.getNewValue().equals("")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "This field cannot be empty",
                    "Ok", "close");
        }else if (!event.getOldValue().equals(event.getNewValue())){
            this.statement.execute(String.format("UPDATE expenses SET DeliveredFrom = '%s' WHERE ExpenseNumber = '%s'", event.getNewValue(), this.expenses_selectedExpenseNumber));

            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Success", "Field successfully changed",
                    "Ok", "close");
        }

        this.loadExpensesTable();
    }

    public void expense_deliveredFromCancel(){

    }

    public void expense_deliverToStart() throws IOException {
        ExpensesTable row = this.expenses_ttv_main.getSelectionModel().getSelectedItem().getValue();

        this.expenses_selectedExpenseNumber = row.getExpenseNumber();

        if (row.getStatus().equals("Cancelled")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Cannot edit this field when the record was cancelled",
                    "Ok", "close");

            this.loadExpensesTable();
        }else if (row.getPaymentTerms().equals("Due on Receipt")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Due on receipt transactions cannot have delivery details",
                    "Ok", "close");

            this.loadExpensesTable();
        }
    }

    public void expense_deliverToCommit(TreeTableColumn.CellEditEvent<ExpensesTable, String> event) throws IOException, SQLException {
        if (event.getNewValue().equals("")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "This field cannot be empty",
                    "Ok", "close");
        }else if (!event.getOldValue().equals(event.getNewValue())){
            this.statement.execute(String.format("UPDATE expenses SET DeliverTo = '%s' WHERE ExpenseNumber = '%s'", event.getNewValue(), this.expenses_selectedExpenseNumber));

            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Success", "Field successfully changed",
                    "Ok", "close");
        }

        this.loadExpensesTable();
    }

    public void expense_deliverToCancel(){

    }

    public void expenses_message(){
        this.expenses_message.setText(this.expenses_messages.get(this.expenses_currentMessageIndex));

        if (expenses_messages.size() == this.expenses_currentMessageIndex + 1)
            this.expenses_currentMessageIndex = 0;
        else
            this.expenses_currentMessageIndex += 1;
    }

    public void expense_totalItemCostStart() throws IOException {
        ExpensesTable row = this.expenses_ttv_main.getSelectionModel().getSelectedItem().getValue();

        this.expenses_selectedExpenseNumber = row.getExpenseNumber();

        if (row.getStatus().equals("Cancelled")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Cannot edit total item cost when the record was cancelled",
                    "Ok", "close");

            this.loadExpensesTable();
        }
    }

    public void expense_totalItemCommit(TreeTableColumn.CellEditEvent<ExpensesTable, String> event) throws IOException, SQLException {
        ExpensesTable row = this.expenses_ttv_main.getSelectionModel().getSelectedItem().getValue();

        if (event.getNewValue().equals("")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "This field cannot be empty",
                    "Ok", "close");
        }else if (!event.getOldValue().equals(event.getNewValue())){
            try{
                Double.parseDouble(event.getNewValue());
            }catch (Exception e){
                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Error", "Total cost must only consist of digits",
                        "Ok", "close");
                this.loadExpensesTable();
                return;
            }

            if (Double.parseDouble(event.getNewValue()) < 0){
                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Error", "Total item cost cannot be lower than 0",
                        "Ok", "close");
            }else {
                this.statement.execute(String.format("UPDATE expenses SET TotalItemCost = '%s' WHERE ExpenseNumber = '%s';",
                        event.getNewValue(), this.expenses_selectedExpenseNumber));

                if (row.getCostPerItem() != null){
                    double newCostPerItem = Double.parseDouble(event.getNewValue()) / Double.parseDouble(row.getQuantity());
                    this.statement.execute(String.format("UPDATE expenses SET CostPerItem = %.2f WHERE ExpenseNumber = '%s';",
                            newCostPerItem, this.expenses_selectedExpenseNumber));
                }

                double forTotalExpenseCost = Double.parseDouble(event.getNewValue()) - Double.parseDouble(event.getOldValue());
                this.statement.execute(String.format("UPDATE expenses SET TotalExpenseCost = TotalExpenseCost + %.2f WHERE ExpenseNumber = '%s';",
                        forTotalExpenseCost, this.expenses_selectedExpenseNumber));

                if (row.getCostPerItem() != null){
                    PopUpApplication popup = new PopUpApplication();
                    popup.start(new Stage());
                    popup.setData(this.stage_this, "Success", "Total item cost, cost-per-item, total-expense-cost have been successfully changed",
                            "Ok", "close");
                }else {
                    PopUpApplication popup = new PopUpApplication();
                    popup.start(new Stage());
                    popup.setData(this.stage_this, "Success", "Total item cost and total expense cost have been successfully changed",
                            "Ok", "close");
                }

            }
        }

        this.loadExpensesTable();
        this.dataChanged();
    }

    public void expense_totalItemCostCancel(){
        
    }

    public void expense_expenseTitleStart(){
        ExpensesTable row = this.expenses_ttv_main.getSelectionModel().getSelectedItem().getValue();

        this.expenses_selectedExpenseNumber = row.getExpenseNumber();
    }

    public void expense_expenseTitleCommit(TreeTableColumn.CellEditEvent<ExpensesTable, String> event) throws IOException, SQLException {
        if (event.getNewValue().equals("")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "This field cannot be empty",
                    "Ok", "close");
        }else if (!event.getOldValue().equals(event.getNewValue())){
            this.rs = this.statement.executeQuery(String.format("SELECT * FROM expenses WHERE ExpenseTitle = '%s'", event.getNewValue()));

            if (rs.next()){
                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Error", "Expense title already exist.",
                        "Ok", "close");
            } else {
                this.rs = this.statement.executeQuery(String.format("SELECT * FROM expenses WHERE ExpenseTitle = '%s'", event.getOldValue()));

                ArrayList<String> expenseNumbers = new ArrayList<>();

                while (rs.next()){
                    expenseNumbers.add(this.rs.getString("ExpenseNumber"));
                }

                for (String expenseNumber : expenseNumbers) {
                    this.statement.execute(String.format("UPDATE expenses SET ExpenseTitle = '%s' WHERE ExpenseNumber = '%s';", event.getNewValue(), expenseNumber));
                    this.statement.execute(String.format("UPDATE inventory SET ExpenseTitle = '%s' WHERE ExpenseNumber = '%s';", event.getNewValue(), expenseNumber));
                    this.statement.execute(String.format("UPDATE sales SET Product = '%s' WHERE Product = '%s';", event.getNewValue(), event.getOldValue()));
                }

                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Success", "Expense title has successfully changed",
                        "Ok", "close");
            }
        }

        this.loadExpensesTable();
        this.loadInventoryTable();
        this.loadSalesTable();
        this.dataChanged();
    }

    public void expense_expenseTitleCancel(){

    }

    public void expense_companyNameStart(){
        ExpensesTable row = this.expenses_ttv_main.getSelectionModel().getSelectedItem().getValue();

        this.expenses_selectedExpenseNumber = row.getExpenseNumber();
    }

    public void expense_companyNameCommit(TreeTableColumn.CellEditEvent<ExpensesTable, String> event) throws IOException, SQLException {
        if (event.getNewValue().equals("")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "This field cannot be empty",
                    "Ok", "close");
        }else if (!event.getOldValue().equals(event.getNewValue())){
            this.rs = this.statement.executeQuery(String.format("SELECT * FROM vendors WHERE CompanyName = '%s'", event.getNewValue()));
            if (rs.next()){
                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Error", "Company name already exist.",
                        "Ok", "close");
            } else {
                this.statement.execute(String.format("UPDATE `vendors` SET `CompanyName` = '%s' WHERE `vendors`.`CompanyName` = '%s';", event.getNewValue(), event.getOldValue()));

                PopUpApplication popup = new PopUpApplication();
                popup.start(new Stage());
                popup.setData(this.stage_this, "Success", "Company name has successfully changed",
                        "Ok", "close");
            }
        }

        this.loadExpensesTable();
        this.loadVendorsTable();
    }

    public void expense_companyNameCancel(){

    }

    public void expense_dateDeliveredDoubleClicked() throws IOException {
        ExpensesTable row = this.expenses_ttv_main.getSelectionModel().getSelectedItem().getValue();

        this.expenses_selectedExpenseNumber = row.getExpenseNumber();
        this.currentRow = row;

        if (row.getStatus().equals("Cancelled")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Cannot change this cell when the record was cancelled",
                    "Ok", "close");

            loadExpensesTable();
        }else if (row.getPaymentTerms().equals("Due on Receipt")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Due on receipt items cannot have a value for this field",
                    "Ok", "close");

            loadExpensesTable();
        }else if (row.getPaymentTerms().equals("Payment in Advance") && row.getDeliverTo().equals("")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Cannot edit field when delivery details are blank",
                    "Ok", "close");

            loadExpensesTable();
        }
    }

    public void expense_dateDeliveredPicked(TreeTableColumn.CellEditEvent<ExpensesTable, Date> event) throws IOException, SQLException {
        ExpensesTable row = this.expenses_ttv_main.getSelectionModel().getSelectedItem().getValue();

        this.expenses_newDateDelivered = event.getNewValue();
        this.expenses_oldDateDelivered = event.getOldValue();

        if (row.getStatus().equals("Transaction Completed") && !event.getOldValue().equals(event.getNewValue())){
            this.statement.execute(String.format("UPDATE expenses SET DateDelivered = '%s' WHERE ExpenseNumber = '%s'", event.getNewValue(), this.expenses_selectedExpenseNumber));

            if (row.getPaymentTerms().equals("Cash on Delivery"))
                this.statement.execute(String.format("UPDATE expenses SET PaymentDate = '%s' WHERE ExpenseNumber = '%s'", event.getNewValue(), this.expenses_selectedExpenseNumber));

            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Success", "Date delivered was successfully changed",
                    "Ok", "close");

            this.loadExpensesTable();
        }else if (row.getStatus().equals("Transaction on Process")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Confirmation", "Adding a value means changing the status to transaction completed. Continue?",
                    "Continue", "datedelivered_status");
        }
    }

    public void expense_dateDeliveredCancelled(){
        System.out.println("3");
    }

    public void expense_statusDoubleClicked() throws IOException {
        ExpensesTable record = this.expenses_ttv_main.getSelectionModel().getSelectedItem().getValue();

        this.expenses_selectedExpenseNumber = record.getExpenseNumber();
        this.currentRow = record;

        if (record.getStatus().equals("Cancelled")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Cannot un-cancel transactions",
                    "Ok", "close");

            this.loadExpensesTable();
        }else if (record.getStatus().equals("Transaction Completed")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Cannot edit completed transactions",
                    "Ok", "close");

            this.loadExpensesTable();
        }
    }

    public void expense_statusPicked(TreeTableColumn.CellEditEvent<ExpensesTable, String> event) throws IOException {
        ExpensesTable row = this.expenses_ttv_main.getSelectionModel().getSelectedItem().getValue();

        this.expenses_oldStatus = event.getOldValue();
        this.expenses_newStatus = event.getNewValue();

        if (event.getOldValue().equals("Cancelled")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Cannot un-cancel transactions",
                    "Ok", "close");

            row.setStatus(" " + this.expenses_oldStatus);
            row.setStatus(row.getStatus().trim());
        }else if (event.getOldValue().equals("Transaction Completed")){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Cannot edit completed transactions",
                    "Ok", "close");

            row.setStatus(" " + this.expenses_oldStatus);
            row.setStatus(row.getStatus().trim());
        } else if (event.getOldValue().equals("Transaction on Process") && !this.expenses_oldStatus.equals(this.expenses_newStatus)){
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Confirmation", String.format("Are you sure you want to change the value to %s?", this.expenses_newStatus),
                    "Change", "change expense status");
        }
    }

    public void expense_cancelled(){
        
    }

    public void showExpenseImage() throws IOException, SQLException {
        if (this.expenses_ttv_main.getSelectionModel().getSelectedItem() != null){
            TreeItem<ExpensesTable> selectedRow = this.expenses_ttv_main.getSelectionModel().getSelectedItem();
            ExpensesTable record = selectedRow.getValue();

            ShowImageApplication application = new ShowImageApplication();
            application.start(new Stage());
            application.setData(record.getExpenseNumber());
        }else {
            PopUpApplication popup = new PopUpApplication();
            popup.start(new Stage());
            popup.setData(this.stage_this, "Error", "Please select a record in the table", "Ok", "close");
        }
    }

    public void search(){
        String column = this.expenses_cb_column.getSelectionModel().getSelectedItem();
        String toSearch = this.expenses_tf_search.getText();
        LocalDate date = this.expenses_dp_startingDate.getValue();
        String startingDate = date == null ? null : date.toString();
        date = this.expenses_dp_endingDate.getValue();
        String endingDate = date == null ? null : date.toString();
        String dateColumn = this.expenses_cb_dateColumn.getSelectionModel().getSelectedItem();
        column = column.equals("Quantity / Amount") ? "Quantity" : column;
        boolean isChecked = this.expenses_cb_includeCancelledRecords.isSelected();
        boolean searchByMatch = this.expenses_cb_searchExactMatch.isSelected();

        String sql = "SELECT * FROM expenses WHERE";

        if (isChecked)
            sql += " Status LIKE '%%'";
        else
            sql += " Status NOT LIKE '%Cancelled%'";

        if (startingDate != null)
            sql += String.format(" AND %s >= '%s'", dateColumn.replaceAll(" ", ""), startingDate.replaceAll(" ", ""));

        if (endingDate != null)
            sql += String.format(" AND %s <= '%s'", dateColumn.replaceAll(" ", ""), endingDate.replaceAll(" ", ""));

        if (!toSearch.equals("")){
            if (toSearch.toLowerCase(Locale.ROOT).equals("Null".toLowerCase(Locale.ROOT))){
                sql += String.format(" AND %s = '' OR %s IS NULL", column.replaceAll(" ", ""), column.replaceAll(" ", ""));
            }else {
                if (searchByMatch){
                    sql += String.format(" AND %s = '%s'", column.replaceAll(" ", ""), toSearch.trim());
                }else{
                    sql += String.format(" AND %s LIKE '%%%s%%'", column.replaceAll(" ", ""), toSearch.trim());
                }

            }

        }

        this.currentExpensesTableQuery = sql;
        this.loadExpensesTable();
    }

    public void expenses_clearStartingDate(){
        this.expenses_dp_startingDate.setValue(null);
    }

    public void expenses_clearEndingDate(){
        this.expenses_dp_endingDate.setValue(null);
    }

    public void expenses_clearSearch(){
        this.expenses_tf_search.clear();
    }

    public void loadExpensesTable(){
        this.root.getChildren().clear();

        try{
            rs = statement.executeQuery(this.currentExpensesTableQuery);

            while (rs.next()){
                TreeItem<ExpensesTable> column = new TreeItem<>(new ExpensesTable(rs.getString("ExpenseNumber"), rs.getString("ExpenseType"),
                        rs.getString("CompanyName"), rs.getString("ExpenseTitle"), rs.getString("Measurement"), rs.getString("Quantity"), rs.getString("CostPerItem"),
                        rs.getString("TotalItemCost"), rs.getString("Description"), rs.getString("DeliverTo"), rs.getString("DeliveredFrom"),
                        rs.getString("ShipmentPreference"), rs.getString("TrackingNumber"), rs.getString("DeliveryFees"),
                        rs.getString("ExpectedDeliveryDate"), rs.getString("DateDelivered") , rs.getString("PaymentDate"), rs.getString("PaymentTerms"),
                        rs.getString("TotalExpenseCost"), rs.getString("DateAdded"), rs.getString("Status")));

                root.getChildren().add(column);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        expenses_col_expenseNumber.setCellValueFactory(expensesTableStringCellDataFeatures -> expensesTableStringCellDataFeatures.getValue().getValue().expenseNumberProperty());
        expenses_col_type.setCellValueFactory(expensesTableStringCellDataFeatures -> expensesTableStringCellDataFeatures.getValue().getValue().typeProperty());
        expenses_col_companyName.setCellValueFactory(expensesTableStringCellDataFeatures -> expensesTableStringCellDataFeatures.getValue().getValue().companyNameProperty());
        expenses_col_expenseTitle.setCellValueFactory(expensesTableStringCellDataFeatures -> expensesTableStringCellDataFeatures.getValue().getValue().expenseTitleProperty());
        expenses_col_measurement.setCellValueFactory(expensesTableStringCellDataFeatures -> expensesTableStringCellDataFeatures.getValue().getValue().measurementProperty());
        expenses_col_quantity.setCellValueFactory(expensesTableStringCellDataFeatures -> expensesTableStringCellDataFeatures.getValue().getValue().quantityProperty());
        expenses_col_costPerItem.setCellValueFactory(expensesTableStringCellDataFeatures -> expensesTableStringCellDataFeatures.getValue().getValue().costPerItemProperty());
        expenses_col_totalItemCost.setCellValueFactory(expensesTableStringCellDataFeatures -> expensesTableStringCellDataFeatures.getValue().getValue().totalItemCostProperty());
        expenses_col_description.setCellValueFactory(expensesTableStringCellDataFeatures -> expensesTableStringCellDataFeatures.getValue().getValue().descriptionProperty());
        expenses_col_deliverTo.setCellValueFactory(expensesTableStringCellDataFeatures -> expensesTableStringCellDataFeatures.getValue().getValue().deliverToProperty());
        expenses_col_deliveredFrom.setCellValueFactory(expensesTableStringCellDataFeatures -> expensesTableStringCellDataFeatures.getValue().getValue().deliveredFromProperty());
        expenses_col_shipmentPreference.setCellValueFactory(expensesTableStringCellDataFeatures -> expensesTableStringCellDataFeatures.getValue().getValue().shipmentPreferenceProperty());
        expenses_col_trackingNumber.setCellValueFactory(expensesTableStringCellDataFeatures -> expensesTableStringCellDataFeatures.getValue().getValue().trackingNumberProperty());
        expenses_col_deliveryFees.setCellValueFactory(expensesTableStringCellDataFeatures -> expensesTableStringCellDataFeatures.getValue().getValue().deliveryFeesProperty());
        expenses_col_expectedDeliveryDate.setCellValueFactory(expensesTableStringCellDataFeatures -> expensesTableStringCellDataFeatures.getValue().getValue().expectedDeliveryDateProperty());
        expenses_col_dateDelivered.setCellValueFactory(expensesTableStringCellDataFeatures -> expensesTableStringCellDataFeatures.getValue().getValue().dateDeliveredProperty());
        expenses_col_paymentDate.setCellValueFactory(expensesTableDateCellDataFeatures -> expensesTableDateCellDataFeatures.getValue().getValue().paymentDateProperty());
        expenses_col_paymentTerms.setCellValueFactory(expensesTableStringCellDataFeatures -> expensesTableStringCellDataFeatures.getValue().getValue().paymentTermsProperty());
        expenses_col_totalExpenseCost.setCellValueFactory(expensesTableStringCellDataFeatures -> expensesTableStringCellDataFeatures.getValue().getValue().totalExpenseCostProperty());
        expenses_col_dateAdded.setCellValueFactory(expensesTableStringCellDataFeatures -> expensesTableStringCellDataFeatures.getValue().getValue().dateAddedProperty());
        expenses_col_status.setCellValueFactory(expensesTableStringCellDataFeatures -> expensesTableStringCellDataFeatures.getValue().getValue().statusProperty());

        expenses_col_status.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn("Transaction Completed", "Transaction on Process", "Cancelled"));
        expenses_col_dateDelivered.setCellFactory(dateCellFactory);
        expenses_col_companyName.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        expenses_col_expenseTitle.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        expenses_col_totalItemCost.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        expenses_col_description.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        expenses_col_deliverTo.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        expenses_col_deliveredFrom.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        expenses_col_shipmentPreference.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        expenses_col_trackingNumber.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        expenses_col_deliveryFees.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        expenses_col_expectedDeliveryDate.setCellFactory(dateCellFactory);
        expenses_col_paymentDate.setCellFactory(dateCellFactory);

        this.expenses_ttv_main.setEditable(true);
        this.expenses_ttv_main.setRoot(root);
        this.expenses_ttv_main.setShowRoot(false);
    }

    public void clearSelection(){
        Platform.runLater(() -> this.expenses_ttv_main.getSelectionModel().clearSelection());
    }

    public void show_charts(){
        set_effect(ap_charts, this.current, lbl_charts);
        lbl_windowTitle.setText("Charts");
    }

    public void show_inventory(){
        set_effect(ap_inventory, this.current, lbl_inventory);
        lbl_windowTitle.setText("Inventory");
    }

    public void show_accounts(){
        set_effect(ap_accounts, this.current, lbl_accounts);
        lbl_windowTitle.setText("Accounts");
    }

    public void show_vendors(){
        set_effect(ap_vendors, this.current, lbl_vendors);
        lbl_windowTitle.setText("Suppliers");
    }

    public void show_expenses(){
        set_effect(ap_expenses, this.current, lbl_expenses);
        lbl_windowTitle.setText("Expenses");
    }

    public void show_transactions(){
        set_effect(ap_sales, this.current, lbl_transactions);
        lbl_windowTitle.setText("Sales");
    }

    public void show_account(){
        set_effect(ap_account, this.current, lbl_account);
        lbl_windowTitle.setText("Account");
    }

    public void set_effect(AnchorPane pane, Label from, Label to){
        if (from == to){
            return;
        }

        pane.toFront();
        if (to == this.lbl_transactions) {
            to.setStyle("-fx-background-color: #fcfcfa; -fx-background-radius:  25 0 0 25; -fx-font-size: 26px; -fx-font-weight: 700;");
        }else if (to == this.lbl_inventory || to == this.lbl_account || to == this.lbl_vendors || to == this.lbl_accounts){
            to.setStyle("-fx-background-color: #fcfcfa; -fx-background-radius:  25 0 0 25; -fx-font-size: 34px; -fx-font-weight: 700;");
        }else if (to == this.lbl_expenses){
            to.setStyle("-fx-background-color: #fcfcfa; -fx-background-radius:  25 0 0 25; -fx-font-size: 30px; -fx-font-weight: 700;");
        }else {
            to.setStyle("-fx-background-color: #fcfcfa; -fx-background-radius:  25 0 0 25; -fx-font-size: 38px; -fx-font-weight: 700;");
        }

        to.setEffect(this.shadow);
        from.setEffect(null);

        if (from == this.lbl_inventory ){
            from.setStyle("-fx-background-color: transparent; -fx-background-radius:  25 0 0 25; -fx-font-size: 22px; -fx-font-weight: 400;");
        }else if (from == this.lbl_transactions){
            from.setStyle("-fx-background-color: transparent; -fx-background-radius:  25 0 0 25; -fx-font-size: 16px; -fx-font-weight: 400;");
        }else if (from == this.lbl_account || from == this.lbl_vendors || from == this.lbl_accounts){
            from.setStyle("-fx-background-color: transparent; -fx-background-radius:  25 0 0 25; -fx-font-size: 24px; -fx-font-weight: 400;");
        }else if (from == this.lbl_expenses){
            from.setStyle("-fx-background-color: transparent; -fx-background-radius:  25 0 0 25; -fx-font-size: 20px; -fx-font-weight: 400;");
        }else{
            from.setStyle("-fx-background-color: transparent; -fx-background-radius:  25 0 0 25; -fx-font-size: 28px; -fx-font-weight: 400;");
        }

        this.current = to;
    }

    public void logout(ActionEvent event) throws IOException {
        PopUpApplication popup = new PopUpApplication();
        popup.start(new Stage());
        Stage this_stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        popup.setData(this_stage, "Confirmation", "Do you want log off?", "Logout", "logout");
    }

    public void exitApplication(ActionEvent event) throws IOException {
        PopUpApplication popup = new PopUpApplication();
        popup.start(new Stage());
        Stage this_stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        popup.setData(this_stage, "Confirmation", "Do you want to exit the application?", "Exit", "exit application");
    }

    public void setData(Stage stage_this, String username, String password) throws SQLException {
        this.currentUsername = username;
        this.currentPassword = password;
        this.stage_this = stage_this;
        this.stage_this.setUserData(this);

        this.changeAge(username);

        ResultSet result = statement.executeQuery(String.format("SELECT CONCAT(FirstName, \" \", LastName) as Name FROM user WHERE Username = \"%s\";", username));

        if (result.next())
            lbl_name.setText(result.getString(1));

        // account
        try {
            this.setAccountLabels();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            this.rs = this.statement.executeQuery(String.format("SELECT * FROM accounts WHERE Username = '%s'", this.currentUsername));
            this.rs.next();

            String role = this.rs.getString("Role");
            this.role = role;
            this.loadInventoryTable();

            if (role.equals("Employee")){
                shadow = lbl_transactions.getEffect();
                current = lbl_transactions;
            }else {
                shadow = lbl_expenses.getEffect();
                current = lbl_expenses;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void title_hover(){
        this.currentTitle = this.lbl_windowTitle.getText();
        this.lbl_windowTitle.setText("Mhaika's Sales and Inventory System");
    }

    public void title_hoverExit(){
        this.lbl_windowTitle.setText(this.currentTitle);
    }

    public void minimize(ActionEvent event){
        Stage this_stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this_stage.setIconified(true);
    }

    public void expenses_add() throws IOException {
        Vendor0Application vendor0 = new Vendor0Application();
        vendor0.start(new Stage());
        vendor0.setData(this, "expenses");
    }

    public void saveExpenseTable() throws Exception{
        FileChooser asdf = new FileChooser();
        asdf.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        File file =  asdf.showSaveDialog(null);

        if (file == null)
            return;

        ObservableList<TreeTableColumn<ExpensesTable, ?>> column = this.expenses_ttv_main.getColumns();
        ObservableList<TreeItem<ExpensesTable>> records = this.expenses_ttv_main.getRoot().getChildren();

        for (int i = 0; i < column.size(); i++){
            System.out.println(column.get(i).getText());
        }

        Document dc = new Document();
        PdfWriter.getInstance(dc, new FileOutputStream(file.getPath()));
        dc.open();

        PdfPTable table = new PdfPTable(5);
        table.setSpacingAfter(50);

        table.addCell(this.addColumnCell("#"));

        for (int i = 0; i < 4; i++){
            table.addCell( this.addColumnCell(column.get(i).getText()));
        }

        for (int i = 0; i < records.size(); i++){
            table.addCell( this.addRecordCell(String.valueOf(i + 1), i));

            table.addCell(this.addRecordCell(records.get(i).getValue().getExpenseNumber(), i));
            table.addCell(this.addRecordCell(records.get(i).getValue().getType(), i));
            table.addCell(this.addRecordCell(records.get(i).getValue().getCompanyName(), i));
            table.addCell(this.addRecordCell(records.get(i).getValue().getExpenseTitle(), i));
        }

        PdfPTable table1 = new PdfPTable(6);
        table1.setSpacingAfter(50);

        table1.addCell(this.addColumnCell("#"));
        table1.addCell(this.addColumnCell(column.get(4).getText()));
        table1.addCell(this.addColumnCell(column.get(5).getText()));
        table1.addCell(this.addColumnCell(column.get(6).getText()));
        table1.addCell(this.addColumnCell(column.get(7).getText()));
        table1.addCell(this.addColumnCell(column.get(8).getText()));

        for (int i = 0; i < records.size(); i++){
            table1.addCell(this.addRecordCell(String.valueOf(i + 1), i));

            table1.addCell(this.addRecordCell(records.get(i).getValue().getMeasurement(), i));
            table1.addCell(this.addRecordCell(records.get(i).getValue().getQuantity(), i));
            table1.addCell(this.addRecordCell(records.get(i).getValue().getCostPerItem(), i));
            table1.addCell(this.addRecordCell(records.get(i).getValue().getTotalItemCost(), i));
            table1.addCell(this.addRecordCell(records.get(i).getValue().getDescription(), i));
        }

        PdfPTable table2 = new PdfPTable(6);
        table2.setSpacingAfter(50);

        table2.addCell(this.addColumnCell("#"));
        table2.addCell( this.addColumnCell(column.get(9).getText()) );
        table2.addCell( this.addColumnCell(column.get(10).getText()) );
        table2.addCell( this.addColumnCell(column.get(11).getText()) );
        table2.addCell( this.addColumnCell(column.get(12).getText()) );
        table2.addCell( this.addColumnCell(column.get(13).getText()) );

        for (int i = 0; i < records.size(); i++){
            table2.addCell( this.addRecordCell(String.valueOf(i + 1), i));

            table2.addCell(this.addRecordCell(records.get(i).getValue().getDeliverTo(), i));
            table2.addCell(this.addRecordCell(records.get(i).getValue().getDeliveredFrom(), i));
            table2.addCell(this.addRecordCell(records.get(i).getValue().getShipmentPreference(), i));
            table2.addCell(this.addRecordCell(records.get(i).getValue().getTrackingNumber(), i));
            table2.addCell(this.addRecordCell(records.get(i).getValue().getDeliveryFees(), i));
        }

        PdfPTable table4 = new PdfPTable(5);
        table4.setSpacingAfter(50);

        table4.addCell(this.addColumnCell("#"));
        table4.addCell( this.addColumnCell(column.get(14).getText()) );
        table4.addCell( this.addColumnCell(column.get(15).getText()) );
        table4.addCell( this.addColumnCell(column.get(16).getText()) );
        table4.addCell( this.addColumnCell(column.get(17).getText()) );

        for (int i = 0; i < records.size(); i++){
            table4.addCell( this.addRecordCell(String.valueOf(i + 1), i));

            Date edd = records.get(i).getValue().getExpectedDeliveryDate();
            String eddString = edd == null ? "" : edd.toString();

            Date dd = records.get(i).getValue().getDateDelivered();
            String ddString = dd == null ? "" : dd.toString();

            Date pd = records.get(i).getValue().getPaymentDate();
            String pdString = pd == null ? "" : pd.toString();

            table4.addCell(this.addRecordCell(eddString, i));
            table4.addCell(this.addRecordCell(ddString, i));
            table4.addCell(this.addRecordCell(pdString, i));
            table4.addCell(this.addRecordCell(records.get(i).getValue().getPaymentTerms(), i));

        }

        PdfPTable table3 = new PdfPTable(5);
        table3.setSpacingAfter(50);

        table3.addCell(this.addColumnCell("#"));
        table3.addCell(this.addColumnCell("Image"));
        table3.addCell( this.addColumnCell(column.get(18).getText()));
        table3.addCell( this.addColumnCell(column.get(19).getText()));
        table3.addCell( this.addColumnCell(column.get(20).getText()) );

        for (int i = 0; i < records.size(); i++){
            table3.addCell( this.addRecordCell(String.valueOf(i + 1), i));

            ResultSet rs = this.statement.executeQuery(String.format("SELECT Image FROM expenses WHERE ExpenseNumber = '%s'", records.get(i).getValue().getExpenseNumber()));
            rs.next();

            if (rs.getBlob("Image") == null){
                PdfPCell cell = new PdfPCell(new Phrase(""));
                if (i %2 == 1){
                    cell.setBackgroundColor(WebColors.getRGBColor("#dbdbdb"));
                }
                table3.addCell(cell);
            }else{
                Blob imageBlob1 = rs.getBlob("Image");
                byte[] imageBytes1 = imageBlob1.getBytes(1, (int) imageBlob1.length());
                Image image1 = com.itextpdf.text.Image.getInstance(imageBytes1);
                table3.addCell(image1);
            }

            table3.addCell(this.addRecordCell(records.get(i).getValue().getTotalExpenseCost(), i));
            table3.addCell(this.addRecordCell(records.get(i).getValue().getDateAdded(), i));
            table3.addCell(this.addRecordCell(records.get(i).getValue().getStatus(), i));

        }

        dc.add(table);
        dc.add(table1);
        dc.add(table2);
        dc.add(table4);
        dc.add(table3);

        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + file.getPath());
        dc.close();
    }

    public PdfPCell addRecordCell(String text, int colorIndicator){
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
        PdfPCell cell = new PdfPCell(new Phrase(text, normalFont));

        BaseColor bc;

        if (colorIndicator %2 == 1){
             bc = WebColors.getRGBColor("#dbdbdb");
        }else{
             bc = WebColors.getRGBColor("#ffffff");
        }

        cell.setBackgroundColor(bc);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setMinimumHeight(100);

        return cell;
    }

    public PdfPCell addColumnCell(String text){
        Font boldFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);

        PdfPCell cell = new PdfPCell(new Phrase(text, boldFont));
        BaseColor bc = WebColors.getRGBColor("#00adcc");
        cell.setBackgroundColor(bc);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setMinimumHeight(50);

        return cell;
    }

}

class DateEditingCell extends TreeTableCell<ExpensesTable, Date> {

    private DatePicker datePicker;

    public DateEditingCell() {
    }


    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createDatePicker();
            setText(null);
            setGraphic(datePicker);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setText(getDate() == null ? null : getDate().toString());
        setGraphic(null);
    }

    @Override
    public void updateItem(Date item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {

                if (datePicker != null) {
                    datePicker.setValue(getDate());
                }

                setText(null);
                setGraphic(datePicker);
            } else {
                setText(getDate() == null ? null : getDate().toString());
            }
        }
    }

    private void createDatePicker() {
        datePicker = new DatePicker(getDate() == null ? LocalDate.now() : getDate());
        datePicker.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        datePicker.setOnAction((e) -> {
            commitEdit(Date.valueOf(datePicker.getValue().toString()));
            setGraphic(null);
        });
    }

    private LocalDate getDate() {
        return getItem() == null ? null : getItem().toLocalDate();
    }
}

class InventoryDateEditingCell extends TreeTableCell<InventoryTable, Date> {

    private DatePicker datePicker;

    public InventoryDateEditingCell() {
    }


    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createDatePicker();
            setText(null);
            setGraphic(datePicker);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setText(getDate() == null ? null : getDate().toString());
        setGraphic(null);
    }

    @Override
    public void updateItem(Date item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {

                if (datePicker != null) {
                    datePicker.setValue(getDate());
                }

                setText(null);
                setGraphic(datePicker);
            } else {
                setText(getDate() == null ? null : getDate().toString());
            }
        }
    }

    private void createDatePicker() {
        datePicker = new DatePicker(getDate() == null ? LocalDate.now() : getDate());
        datePicker.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        datePicker.setOnAction((e) -> {
            commitEdit(Date.valueOf(datePicker.getValue().toString()));
            setGraphic(null);
        });
    }

    private LocalDate getDate() {
        return getItem() == null ? null : getItem().toLocalDate();
    }
}
