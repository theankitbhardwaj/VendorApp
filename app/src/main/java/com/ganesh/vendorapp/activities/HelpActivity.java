package com.ganesh.vendorapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ganesh.vendorapp.R;
import com.ganesh.vendorapp.api.RetrofitClient;
import com.ganesh.vendorapp.models.SaveResponse;
import com.ganesh.vendorapp.storage.UsersSharedPrefManager;
import com.ganesh.vendorapp.utils.Helper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelpActivity extends AppCompatActivity {

    private EditText name, message;
    private Button send, screenShot;
    private ImageView showSs;
    private Helper helper;
    private ProgressDialog progressDialog;
    private static final int PICK_IMAGE = 103;
    private static final int STORAGE_PERMISSION = 105;
    private boolean accessGranted = false;
    private Uri imageScreenShot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        name = findViewById(R.id.userName);
        message = findViewById(R.id.queryMessage);
        send = findViewById(R.id.submit);
        showSs = findViewById(R.id.displaySs);
        screenShot = findViewById(R.id.screenShot);
        helper = new Helper();
        progressDialog = new ProgressDialog(this);

        send.setOnClickListener(view -> {
            if (name.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Name is empty", Toast.LENGTH_SHORT).show();
                return;
            } else if (message.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Query Message is empty", Toast.LENGTH_SHORT).show();
                return;
            } else if (imageScreenShot == null) {
                Toast.makeText(this, "Attach Screenshot", Toast.LENGTH_SHORT).show();
                return;
            }
            if (helper.isNetworkConnected(this)) {
                showLoading();
                RetrofitClient.getInstance().getApi().sendQuery(
                        UsersSharedPrefManager.getInstance(this).getUid(),
                        name.getText().toString().trim(),
                        message.getText().toString().trim(),
                        helper.getTimeStamp(),
                        helper.base64String(imageScreenShot, this)
                ).enqueue(new Callback<SaveResponse>() {
                    @Override
                    public void onResponse(Call<SaveResponse> call, Response<SaveResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getError().equals("false")) {
                                cancelLoading();
                                Toast.makeText(HelpActivity.this, "Query submitted successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(HelpActivity.this, MainActivity.class));
                                finish();
                            } else {
                                cancelLoading();
                                Toast.makeText(HelpActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SaveResponse> call, Throwable t) {
                        cancelLoading();
                        Toast.makeText(HelpActivity.this, "Timed out", Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "onFailure: " + t);
                    }
                });
            } else {
                Toast.makeText(this, "You are offline. Please connect to internet", Toast.LENGTH_SHORT).show();
            }
        });

        screenShot.setOnClickListener(view -> {
            askPermission();
        });
    }

    private void askPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
        } else {
            accessGranted = true;
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        }
    }

    private void showLoading() {
        progressDialog.setMessage("Submitting query...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void cancelLoading() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED && requestCode != STORAGE_PERMISSION) {
            accessGranted = false;
            Toast.makeText(this, "Storage Permission is required", Toast.LENGTH_SHORT).show();
            askPermission();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageScreenShot = data.getData();
            Glide.with(this).load(imageScreenShot).into(showSs);
        }
    }

}