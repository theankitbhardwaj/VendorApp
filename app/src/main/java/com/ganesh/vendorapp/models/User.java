package com.ganesh.vendorapp.models;

import com.google.gson.annotations.SerializedName;

public class User {

    private String uid;
    private String fullname, phone_no, email, login_with;

    public String getUid() {
        return uid;
    }

    public String getFullName() {
        return fullname;
    }

    public String getPhoneNo() {
        return phone_no;
    }

    public String getEmail() {
        return email;
    }

    public String getLoginWith() {
        return login_with;
    }

    public User(String uid, String fullName, String phoneNo, String email, String loginWith) {
        this.uid = uid;
        this.fullname = fullName;
        this.phone_no = phoneNo;
        this.email = email;
        this.login_with = loginWith;
    }

    public User(String uid, String fullName, String phoneNo, String email) {
        this.uid = uid;
        this.fullname = fullName;
        this.phone_no = phoneNo;
        this.email = email;
    }
}
