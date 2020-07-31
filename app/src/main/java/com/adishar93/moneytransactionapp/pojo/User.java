package com.adishar93.moneytransactionapp.pojo;

public class User {
    String name;
    String email;
    String phone;
    String uid;
    boolean twoStepVerification=false;

    public User() {

    }

    public User(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isTwoStepVerification() {
        return twoStepVerification;
    }

    public void setTwoStepVerification(boolean twoStepVerification) {
        this.twoStepVerification = twoStepVerification;
    }
}
