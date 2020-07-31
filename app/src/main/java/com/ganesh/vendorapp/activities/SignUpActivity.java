package com.ganesh.vendorapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ganesh.vendorapp.models.DefaultResponse;
import com.ganesh.vendorapp.R;
import com.ganesh.vendorapp.api.RetrofitClient;
import com.ganesh.vendorapp.storage.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.System.out;

public class SignUpActivity extends AppCompatActivity {

    private EditText inputFirstName, inputLastName, inputEmail, inputPassword, inputConfirmPassword;
    private Button btnSignup;
    private TextView backtoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        inputFirstName = findViewById(R.id.inputFirstName);
        inputLastName = findViewById(R.id.inputLastName);
        inputEmail = findViewById(R.id.inputEmail2);
        inputPassword = findViewById(R.id.inputPassword2);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);

        btnSignup = findViewById(R.id.btnSignUp);
        btnSignup.setOnClickListener((View v) ->{
                userSignUp();
        });

        backtoLogin = findViewById(R.id.backtoLogin);
        backtoLogin.setOnClickListener(view -> {
            onBackPressed();
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void userSignUp() {
        String fname = inputFirstName.getText().toString().trim();
        String lname = inputLastName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        /* all validation here.. */
        if(fname.isEmpty()){
            inputFirstName.setError("First name is required");
            inputFirstName.requestFocus();
            return;
        }
        if(email.isEmpty()){
            inputEmail.setError("Email is required");
            inputEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            inputEmail.setError("Enter a valid email");
            inputEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            inputPassword.setError("Password required");
            inputPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            inputPassword.setError("Password should be atleast 6 character long");
            inputPassword.requestFocus();
            return;
        }
        if(!inputPassword.getText().toString().trim().equals(password)){
            inputPassword.setError("Password Should be match");
            inputPassword.requestFocus();
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
                //out.println(response.code());
                if(response.code() == 201){
                    DefaultResponse defaultResponse = response.body();
                    Toast.makeText(SignUpActivity.this, defaultResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }else if (response.code() == 422){
                    Toast.makeText(SignUpActivity.this, "User already exist", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}