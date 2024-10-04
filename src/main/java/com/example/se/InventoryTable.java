package com.example.se;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;

public class InventoryTable {
    public String getProductID() {
        return productID.get();
    }

    public SimpleStringProperty productIDProperty() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID.set(productID);
    }

    public String getExpenseNumber() {
        return expenseNumber.get();
    }

    public SimpleStringProperty expenseNumberProperty() {
        return expenseNumber;
    }

    public void setExpenseNumber(String expenseNumber) {
        this.expenseNumber.set(expenseNumber);
    }

    public String getExpenseTitle() {
        return expenseTitle.get();
    }

    public SimpleStringProperty expenseTitleProperty() {
        return expenseTitle;
    }

    public void setExpenseTitle(String expenseTitle) {
        this.expenseTitle.set(expenseTitle);
    }

    public String getProductType() {
        return productType.get();
    }

    public SimpleStringProperty productTypeProperty() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType.set(productType);
    }

    public String getProductName() {
        return productName.get();
    }

    public SimpleStringProperty productNameProperty() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
    }

    public String getVariant() {
        return variant.get();
    }

    public SimpleStringProperty variantProperty() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant.set(variant);
    }

    public String getStock() {
        return stock.get();
    }

    public SimpleStringProperty stockProperty() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock.set(stock);
    }

    public String getPrice() {
        return price.get();
    }

    public SimpleStringProperty priceProperty() {
        return price;
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public Date getExpirationDate() {
        return expirationDate.get();
    }

    public SimpleObjectProperty<Date> expirationDateProperty() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate.set(Date.valueOf(expirationDate));
    }

    public String getCriticalLevel() {
        return criticalLevel.get();
    }

    public SimpleStringProperty criticalLevelProperty() {
        return criticalLevel;
    }

    public void setCriticalLevel(String criticalLevel) {
        this.criticalLevel.set(criticalLevel);
    }

    public String getDateAdded() {
        return dateAdded.get();
    }

    public SimpleStringProperty dateAddedProperty() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded.set(dateAdded);
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    SimpleStringProperty productID, expenseNumber, expenseTitle, productType, productName, variant, stock, price, description, criticalLevel, dateAdded, status;
    SimpleObjectProperty<Date> expirationDate;

    public InventoryTable(String productID, String expenseNumber, String expenseTitle, String productType, String productName, String variant, String stock, String price, String description, String expirationDate, String criticalLevel, String dateAdded, String status){
        this.productID = new SimpleStringProperty(productID);
        this.expenseNumber = new SimpleStringProperty(expenseNumber);
        this.expenseTitle = new SimpleStringProperty(expenseTitle);
        this.productType = new SimpleStringProperty(productType);
        this.productName = new SimpleStringProperty(productName);
        this.variant = new SimpleStringProperty(variant);
        this.price = new SimpleStringProperty(price);
        this.stock = new SimpleStringProperty(stock);
        this.description = new SimpleStringProperty(description);
        this.expirationDate = new SimpleObjectProperty<>(expirationDate == null || expirationDate.equals("expirationDate") ? null : Date.valueOf(expirationDate));
        this.criticalLevel = new SimpleStringProperty(criticalLevel);
        this.dateAdded = new SimpleStringProperty(dateAdded);
        this.status = new SimpleStringProperty(status);
    }
}
