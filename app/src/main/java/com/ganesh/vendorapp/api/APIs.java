package com.ganesh.vendorapp.api;

import com.ganesh.vendorapp.models.DefaultResponse;
import com.ganesh.vendorapp.models.LoginCheckResponse;
import com.ganesh.vendorapp.models.LoginResponse;
import com.ganesh.vendorapp.models.OtpResponse;
import com.ganesh.vendorapp.models.Products;
import com.ganesh.vendorapp.models.ProductsResponse;
import com.ganesh.vendorapp.models.SaveResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

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
    @POST("vendor/loginuser")
    Call<LoginCheckResponse> getCheckUser(
            @Field("phone_no") String phone_no
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

    @FormUrlEncoded
    @POST("vendor/saveproduct")
    Call<SaveResponse> saveProducts(
            @Field("description") String description,
            @Field("product_id") String productID,
            @Field("title") String title,
            @Field("uid") String uid,
            @Field("variants") String variants

    );

    @FormUrlEncoded
    @POST("vendor/updateproduct")
    Call<SaveResponse> updateProduct(
            @Field("company") String company,
            @Field("description") String description,
            @Field("product_id") String productID,
            @Field("title") String title,
            @Field("uid") String uid,
            @Field("variants") String variants

    );


    @GET("vendor/products/{uid}")
    Call<ProductsResponse> getProducts(@Path("uid") String uid);

    @GET("vendor/deleteproduct/{productId}")
    Call<SaveResponse> deleteProduct(@Path("productId") String productId);

    @FormUrlEncoded
    @POST("vendor/sendmessage")
    Call<SaveResponse> sendQuery(
            @Field("uid") String id,
            @Field("name") String name,
            @Field("message") String query,
            @Field("timestamp") String timeStamp,
            @Field("image") String image);


}
