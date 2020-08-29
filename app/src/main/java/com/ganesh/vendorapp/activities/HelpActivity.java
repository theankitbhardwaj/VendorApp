package com.ganesh.vendorapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ganesh.vendorapp.R;
import com.ganesh.vendorapp.api.RetrofitClient;
import com.ganesh.vendorapp.models.SaveResponse;
import com.ganesh.vendorapp.storage.UsersSharedPrefManager;
import com.ganesh.vendorapp.utils.Helper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelpActivity extends AppCompatActivity {

    private EditText name, message;
    private Button send;
    private Helper helper;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        name = findViewById(R.id.userName);
        message = findViewById(R.id.queryMessage);
        send = findViewById(R.id.submit);
        helper = new Helper();
        progressDialog = new ProgressDialog(this);

        send.setOnClickListener(view -> {
            if (name.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Name is empty", Toast.LENGTH_SHORT).show();
                return;
            } else if (message.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Query Message is empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (helper.isNetworkConnected(this)) {
                showLoading();
                RetrofitClient.getInstance().getApi().sendQuery(
                        UsersSharedPrefManager.getInstance(this).getUid(),
                        name.getText().toString().trim(),
                        message.getText().toString().trim(),
                        helper.getTimeStamp()
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
}