package com.ganesh.vendorapp.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.ganesh.vendorapp.models.Products;
import com.ganesh.vendorapp.models.User;
import com.google.android.gms.common.util.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UsersSharedPrefManager {

    private static final String SHARED_PREF_NAME = "user_shared_preff";

    private static UsersSharedPrefManager mInstance;
    private Context mCtx;

    private UsersSharedPrefManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    public static synchronized UsersSharedPrefManager getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new UsersSharedPrefManager(mCtx);
        }
        return mInstance;
    }

    public void setLoginWith(String loginWith, String phoneNo) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("login_with", loginWith);
        editor.putString("phone_no", phoneNo);
        editor.apply();
    }

    public void setLoginWith(String loginWith, String email, String username) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("login_with", loginWith);
        editor.putString("email", email);
        editor.putString("fullname", username);
        editor.apply();
    }

    public void setLoginWith(String uid, String loginWith, String email, String username) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uid", uid);
        editor.putString("login_with", loginWith);
        editor.putString("email", email);
        editor.putString("fullname", username);
        editor.apply();
    }

    public String LoginWith() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("login_with", null);
    }

    public String getPhoneNo() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("phone_no", null);
    }

    public String getEmail() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("email", null);
    }

    public String getUid() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("uid", null);
    }

    public void saveProducts(Products products) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Saved Product", new Gson().toJson(products));
        editor.apply();
    }

    public void saveUser(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("uid", user.getUid());
        editor.putString("fullname", user.getFullName());
        editor.putString("phone_no", user.getPhoneNo());
        editor.putString("email", user.getEmail());
        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("uid", null) != null;
    }

    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString("uid", null),
                sharedPreferences.getString("fullname", null),
                sharedPreferences.getString("phone_no", null),
                sharedPreferences.getString("email", null)
        );
    }

    public void clear() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void temp(String string) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("base64", string);
        editor.apply();
    }

    public void setDeletedProductId(String id) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        List<String> temp = getDeletedProducts();
        if (temp != null && !temp.isEmpty()) {
            temp.add(temp.size(), id);
        } else {
            temp = new ArrayList<>();
            temp.add(id);
        }
        editor.putString("Deleted Products", new Gson().toJson(temp));
        editor.apply();
    }

    public List<String> getDeletedProducts() {
        List<String> ids;
        Type type = new TypeToken<List<String>>() {
        }.getType();
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        ids = new Gson().fromJson(sharedPreferences.getString("Deleted Products", null), type);
        return ids;
    }

    public void saveVariantImages(List<String> images) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("variantImages", new Gson().toJson(images));
        editor.apply();
    }


    public List<String> getVariantImages() {
        Type type = new TypeToken<List<String>>() {
        }.getType();
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Gson().fromJson(sharedPreferences.getString("variantImages", null), type);
    }

}
