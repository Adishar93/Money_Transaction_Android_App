package com.adishar93.moneytransactionapp.pojo;

public class Request {
    String uid;
    String name;
    String email;
    String amount;
    String description;

    public Request()
    {

    }

    public Request(String uid, String name, String email, String amount, String description) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.amount = amount;
        this.description = description;
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
