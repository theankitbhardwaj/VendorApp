package com.ganesh.vendorapp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ganesh.vendorapp.R;
import com.ganesh.vendorapp.adapters.VariantAdapter;
import com.ganesh.vendorapp.api.APIs;
import com.ganesh.vendorapp.api.RetrofitClient;
import com.ganesh.vendorapp.models.SaveResponse;
import com.ganesh.vendorapp.models.Variants;
import com.ganesh.vendorapp.storage.UsersSharedPrefManager;
import com.ganesh.vendorapp.utils.Helper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity {

    private EditText title, company, description;
    private RecyclerView variantRecycler;
    private VariantAdapter variantAdapter;
    private List<Variants> variantListForAdapter;
    private static final int PICK_IMAGE = 103;
    private static final int STORAGE_PERMISSION = 105;
    private Helper helper;
    private boolean accessGranted = false;
    private APIs api;
    private ProgressDialog progressDialog;
    private static final String TAG = AddProductActivity.class.getSimpleName();
    private EditText variantName, variantQuantity, variantPrice;
    private ImageView image1, image2, image3, image4, image5;
    private List<String> variantImages;
    private Button save;
    private ImageButton close1, close2, close3, close4, close5, add1, add2, add3, add4, add5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        askPermission();

        title = findViewById(R.id.et_item_title);
        company = findViewById(R.id.et_item_company);
        description = findViewById(R.id.et_item_desc);
        variantRecycler = findViewById(R.id.variantRecycler);
        variantName = findViewById(R.id.et_variant_name);
        variantQuantity = findViewById(R.id.et_variant_quantity);
        variantPrice = findViewById(R.id.et_variant_price);
        save = findViewById(R.id.saveVariant);

        add1 = findViewById(R.id.add1);
        add2 = findViewById(R.id.add2);
        add3 = findViewById(R.id.add3);
        add4 = findViewById(R.id.add4);
        add5 = findViewById(R.id.add5);

        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        image4 = findViewById(R.id.image4);
        image5 = findViewById(R.id.image5);

        close1 = findViewById(R.id.close1);
        close2 = findViewById(R.id.close2);
        close3 = findViewById(R.id.close3);
        close4 = findViewById(R.id.close4);
        close5 = findViewById(R.id.close5);

        helper = new Helper();
        progressDialog = new ProgressDialog(this);
        api = RetrofitClient.getInstance().getApi();
        variantListForAdapter = new ArrayList<>();
        variantImages = new ArrayList<>();
        variantRecycler.setHasFixedSize(true);
        variantRecycler.setLayoutManager(new LinearLayoutManager(this));

        variantAdapter = new VariantAdapter(variantListForAdapter, this);
        variantRecycler.setAdapter(variantAdapter);

        initVariant();

        /*addVariant.setOnClickListener(view -> {
            if (variantListForAdapter.isEmpty()) {
                Variants variants = new Variants();
                variantListForAdapter.add(variants);
                variantAdapter.notifyDataSetChanged();
            } else if (!variantListToSave.isEmpty()) {
                Variants variants = new Variants();
                variantListForAdapter.add(variants);
                variantAdapter.notifyDataSetChanged();
            }
        });*/

        save.setOnClickListener(view -> {
            if (variantName.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Variant Title is empty", Toast.LENGTH_SHORT).show();
                return;
            } else if (variantPrice.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Variant Price is empty", Toast.LENGTH_SHORT).show();
                return;
            } else if (variantQuantity.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Variant Quantity is empty", Toast.LENGTH_SHORT).show();
                return;
            } else if (variantImages.isEmpty()) {
                Toast.makeText(this, "Variant Images are empty", Toast.LENGTH_SHORT).show();
                return;
            }

            UsersSharedPrefManager.getInstance(this).saveVariantImages(variantImages);
            variantListForAdapter.add(new Variants(
                    variantName.getText().toString().trim(),
                    Integer.parseInt(variantQuantity.getText().toString().trim()),
                    Double.parseDouble(variantPrice.getText().toString().trim()),
                    UsersSharedPrefManager.getInstance(this).getVariantImages()
            ));

            variantAdapter.notifyDataSetChanged();
            clearVariantData();
        });

    }

    private void clearVariantData() {
        variantName.setText("");
        variantPrice.setText("");
        variantQuantity.setText("");
        variantImages.clear();
        imageHelper();
    }

    private void initVariant() {

        imageHelper();

        add1.setOnClickListener(view -> {
            getImage();
        });
        add2.setOnClickListener(view -> {
            getImage();
        });
        add3.setOnClickListener(view -> {
            getImage();
        });
        add4.setOnClickListener(view -> {
            getImage();
        });
        add5.setOnClickListener(view -> {
            getImage();
        });


        close1.setOnClickListener(view -> {
            variantImages.remove(0);
            image1.setVisibility(View.GONE);
            close1.setVisibility(View.GONE);
            add1.setVisibility(View.VISIBLE);
        });
        close2.setOnClickListener(view -> {
            variantImages.remove(1);
            image2.setVisibility(View.GONE);
            close2.setVisibility(View.GONE);
            add2.setVisibility(View.VISIBLE);
        });
        close3.setOnClickListener(view -> {
            variantImages.remove(2);
            image3.setVisibility(View.GONE);
            close3.setVisibility(View.GONE);
            add3.setVisibility(View.VISIBLE);
        });
        close4.setOnClickListener(view -> {
            variantImages.remove(3);
            image4.setVisibility(View.GONE);
            close4.setVisibility(View.GONE);
            add4.setVisibility(View.VISIBLE);

        });
        close5.setOnClickListener(view -> {
            variantImages.remove(4);
            image5.setVisibility(View.GONE);
            close5.setVisibility(View.GONE);
            add5.setVisibility(View.VISIBLE);
        });


    }

    private void imageHelper() {
        if (variantImages.isEmpty()) {
            add1.setVisibility(View.VISIBLE);
            add2.setVisibility(View.VISIBLE);
            add3.setVisibility(View.VISIBLE);
            add4.setVisibility(View.VISIBLE);
            add5.setVisibility(View.VISIBLE);

            image1.setVisibility(View.GONE);
            image2.setVisibility(View.GONE);
            image3.setVisibility(View.GONE);
            image4.setVisibility(View.GONE);
            image5.setVisibility(View.GONE);

            close1.setVisibility(View.GONE);
            close2.setVisibility(View.GONE);
            close3.setVisibility(View.GONE);
            close4.setVisibility(View.GONE);
            close5.setVisibility(View.GONE);
        } else if (variantImages.size() == 1) {

            add1.setVisibility(View.GONE);
            add2.setVisibility(View.VISIBLE);
            add3.setVisibility(View.VISIBLE);
            add4.setVisibility(View.VISIBLE);
            add5.setVisibility(View.VISIBLE);

            image1.setVisibility(View.VISIBLE);
            image2.setVisibility(View.GONE);
            image3.setVisibility(View.GONE);
            image4.setVisibility(View.GONE);
            image5.setVisibility(View.GONE);

            close1.setVisibility(View.VISIBLE);
            close2.setVisibility(View.GONE);
            close3.setVisibility(View.GONE);
            close4.setVisibility(View.GONE);
            close5.setVisibility(View.GONE);

            Glide.with(this)
                    .load(variantImages.get(0))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(image1);
        } else if (variantImages.size() == 2) {

            add1.setVisibility(View.GONE);
            add2.setVisibility(View.GONE);
            add3.setVisibility(View.VISIBLE);
            add4.setVisibility(View.VISIBLE);
            add5.setVisibility(View.VISIBLE);

            image1.setVisibility(View.VISIBLE);
            image2.setVisibility(View.VISIBLE);
            image3.setVisibility(View.GONE);
            image4.setVisibility(View.GONE);
            image5.setVisibility(View.GONE);

            close1.setVisibility(View.VISIBLE);
            close2.setVisibility(View.VISIBLE);
            close3.setVisibility(View.GONE);
            close4.setVisibility(View.GONE);
            close5.setVisibility(View.GONE);

            Glide.with(this)
                    .load(variantImages.get(0))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(image1);
            Glide.with(this)
                    .load(variantImages.get(1))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(image2);
        } else if (variantImages.size() == 3) {

            add1.setVisibility(View.GONE);
            add2.setVisibility(View.GONE);
            add3.setVisibility(View.GONE);
            add4.setVisibility(View.VISIBLE);
            add5.setVisibility(View.VISIBLE);

            image1.setVisibility(View.VISIBLE);
            image2.setVisibility(View.VISIBLE);
            image3.setVisibility(View.VISIBLE);
            image4.setVisibility(View.GONE);
            image5.setVisibility(View.GONE);

            close1.setVisibility(View.VISIBLE);
            close2.setVisibility(View.VISIBLE);
            close3.setVisibility(View.VISIBLE);
            close4.setVisibility(View.GONE);
            close5.setVisibility(View.GONE);

            Glide.with(this)
                    .load(variantImages.get(0))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(image1);
            Glide.with(this)
                    .load(variantImages.get(1))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(image2);
            Glide.with(this)
                    .load(variantImages.get(2))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(image3);

        } else if (variantImages.size() == 4) {

            add1.setVisibility(View.GONE);
            add2.setVisibility(View.GONE);
            add3.setVisibility(View.GONE);
            add4.setVisibility(View.GONE);
            add5.setVisibility(View.VISIBLE);

            image1.setVisibility(View.VISIBLE);
            image2.setVisibility(View.VISIBLE);
            image3.setVisibility(View.VISIBLE);
            image4.setVisibility(View.VISIBLE);
            image5.setVisibility(View.GONE);

            close1.setVisibility(View.VISIBLE);
            close2.setVisibility(View.VISIBLE);
            close3.setVisibility(View.VISIBLE);
            close4.setVisibility(View.VISIBLE);
            close5.setVisibility(View.GONE);

            Glide.with(this)
                    .load(variantImages.get(0))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(image1);
            Glide.with(this)
                    .load(variantImages.get(1))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(image2);
            Glide.with(this)
                    .load(variantImages.get(2))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(image3);
            Glide.with(this)
                    .load(variantImages.get(3))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(image4);

        } else if (variantImages.size() == 5) {

            add1.setVisibility(View.GONE);
            add2.setVisibility(View.GONE);
            add3.setVisibility(View.GONE);
            add4.setVisibility(View.GONE);
            add5.setVisibility(View.GONE);

            image1.setVisibility(View.VISIBLE);
            image2.setVisibility(View.VISIBLE);
            image3.setVisibility(View.VISIBLE);
            image4.setVisibility(View.VISIBLE);
            image5.setVisibility(View.VISIBLE);

            close1.setVisibility(View.VISIBLE);
            close2.setVisibility(View.VISIBLE);
            close3.setVisibility(View.VISIBLE);
            close4.setVisibility(View.VISIBLE);
            close5.setVisibility(View.VISIBLE);

            Glide.with(this)
                    .load(variantImages.get(0))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(image1);
            Glide.with(this)
                    .load(variantImages.get(1))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(image2);
            Glide.with(this)
                    .load(variantImages.get(2))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(image3);
            Glide.with(this)
                    .load(variantImages.get(3))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(image4);
            Glide.with(this)
                    .load(variantImages.get(4))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(image5);
        }
    }

    public void getImage() {
        if (accessGranted) {
            if (variantImages.size() == 5)
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
            if (!variantListForAdapter.isEmpty()) {
                if (helper.isNetworkConnected(this)) {
                    showLoading();
                    List<Variants> newVariantList = new ArrayList<>();
                    for (Variants b : variantListForAdapter) {
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
        List<Variants> temp = new ArrayList<>();

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
        return !title.getText().toString().trim().equals("");
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
            if (variantImages == null) {
                variantImages = new ArrayList<>();
                variantImages.add(String.valueOf(data.getData()));
            } else
                variantImages.add(variantImages.size(), String.valueOf(data.getData()));
            Log.e(TAG, "onActivityResult: " + variantImages);
            imageHelper();
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