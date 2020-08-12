package com.ganesh.vendorapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.ganesh.vendorapp.R;
import com.ganesh.vendorapp.storage.SharedPrefManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class SplashLogoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_logo);


        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(1000);

                    if(SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }



                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                super.run();
            }
        };
        thread.start();

    }

    @Override
    public void onBackPressed() {
        System.exit(0);
        //super.onBackPressed();
    }
}