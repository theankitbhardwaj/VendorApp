package com.ganesh.vendorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {

    private EditText et_fname, et_lname, et_email, et_password, et_confirm_password;
    private Button btn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        et_fname = findViewById(R.id.et_fname);
        et_lname = findViewById(R.id.et_lname);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_confirm_password = findViewById(R.id.et_confirm_password);

        btn_signup = findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSignUp();
            }
        });

    }

    private void userSignUp() {
        String fname = et_fname.getText().toString().trim();
        String lname = et_lname.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        /* all validation here.. */
        if(fname.isEmpty()){
            et_fname.setError("First name is required");
            et_fname.requestFocus();
            return;
        }
        if(email.isEmpty()){
            et_email.setError("Email is required");
            et_email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email.setError("Enter a valid email");
            et_email.requestFocus();
            return;
        }
        if(password.isEmpty()){
            et_password.setError("Password required");
            et_password.requestFocus();
            return;
        }
        if(password.length() < 6){
            et_password.setError("Password should be atleast 6 character long");
            et_password.requestFocus();
            return;
        }
        if(!et_confirm_password.getText().toString().trim().equals(password)){
            et_confirm_password.setError("Password Should be match");
            et_confirm_password.requestFocus();
            return;
        }


        /* Do user registration using the api call */


    }

    public void GotoLoginActivity(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}