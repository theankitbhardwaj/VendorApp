package com.ganesh.vendorapp.models;

import com.google.gson.annotations.SerializedName;

public class OtpResponse {

    @SerializedName("error")
    private boolean err;

    @SerializedName("message")
    private String msg;

    @SerializedName("otp")
    private String otp;

    @SerializedName("uid")
    private String uid;

    public boolean isErr() {
        return err;
    }

    public String getMsg() {
        return msg;
    }

    public String getOtp() {
        return otp;
    }

    public String getUid() {
        return uid;
    }

    public OtpResponse(boolean err, String msg, String otp, String uid) {
        this.err = err;
        this.msg = msg;
        this.otp = otp;
        this.uid = uid;
    }
}
