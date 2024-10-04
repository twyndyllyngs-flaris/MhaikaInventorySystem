package com.example.se;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;

public class ExpensesTable {
    public String getExpenseNumber() {
        return expenseNumber.get();
    }

    public SimpleStringProperty expenseNumberProperty() {
        return expenseNumber;
    }

    public void setExpenseNumber(String expenseNumber) {
        this.expenseNumber.set(expenseNumber);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getCompanyName() {
        return companyName.get();
    }

    public SimpleStringProperty companyNameProperty() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName.set(companyName);
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

    public String getMeasurement() {
        return measurement.get();
    }

    public SimpleStringProperty measurementProperty() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement.set(measurement);
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

    public String getCostPerItem() {
        return costPerItem.get();
    }

    public SimpleStringProperty costPerItemProperty() {
        return costPerItem;
    }

    public void setCostPerItem(String costPerItem) {
        this.costPerItem.set(costPerItem);
    }

    public String getTotalItemCost() {
        return totalItemCost.get();
    }

    public SimpleStringProperty totalItemCostProperty() {
        return totalItemCost;
    }

    public void setTotalItemCost(String totalItemCost) {
        this.totalItemCost.set(totalItemCost);
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

    public String getDeliverTo() {
        return deliverTo.get();
    }

    public SimpleStringProperty deliverToProperty() {
        return deliverTo;
    }

    public void setDeliverTo(String deliverTo) {
        this.deliverTo.set(deliverTo);
    }

    public String getDeliveredFrom() {
        return deliveredFrom.get();
    }

    public SimpleStringProperty deliveredFromProperty() {
        return deliveredFrom;
    }

    public void setDeliveredFrom(String deliveredFrom) {
        this.deliveredFrom.set(deliveredFrom);
    }

    public String getShipmentPreference() {
        return shipmentPreference.get();
    }

    public SimpleStringProperty shipmentPreferenceProperty() {
        return shipmentPreference;
    }

    public void setShipmentPreference(String shipmentPreference) {
        this.shipmentPreference.set(shipmentPreference);
    }

    public String getTrackingNumber() {
        return trackingNumber.get();
    }

    public SimpleStringProperty trackingNumberProperty() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber.set(trackingNumber);
    }

    public String getDeliveryFees() {
        return deliveryFees.get();
    }

    public Date getExpectedDeliveryDate() {
        return expectedDeliveryDate.get();
    }

    public SimpleObjectProperty<Date> expectedDeliveryDateProperty() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(String expectedDeliveryDate) {
        this.expectedDeliveryDate.set(Date.valueOf(expectedDeliveryDate));
    }

    public Date getDateDelivered() {
        return dateDelivered.get();
    }

    public SimpleObjectProperty<Date> dateDeliveredProperty() {
        return dateDelivered;
    }

    public void setDateDelivered(String dateDelivered) {
        this.dateDelivered.set(Date.valueOf(dateDelivered));
    }

    public Date getPaymentDate() {
        return paymentDate.get();
    }

    public SimpleObjectProperty<Date> paymentDateProperty() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate.set(Date.valueOf(paymentDate));
    }

    public String getPaymentTerms() {
        return paymentTerms.get();
    }

    public SimpleStringProperty paymentTermsProperty() {
        return paymentTerms;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms.set(paymentTerms);
    }

    public String getTotalExpenseCost() {
        return totalExpenseCost.get();
    }

    public SimpleStringProperty totalExpenseCostProperty() {
        return totalExpenseCost;
    }

    public void setTotalExpenseCost(String totalExpenseCost) {
        this.totalExpenseCost.set(totalExpenseCost);
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

    public ExpensesTable(String expenseNumber, String type, String companyName, String expenseTitle, String measurement, String quantity, String costPerItem, String totalItemCost, String description, String deliverTo, String deliveredFrom, String shipmentPreference, String trackingNumber, String deliveryFees, String expectedDeliveryDate, String dateDelivered, String paymentDate, String paymentTerms, String totalExpenseCost, String dateAdded, String status) {
        this.expenseNumber = new SimpleStringProperty(expenseNumber);
        this.type = new SimpleStringProperty(type);
        this.companyName = new SimpleStringProperty(companyName);
        this.expenseTitle = new SimpleStringProperty(expenseTitle);
        this.measurement = new SimpleStringProperty(measurement);
        this.quantity = new SimpleStringProperty(quantity);
        this.costPerItem = new SimpleStringProperty(costPerItem);
        this.totalItemCost = new SimpleStringProperty(totalItemCost);
        this.description = new SimpleStringProperty(description);
        this.deliverTo = new SimpleStringProperty(deliverTo);
        this.deliveredFrom = new SimpleStringProperty(deliveredFrom);
        this.shipmentPreference = new SimpleStringProperty(shipmentPreference);
        this.trackingNumber = new SimpleStringProperty(trackingNumber);
        this.deliveryFees = new SimpleStringProperty(deliveryFees);
        this.expectedDeliveryDate = new SimpleObjectProperty<>(expectedDeliveryDate == null || expectedDeliveryDate.equals("expectedDeliveryDate") ? null : Date.valueOf(expectedDeliveryDate));
        this.dateDelivered = new SimpleObjectProperty<>(dateDelivered == null || dateDelivered.equals("dateDelivered") ? null : Date.valueOf(dateDelivered));
        this.paymentDate = new SimpleObjectProperty<>(paymentDate == null || paymentDate.equals("paymentDate") ? null : Date.valueOf(paymentDate));
        this.paymentTerms = new SimpleStringProperty(paymentTerms);
        this.totalExpenseCost = new SimpleStringProperty(totalExpenseCost);
        this.dateAdded = new SimpleStringProperty(dateAdded);
        this.status = new SimpleStringProperty(status);
    }

    public String getFees() {
        return deliveryFees.get();
    }

    public SimpleStringProperty deliveryFeesProperty() {
        return deliveryFees;
    }

    public void setDeliveryFees(String deliveryFees) {
        this.deliveryFees.set(deliveryFees);
    }

    SimpleStringProperty expenseNumber, type, companyName, expenseTitle, measurement, quantity, costPerItem,
            totalItemCost, description, deliverTo, deliveredFrom, shipmentPreference, trackingNumber,
            deliveryFees, paymentTerms, totalExpenseCost, dateAdded, status;

    SimpleObjectProperty<Date> dateDelivered, expectedDeliveryDate, paymentDate;

}
