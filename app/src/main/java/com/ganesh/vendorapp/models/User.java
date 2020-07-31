package com.ganesh.vendorapp.models;

import com.google.gson.annotations.SerializedName;

public class User {

    private int uid;
    private String fname , lname , email;

    public User(int uid, String fname, String lname, String email) {
        this.uid = uid;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
    }

    public int getUid() {
        return uid;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getEmail() {
        return email;
    }
}
