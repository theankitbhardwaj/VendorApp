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
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ganesh.vendorapp.R;
import com.ganesh.vendorapp.adapters.EditVariantAdapter;
import com.ganesh.vendorapp.adapters.VariantAdapter;
import com.ganesh.vendorapp.api.RetrofitClient;
import com.ganesh.vendorapp.models.ProductsItem;
import com.ganesh.vendorapp.models.SaveResponse;
import com.ganesh.vendorapp.models.Variants;
import com.ganesh.vendorapp.models.VariantsItem;
import com.ganesh.vendorapp.storage.UsersSharedPrefManager;
import com.ganesh.vendorapp.utils.Helper;
import com.ganesh.vendorapp.viewmodel.ProductViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProductActivity extends AppCompatActivity {

    private EditText title, description;
    private RecyclerView variantRecycler;
    private EditVariantAdapter variantAdapter;
    private List<VariantsItem> variantListForAdapter;
    private ProductsItem product;
    private static final String TAG = EditProductActivity.class.getSimpleName();
    private Helper helper;
    private static final int STORAGE_PERMISSION = 105;
    private ProgressDialog progressDialog;
    private boolean accessGranted = false;
    private static final int PICK_IMAGE = 103;
    private EditText variantName, variantQuantity, variantPrice;
    private TextView variantId;
    private ImageView image1, image2, image3, image4, image5;
    private List<String> variantImages;
    private Button save;
    private int variantPos = -1;
    private ImageButton close1, close2, close3, close4, close5, add1, add2, add3, add4, add5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitvity_edit_product);
        getSupportActionBar().setTitle("Edit Product Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        product = (ProductsItem) getIntent().getExtras().get("product");

        title = findViewById(R.id.et_item_title);
        description = findViewById(R.id.et_item_desc);
        variantRecycler = findViewById(R.id.variantRecycler);
        variantName = findViewById(R.id.et_variant_name);
        variantQuantity = findViewById(R.id.et_variant_quantity);
        variantPrice = findViewById(R.id.et_variant_price);
        variantId = findViewById(R.id.variantId);
        save = findViewById(R.id.saveVariant);
        progressDialog = new ProgressDialog(this);

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
        variantImages = new ArrayList<>();
        variantRecycler.setLayoutManager(new LinearLayoutManager(this));

        title.setText(product.getTitle());
        description.setText(product.getDescription());
        variantListForAdapter = product.getVariants();
        variantId.setText("0");
        variantAdapter = new EditVariantAdapter(variantListForAdapter, this);
        variantRecycler.setAdapter(variantAdapter);

        initVariant();

        save.setOnClickListener(view -> {
            if (variantName.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Title is empty", Toast.LENGTH_SHORT).show();
                return;
            } else if (variantPrice.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Price is empty", Toast.LENGTH_SHORT).show();
                return;
            } else if (variantQuantity.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Quantity is empty", Toast.LENGTH_SHORT).show();
                return;
            } else if (variantImages.isEmpty()) {
                Toast.makeText(this, "Images are empty", Toast.LENGTH_SHORT).show();
                return;
            }
            UsersSharedPrefManager.getInstance(this).saveVariantImages(variantImages);

            if (variantPos == -1)
                variantListForAdapter.add(new VariantsItem(
                        UsersSharedPrefManager.getInstance(this).getVariantImages(),
                        Integer.parseInt(variantId.getText().toString()),
                        Integer.parseInt(variantQuantity.getText().toString().trim()),
                        Integer.parseInt(variantQuantity.getText().toString().trim()),
                        Integer.parseInt(variantPrice.getText().toString().trim()),
                        variantName.getText().toString().trim()
                ));
            else
                variantListForAdapter.set(variantPos, new VariantsItem(
                        UsersSharedPrefManager.getInstance(this).getVariantImages(),
                        Integer.parseInt(variantId.getText().toString()),
                        Integer.parseInt(variantQuantity.getText().toString().trim()),
                        Integer.parseInt(variantQuantity.getText().toString().trim()),
                        Integer.parseInt(variantPrice.getText().toString().trim()),
                        variantName.getText().toString().trim()
                ));


            variantAdapter.notifyDataSetChanged();
            clearVariantData();

        });
    }

    public void loadVariantData(VariantsItem variantsItem, int pos) {
        variantPos = pos;
        variantName.setText(variantsItem.getVariantName());
        variantPrice.setText(String.valueOf(variantsItem.getPrice()));
        variantQuantity.setText(String.valueOf(variantsItem.getQuantity()));
        variantId.setText(String.valueOf(variantsItem.getVariantId()));
        variantImages = variantsItem.getImage();
        imageHelper();
    }

    private void clearVariantData() {
        variantName.setText("");
        variantPrice.setText("");
        variantQuantity.setText("");
        variantId.setText("0");
        variantPos = -1;
        variantImages.clear();
        imageHelper();
    }

    private void askPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
        } else
            accessGranted = true;
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

            if (variantImages.get(0).contains("content"))
                Glide.with(this)
                        .load(variantImages.get(0))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(image1);
            else
                Glide.with(this)
                        .load("https://sambalpurihaat.com/admin/images/vendor_product/" + variantImages.get(0))
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

            if (variantImages.get(0).contains("content"))
                Glide.with(this)
                        .load(variantImages.get(0))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(image1);
            else
                Glide.with(this)
                        .load("https://sambalpurihaat.com/admin/images/vendor_product/" + variantImages.get(0))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(image1);


            if (variantImages.get(1).contains("content"))
                Glide.with(this)
                        .load(variantImages.get(1))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(image2);
            else
                Glide.with(this)
                        .load("https://sambalpurihaat.com/admin/images/vendor_product/" + variantImages.get(1))
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

            if (variantImages.get(0).contains("content"))
                Glide.with(this)
                        .load(variantImages.get(0))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(image1);
            else
                Glide.with(this)
                        .load("https://sambalpurihaat.com/admin/images/vendor_product/" + variantImages.get(0))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(image1);

            if (variantImages.get(1).contains("content"))
                Glide.with(this)
                        .load(variantImages.get(1))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(image2);
            else
                Glide.with(this)
                        .load("https://sambalpurihaat.com/admin/images/vendor_product/" + variantImages.get(1))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(image2);

            if (variantImages.get(2).contains("content"))
                Glide.with(this)
                        .load(variantImages.get(2))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(image3);
            else
                Glide.with(this)
                        .load("https://sambalpurihaat.com/admin/images/vendor_product/" + variantImages.get(2))
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

            if (variantImages.get(0).contains("content"))
                Glide.with(this)
                        .load(variantImages.get(0))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(image1);
            else
                Glide.with(this)
                        .load("https://sambalpurihaat.com/admin/images/vendor_product/" + variantImages.get(0))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(image1);

            if (variantImages.get(1).contains("content"))
                Glide.with(this)
                        .load(variantImages.get(1))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(image2);
            else
                Glide.with(this)
                        .load("https://sambalpurihaat.com/admin/images/vendor_product/" + variantImages.get(1))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(image2);

            if (variantImages.get(2).contains("content"))
                Glide.with(this)
                        .load(variantImages.get(2))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(image3);
            else
                Glide.with(this)
                        .load("https://sambalpurihaat.com/admin/images/vendor_product/" + variantImages.get(2))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(image3);

            if (variantImages.get(3).contains("content"))
                Glide.with(this)
                        .load(variantImages.get(3))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(image4);
            else
                Glide.with(this)
                        .load("https://sambalpurihaat.com/admin/images/vendor_product/" + variantImages.get(3))
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

            if (variantImages.get(0).contains("content"))
                Glide.with(this)
                        .load(variantImages.get(0))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(image1);
            else
                Glide.with(this)
                        .load("https://sambalpurihaat.com/admin/images/vendor_product/" + variantImages.get(0))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(image1);

            if (variantImages.get(1).contains("content"))
                Glide.with(this)
                        .load(variantImages.get(1))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(image2);
            else
                Glide.with(this)
                        .load("https://sambalpurihaat.com/admin/images/vendor_product/" + variantImages.get(1))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(image2);

            if (variantImages.get(2).contains("content"))
                Glide.with(this)
                        .load(variantImages.get(2))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(image3);
            else
                Glide.with(this)
                        .load("https://sambalpurihaat.com/admin/images/vendor_product/" + variantImages.get(2))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(image3);

            if (variantImages.get(3).contains("content"))
                Glide.with(this)
                        .load(variantImages.get(3))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(image4);
            else
                Glide.with(this)
                        .load("https://sambalpurihaat.com/admin/images/vendor_product/" + variantImages.get(3))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(image4);

            if (variantImages.get(4).contains("content"))
                Glide.with(this)
                        .load(variantImages.get(4))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(image5);
            else
                Glide.with(this)
                        .load("https://sambalpurihaat.com/admin/images/vendor_product/" + variantImages.get(4))
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

    private void updateProduct() {
        if (validateMainData()) {
            if (variantListForAdapter != null) {
                if (helper.isNetworkConnected(this)) {
                    showLoading();
                    List<VariantsItem> newVariantList = new ArrayList<>();
                    for (VariantsItem b : variantListForAdapter) {
                        newVariantList.add(new VariantsItem(
                                helper.base64String(b.getImage(), this),
                                b.getVariantId(),
                                b.getQuantity(),
                                b.getQuantity(),
                                b.getPrice(),
                                b.getVariantName()
                        ));
                    }
                    RetrofitClient.getInstance().getApi().updateProduct(
                            product.getCompany(),
                            description.getText().toString().trim(),
                            product.getProductId(),
                            title.getText().toString().trim(),
                            UsersSharedPrefManager.getInstance(this).getUid(),
                            new Gson().toJson(newVariantList)
                    ).enqueue(new Callback<SaveResponse>() {
                        @Override
                        public void onResponse(Call<SaveResponse> call, Response<SaveResponse> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getError().equals("false")) {
                                    cancelLoading();
                                    startActivity(new Intent(EditProductActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SaveResponse> call, Throwable t) {
                            cancelLoading();
                            Toast.makeText(EditProductActivity.this, "Error in updating data", Toast.LENGTH_SHORT).show();
                            Log.e("TAG", "onFailure: " + t);
                        }
                    });
                } else {
                    Toast.makeText(this, "Connect to the internet to update the product", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please add variants", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void showLoading() {
        progressDialog.setMessage("Updating product...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void cancelLoading() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private boolean validateMainData() {
        return !title.getText().toString().trim().equals("");
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
            updateProduct();
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


