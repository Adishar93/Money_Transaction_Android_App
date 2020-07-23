package com.adishar93.moneytransactionapp.pojo;

public class Transaction {

    String uid;
    String name;
    String email;
    String amount;
    String description;
    String dateNTime;
    public boolean to=true;

    public Transaction()
    {

    }

    public Transaction(Request request,String dateNTime) {
        this.uid = request.getUid();
        this.name = request.getName();
        this.email = request.getEmail();
        this.amount = request.getAmount();
        this.description = request.getDescription();
        this.dateNTime=dateNTime;
    }

    public Transaction(String uid, String name, String email, String amount, String description,String dateNTime) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.amount = amount;
        this.description = description;
        this.dateNTime=dateNTime;
    }

    public String getDateNTime() {
        return dateNTime;
    }

    public void setDateNTime(String dateNTime) {
        this.dateNTime = dateNTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
