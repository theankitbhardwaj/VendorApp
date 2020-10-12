package com.ganesh.vendorapp.models;

import com.google.gson.annotations.SerializedName;

public class LoginCheckResponse {

    @SerializedName("error")
    private boolean err;

    @SerializedName("message")
    private String msg;

    public String getUid() {
        return uid;
    }


    @SerializedName("uid")
    private String uid;

    public LoginCheckResponse(boolean err, String msg, String user) {
        this.err = err;
        this.msg = msg;
        this.uid = user;
    }

    public boolean isErr() {
        return err;
    }

    public String getMsg() {
        return msg;
    }


}
