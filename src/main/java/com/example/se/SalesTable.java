package com.example.se;

import javafx.beans.property.SimpleStringProperty;

public class SalesTable {
    SimpleStringProperty saleNumber, userID, productName, quantity, price, totalPrice, receiptID, saleDate, totalBill, changeAmount, cash;

    public String getSaleNumber() {
        return saleNumber.get();
    }

    public SimpleStringProperty saleNumberProperty() {
        return saleNumber;
    }

    public void setSaleNumber(String saleNumber) {
        this.saleNumber.set(saleNumber);
    }

    public String getUserID() {
        return userID.get();
    }

    public SimpleStringProperty userIDProperty() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID.set(userID);
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

    public String getQuantity() {
        return quantity.get();
    }

    public SimpleStringProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity.set(quantity);
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

    public String getTotalPrice() {
        return totalPrice.get();
    }

    public SimpleStringProperty totalPriceProperty() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice.set(totalPrice);
    }

    public String getReceiptID() {
        return receiptID.get();
    }

    public SimpleStringProperty receiptIDProperty() {
        return receiptID;
    }

    public void setReceiptID(String receiptID) {
        this.receiptID.set(receiptID);
    }

    public String getSaleDate() {
        return saleDate.get();
    }

    public SimpleStringProperty saleDateProperty() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate.set(saleDate);
    }

    public String getTotalBill() {
        return totalBill.get();
    }

    public SimpleStringProperty totalBillProperty() {
        return totalBill;
    }

    public void setTotalBill(String totalBill) {
        this.totalBill.set(totalBill);
    }

    public String getChangeAmount() {
        return changeAmount.get();
    }

    public SimpleStringProperty changeAmountProperty() {
        return changeAmount;
    }

    public void setChangeAmount(String changeAmount) {
        this.changeAmount.set(changeAmount);
    }

    public String getCash() {
        return cash.get();
    }

    public SimpleStringProperty cashProperty() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash.set(cash);
    }

    public SalesTable(String saleNumber, String userID, String productName, String quantity, String price, String totalPrice, String totalBill, String cash, String changeAmount,  String receiptID, String saleDate){
        this.saleNumber = new SimpleStringProperty(saleNumber);
        this.userID = new SimpleStringProperty(userID);
        this.productName = new SimpleStringProperty(productName);
        this.quantity = new SimpleStringProperty(quantity);
        this.price = new SimpleStringProperty(price);
        this.totalPrice = new SimpleStringProperty(totalPrice);
        this.receiptID = new SimpleStringProperty(receiptID);
        this.saleDate = new SimpleStringProperty(saleDate);
        this.totalBill = new SimpleStringProperty(totalBill);
        this.changeAmount = new SimpleStringProperty(changeAmount);
        this.cash = new SimpleStringProperty(cash);
    }
}
