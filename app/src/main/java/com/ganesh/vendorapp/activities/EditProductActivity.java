package com.ganesh.vendorapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ganesh.vendorapp.R;
import com.ganesh.vendorapp.adapters.EditVariantAdapter;
import com.ganesh.vendorapp.api.RetrofitClient;
import com.ganesh.vendorapp.models.ProductsItem;
import com.ganesh.vendorapp.models.SaveResponse;
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

    private EditText title, company, description;
    private RecyclerView variantRecycler;
    private EditVariantAdapter variantAdapter;
    private List<VariantsItem> variantsList;
    private ProductsItem product;
    private int imagePos;
    private Helper helper;
    private ProductViewModel productViewModel;
    private TextView variantCount;
    private static final int PICK_IMAGE = 103;
    private static final int STORAGE_PERMISSION = 105;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitvity_edit_product);
        getSupportActionBar().setTitle("Edit Product Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        product = (ProductsItem) getIntent().getExtras().get("product");

        title = findViewById(R.id.et_item_title);
        company = findViewById(R.id.et_item_company);
        description = findViewById(R.id.et_item_desc);
        variantCount = findViewById(R.id.tv_count_variant);
        variantRecycler = findViewById(R.id.variantRecycler);

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        helper = new Helper();
        variantRecycler.setLayoutManager(new LinearLayoutManager(this));

        title.setText(product.getTitle());
        company.setText(product.getCompany());
        description.setText(product.getDescription());
        variantAdapter = new EditVariantAdapter(product.getVariants(), this);
        variantRecycler.setAdapter(variantAdapter);
    }

    private void updateProduct() {
        if (validateMainData()) {
            List<VariantsItem> data = getVariantData();
            if (data != null) {
                RetrofitClient.getInstance().getApi().updateProduct(
                        company.getText().toString().trim(),
                        description.getText().toString().trim(),
                        product.getProductId(),
                        title.getText().toString().trim(),
                        UsersSharedPrefManager.getInstance(this).getUid(),
                        new Gson().toJson(data)
                ).enqueue(new Callback<SaveResponse>() {
                    @Override
                    public void onResponse(Call<SaveResponse> call, Response<SaveResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getError().equals("false")) {
                                startActivity(new Intent(EditProductActivity.this, MainActivity.class));
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SaveResponse> call, Throwable t) {
                        Toast.makeText(EditProductActivity.this, "Error in updating data", Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "onFailure: " + t);
                    }
                });

            } else {
                Toast.makeText(this, "Please add variants", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
        }
    }

    private List<VariantsItem> getVariantData() {
        List<VariantsItem> variants = new ArrayList<>();
        List<VariantsItem> temp = variantAdapter.getArrayList();

        for (int i = 0; i < temp.size(); i++) {
            if (helper.validateVariantData(temp.get(i))) {
                variants.add(new VariantsItem(checkImageUrl(temp.get(i).getImage()),
                        temp.get(i).getVariantId(),
                        temp.get(i).getQuantity(),
                        temp.get(i).getPrice(),
                        temp.get(i).getVariantName()));
            } else {
                Toast.makeText(this, "Variant at " + (i + 1) + " is not completed", Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        if (variants.isEmpty())
            return null;
        return variants;
    }

    private List<String> checkImageUrl(List<String> image) {
        List<String> imageUrls = new ArrayList<>();
        for (String a : image) {
            if (a.contains("content"))
                imageUrls.add(helper.base64String(Uri.parse(a), this));
            else
                imageUrls.add(a);
        }
        return imageUrls;
    }


    private boolean validateMainData() {
        if (title.getText().toString().trim().equals(""))
            return false;
        else if (company.getText().toString().trim().equals(""))
            return false;
        else return !description.getText().toString().trim().equals("");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            List<String> images;
            VariantsItem temp = variantAdapter.getArrayList().get(imagePos);
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


