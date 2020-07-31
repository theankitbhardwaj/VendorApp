package com.ganesh.vendorapp.models;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("error")
    private boolean err;

    @SerializedName("message")
    private String msg;

    private User user;

    public LoginResponse(boolean err, String msg, User user) {
        this.err = err;
        this.msg = msg;
        this.user = user;
    }

    public boolean isErr() {
        return err;
    }

    public String getMsg() {
        return msg;
    }

    public User getUser() {
        return user;
    }
}
