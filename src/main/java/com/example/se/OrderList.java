package com.example.se;

public class OrderList {
    String productName;
    double price;
    int quantity;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getListViewRegex(){
        return String.format("%s'-(x%s, ₱%s)", productName, quantity, price);
    }

    public OrderList(String productName, int quantity, double price){
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }
}
