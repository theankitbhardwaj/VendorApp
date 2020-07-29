package com.ganesh.vendorapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ganesh.vendorapp.models.DefaultResponse;
import com.ganesh.vendorapp.R;
import com.ganesh.vendorapp.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        btn_signup.setOnClickListener((View v) ->{
                userSignUp();
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
        Call<DefaultResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .createUser(fname,lname,email,password);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                if(response.code() == 201){
                    DefaultResponse res = response.body();
                    Toast.makeText(SignUpActivity.this, res.getMsg(), Toast.LENGTH_SHORT).show();
                }else if (response.code() == 422){
                    Toast.makeText(SignUpActivity.this, "User already exist", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }
        });

    }

    public void GotoLoginActivity(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}