package com.ganesh.vendorapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ganesh.vendorapp.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void GotoSignUpActivity(View view) {
        startActivity(new Intent(this,SignUpActivity.class));
    }

    public void login(View view) {

    }
}