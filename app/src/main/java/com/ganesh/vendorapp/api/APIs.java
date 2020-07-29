package com.ganesh.vendorapp.api;

import com.ganesh.vendorapp.models.DefaultResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIs {

    @FormUrlEncoded
    @POST("createuser")
    Call<DefaultResponse> createUser(
            @Field("fname") String fname,
            @Field("lname") String lname,
            @Field("email") String email,
            @Field("password") String password
    );
}
