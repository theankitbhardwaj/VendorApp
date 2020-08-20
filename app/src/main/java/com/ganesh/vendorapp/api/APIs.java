package com.ganesh.vendorapp.api;

import com.ganesh.vendorapp.models.DefaultResponse;
import com.ganesh.vendorapp.models.LoginResponse;
import com.ganesh.vendorapp.models.OtpResponse;
import com.ganesh.vendorapp.models.Products;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIs {

    @FormUrlEncoded
    @POST("vendor/createuser")
    Call<DefaultResponse> createUser(
            @Field("uid") String uid,
            @Field("fullname") String fullName,
            @Field("phone_no") String phone_no,
            @Field("email") String email,
            @Field("login_with") String login_with
    );


    @FormUrlEncoded
    @POST("vendor/getuser")
    Call<LoginResponse> getUser(
            @Field("uid") String uid
    );

    @FormUrlEncoded
    @POST("vendor/getuserbyemail")
    Call<LoginResponse> getUserbyemail(
            @Field("uid") String uid,
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("vendor/sendotp")
    Call<OtpResponse> getOtp(
            @Field("phone_no") String phone_no
    );

}
