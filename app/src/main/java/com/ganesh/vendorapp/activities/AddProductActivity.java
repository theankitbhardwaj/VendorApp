package com.ganesh.vendorapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ganesh.vendorapp.R;
import com.ganesh.vendorapp.adapters.VariantAdapter;
import com.ganesh.vendorapp.api.APIs;
import com.ganesh.vendorapp.api.RetrofitClient;
import com.ganesh.vendorapp.models.ProductsItem;
import com.ganesh.vendorapp.models.SaveResponse;
import com.ganesh.vendorapp.models.Variants;
import com.ganesh.vendorapp.storage.ProductRoom;
import com.ganesh.vendorapp.storage.UsersSharedPrefManager;
import com.ganesh.vendorapp.utils.Helper;
import com.ganesh.vendorapp.viewmodel.ProductViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity {

    private EditText title, company, description;
    private Button addVariant;
    private RecyclerView variantRecycler;
    private VariantAdapter variantAdapter;
    private List<Variants> variantsList;
    private int imagePos;
    private ProductRoom productRoom;
    private ProductViewModel productViewModel;
    private static final int PICK_IMAGE = 103;
    private static final int STORAGE_PERMISSION = 105;
    private Helper helper;
    private boolean accessGranted = false;
    private APIs api;
    private ProgressDialog progressDialog;
    private static final String TAG = AddProductActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        askPermission();

        title = findViewById(R.id.et_item_title);
        company = findViewById(R.id.et_item_company);
        description = findViewById(R.id.et_item_desc);
        addVariant = findViewById(R.id.add_variant_btn);
        variantRecycler = findViewById(R.id.variantRecycler);
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        helper = new Helper();
        progressDialog = new ProgressDialog(this);
        api = RetrofitClient.getInstance().getApi();
        variantsList = new ArrayList<>();
        variantRecycler.setHasFixedSize(true);
        variantRecycler.setLayoutManager(new LinearLayoutManager(this));

        variantAdapter = new VariantAdapter(variantsList, this);
        variantRecycler.setAdapter(variantAdapter);

        addVariant.setOnClickListener(view -> {
            Variants variants = new Variants();
            variantsList.add(variants);
            variantAdapter.notifyDataSetChanged();
        });

    }


    public void getImage(int position) {
        if (accessGranted) {
            imagePos = position;
            if (variantAdapter.getArrayList().get(position).getImage() != null &&
                    variantAdapter.getArrayList().get(position).getImage().size() == 5)
                Toast.makeText(this, "Max 5 image is allowed", Toast.LENGTH_SHORT).show();
            else {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        } else
            askPermission();

    }

    private void saveProductItem() {
        if (validateMainData()) {
            List<Variants> data = getVariantData();
            if (data != null) {
                if (helper.isNetworkConnected(this)) {
                    showLoading();
                    List<Variants> newVariantList = new ArrayList<>();
                    for (Variants b : data) {
                        newVariantList.add(new Variants(
                                b.getVariant_name(),
                                b.getQuantity(),
                                b.getPrice(),
                                helper.base64String(b.getImage(), this)
                        ));
                    }
                    api.saveProducts(
                            company.getText().toString().trim(),
                            description.getText().toString().trim(),
                            helper.getRandomID(),
                            title.getText().toString().trim(),
                            UsersSharedPrefManager.getInstance(this).getUid(),
                            new Gson().toJson(newVariantList)
                    ).enqueue(new Callback<SaveResponse>() {
                        @Override
                        public void onResponse(Call<SaveResponse> call, Response<SaveResponse> response) {
                            if (response.isSuccessful()) {
                                Log.e(TAG, "onResponse: " + response.body().getMessage());
                                if (response.body().getError().equals("false")) {
                                    cancelLoading();
                                    Log.e(TAG, "Product: " + title.getText().toString().trim() + " is saved successfully");
                                    Toast.makeText(AddProductActivity.this, "Product: " + title.getText().toString().trim() + " is saved successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SaveResponse> call, Throwable t) {
                            Log.e(TAG, "onFailure: " + t);
                            cancelLoading();
                            Toast.makeText(AddProductActivity.this, "Connection timed out. Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(this, "Connect to the internet to save the product", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please add variants", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void askPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
        } else
            accessGranted = true;
    }

    private List<Variants> getVariantData() {
        List<Variants> variants = new ArrayList<>();
        List<Variants> temp = variantAdapter.getArrayList();

        for (int i = 0; i < temp.size(); i++) {
            if (helper.validateVariantData(temp.get(i))) {
                variants.add(new Variants(temp.get(i).getVariant_name(),
                        temp.get(i).getQuantity(),
                        temp.get(i).getPrice(),
                        temp.get(i).getImage()));
            } else {
                Toast.makeText(this, "Variant at " + (i + 1) + " is not completed", Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        if (variants.isEmpty())
            return null;
        return variants;
    }

    private boolean validateMainData() {
        if (title.getText().toString().trim().equals(""))
            return false;
        else if (company.getText().toString().trim().equals(""))
            return false;
        else return !description.getText().toString().trim().equals("");
    }

    private void showLoading() {
        progressDialog.setMessage("Saving product...");
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
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            List<String> images;
            Variants temp = variantAdapter.getArrayList().get(imagePos);
            Log.e("TAG", "onActivityResult: " + temp.toString());
            images = temp.getImage();

            if (images == null) {
                images = new ArrayList<>();
                images.add(String.valueOf(data.getData()));
            } else
                images.add(images.size(), String.valueOf(data.getData()));

            temp.setImage(images);
            variantsList.set(imagePos, temp);
            variantAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_product_btn) {

            saveProductItem();
        }
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}