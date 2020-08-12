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

import com.ganesh.vendorapp.R;
import com.ganesh.vendorapp.api.RetrofitClient;
import com.ganesh.vendorapp.models.DefaultResponse;
import com.ganesh.vendorapp.models.User;
import com.ganesh.vendorapp.storage.SharedPrefManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private EditText inputFullName, inputPhoneNo, inputEmail;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        inputFullName = findViewById(R.id.inputFullName);
        inputPhoneNo = findViewById(R.id.inputPhoneNo);
        inputEmail = findViewById(R.id.inputEmail2);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            inputFullName.setText(account.getDisplayName());
        }

        String phone_no = SharedPrefManager.getInstance(ProfileActivity.this).getPhoneNo();
        if(phone_no != null) {
            inputPhoneNo.setText(phone_no);
            inputPhoneNo.setEnabled(false);
        }
        String email = SharedPrefManager.getInstance(ProfileActivity.this).getEmail();
        if(email != null) {
            inputEmail.setText(email);
            inputEmail.setEnabled(false);
        }

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener((View v) ->{
                saveUserProfile();
        });

    }

    private void saveUserProfile() {
        String fullname = inputFullName.getText().toString().trim();
        String phone_no = inputPhoneNo.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();

        /* all validation here.. */

        if(phone_no.isEmpty()){
            inputPhoneNo.setError("Phone No. is required");
            inputPhoneNo.requestFocus();
            return;
        }
        if(phone_no.length() != 10 ||
                !(Character.getNumericValue(phone_no.charAt(0)) < 10 && Character.getNumericValue(phone_no.charAt(0)) > 5) ){
            inputPhoneNo.setError("Required Valid Phone Number");
            inputPhoneNo.requestFocus();
            return;
        }
        if(fullname.isEmpty()){
            inputFullName.setError("Name is required");
            inputFullName.requestFocus();
            return;
        }
        if(!email.isEmpty()){
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                inputEmail.setError("Enter a valid email");
                inputEmail.requestFocus();
                return;
            }
        }

        if(SharedPrefManager.getInstance(this).isLoggedIn()){

            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }else{

            String loginWith = SharedPrefManager.getInstance(ProfileActivity.this).LoginWith();

            if(loginWith.equals("mobile")) {

                String generateUid = (int)(Math.random() * (99999 - 10000 + 1) + 10000) + ""
                        +(int)(Math.random() * (99999 - 10000 + 1) + 10000)+ ""
                        +(int)(Math.random() * (99999 - 10000 + 1) + 10000);

                /* Do user registration using the api call */
                Call<DefaultResponse> call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .createUser(generateUid,fullname,phone_no,email,loginWith);

                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        DefaultResponse defaultResponse = response.body();
                        if(defaultResponse != null) {
                            if (!defaultResponse.isErr()) {

                                //save user in sheared preference.
                                SharedPrefManager.getInstance(ProfileActivity.this)
                                        .saveUser(new User(generateUid, fullname, phone_no, email));

                                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            } else {
                                Toast.makeText(ProfileActivity.this, "User ID already exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {
                        Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }else if(loginWith.equals("google")){

                String googleUid = GoogleSignIn.getLastSignedInAccount(this).getId();

                /* Do user registration using the api call */
                Call<DefaultResponse> call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .createUser(googleUid,fullname,phone_no,email,loginWith);

                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        DefaultResponse defaultResponse = response.body();
                        if(defaultResponse != null) {
                            if (!defaultResponse.isErr()) {

                                //save user in sheared preference.
                                SharedPrefManager.getInstance(ProfileActivity.this)
                                        .saveUser(new User(googleUid, fullname, phone_no, email));

                                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            } else {
                                Toast.makeText(ProfileActivity.this, "User ID already exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                        inputPhoneNo.setError("Phone No is already Register, try to mobile login or another no.");
                        inputPhoneNo.requestFocus();
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {
                        Toast.makeText(ProfileActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}