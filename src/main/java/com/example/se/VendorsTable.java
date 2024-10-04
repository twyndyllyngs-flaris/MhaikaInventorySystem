package com.example.se;

import javafx.beans.property.SimpleStringProperty;

public class VendorsTable {

    public String getCompanyName() {
        return companyName.get();
    }

    public SimpleStringProperty companyNameProperty() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName.set(companyName);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getDisplayName() {
        return displayName.get();
    }

    public SimpleStringProperty displayNameProperty() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName.set(displayName);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getWorkPhone() {
        return workPhone.get();
    }

    public SimpleStringProperty workPhoneProperty() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone.set(workPhone);
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public SimpleStringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public String getWebsite() {
        return website.get();
    }

    public SimpleStringProperty websiteProperty() {
        return website;
    }

    public void setWebsite(String website) {
        this.website.set(website);
    }

    SimpleStringProperty companyName, firstName, lastName, displayName, email, workPhone, phoneNumber, website;

    public VendorsTable(String companyName, String firstName, String lastName, String displayName, String email, String workPhone, String phoneNumber, String website){
        this.companyName = new SimpleStringProperty(companyName);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.displayName = new SimpleStringProperty(displayName);
        this.email = new SimpleStringProperty(email);
        this.workPhone = new SimpleStringProperty(workPhone);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.website = new SimpleStringProperty(website);
    }
}
