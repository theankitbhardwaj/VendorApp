package com.ganesh.vendorapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ganesh.vendorapp.R;
import com.ganesh.vendorapp.adapters.ImageAdapter;
import com.ganesh.vendorapp.models.Variants;
import com.ganesh.vendorapp.storage.ProductRoom;
import com.ganesh.vendorapp.storage.UsersSharedPrefManager;
import com.ganesh.vendorapp.utils.Helper;
import com.ganesh.vendorapp.viewmodel.ProductViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {

    private LinearLayout variantLayoutList;
    private TextView tv_counter_variant;
    private ArrayList<Variants> variantList = new ArrayList<>();
    private TextInputEditText et_item_title, et_item_company, et_item_desc;
    private ProductRoom productRoom;
    private ProductViewModel productViewModel;
    private List<String> images;
    private List<List<String>> tempImages;
    private static final int PICK_IMAGE = 103;
    private static final int STORAGE_PERMISSION = 105;
    private ImageAdapter imageAdapter;
    private Helper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitvity_add_product);
        getSupportActionBar().setTitle("Add Product Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_item_title = findViewById(R.id.et_item_title);
        et_item_company = findViewById(R.id.et_item_company);
        et_item_desc = findViewById(R.id.et_item_desc);
        images = new ArrayList<>();
        tempImages = new ArrayList<>();
        helper = new Helper();

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);

        findViewById(R.id.main_layout_add_product).setOnFocusChangeListener((view, b) -> {
            if (b) {
                InputMethodManager inputMethodManager = (InputMethodManager) AddProductActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(AddProductActivity.this.getCurrentFocus().getWindowToken(), 0);
            }
        });

        tv_counter_variant = findViewById(R.id.tv_count_variant);
        variantLayoutList = findViewById(R.id.add_variant_list);

        askPermission();

        findViewById(R.id.add_variant_btn).setOnClickListener(view -> {
            addView();
        });

    }

    private void askPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
        }
    }

    private void saveProductItem() {
        String item_title = et_item_title.getText().toString().trim();
        String item_company = et_item_company.getText().toString().trim();
        String item_desc = et_item_desc.getText().toString().trim();

        if (item_title.isEmpty()) {
            et_item_title.setError("Required Pin No.");
            et_item_title.requestFocus();
            return;
        }

        if (checkIfValidAndReadVariants()) {
            productRoom = new ProductRoom();
            productRoom.productId = helper.getRandomID();
            productRoom.userId = "11254";
            productRoom.title = item_title;
            productRoom.company = item_company;
            productRoom.description = item_desc;
            productRoom.variants = variantList;
            productViewModel.insert(productRoom);
            finish();
        }

    }

    private void addView() {
       /* View variantView = getLayoutInflater().inflate(R.layout.row_add_variant, null, false);
//        RecyclerView recyclerView = variantView.findViewById(R.id.imageRecycler);

        tempImages.add(variantLayoutList.getChildCount(), new ArrayList<>());
        variantLayoutList.addView(variantView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        variantView.findViewById(R.id.close_imageButton).setOnClickListener(view -> {
            removeView(variantView);
        });

        variantView.findViewById(R.id.add_variant_image).setOnClickListener(view -> {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

            imageAdapter = new ImageAdapter(tempImages.get(variantLayoutList.getChildCount() - 1), this);
            recyclerView.setAdapter(imageAdapter);

        });

        tv_counter_variant.setText(variantLayoutList.getChildCount() + "");*/
    }

    private void removeView(View view) {

        variantLayoutList.removeView(view);
        tv_counter_variant.setText(variantLayoutList.getChildCount() + "");
    }

    private boolean checkIfValidAndReadVariants() {
        variantList.clear();
        boolean result = true;

        for (int i = 0; i < variantLayoutList.getChildCount(); i++) {

            View variantView = variantLayoutList.getChildAt(i);
            TextInputEditText et_variant_name = variantView.findViewById(R.id.et_variant_name);
            TextInputEditText et_variant_quantity = variantView.findViewById(R.id.et_variant_quantity);
            TextInputEditText et_variant_price = variantView.findViewById(R.id.et_variant_price);

            if (et_variant_name.getText().toString().trim().equals("")) {
                result = false;
                break;
            }

            if (et_variant_quantity.getText().toString().trim().equals("") ||
                    Integer.parseInt(et_variant_quantity.getText().toString().trim()) < 1) {
                result = false;
                break;
            }

            if (et_variant_price.getText().toString().trim().equals("") ||
                    Integer.parseInt(et_variant_price.getText().toString().trim()) < 1) {
                result = false;
                break;
            }
            UsersSharedPrefManager.getInstance(this).temp(helper.base64String(tempImages.get(i), this).get(0));
            variantList.add(new Variants(
                    et_variant_name.getText().toString(),
                    Integer.parseInt(et_variant_quantity.getText().toString()),
                    Double.parseDouble(et_variant_price.getText().toString()),
                    tempImages.get(i)));

        }
        if (variantList.size() == 0) {
            result = false;
            Toast.makeText(this, "Add Variant First!", Toast.LENGTH_SHORT).show();
        } else if (!result) {
            Toast.makeText(this, "Enter All Details Correctly!", Toast.LENGTH_SHORT).show();
        }
        return result;
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri image = data.getData();
            tempImages.get(variantLayoutList.getChildCount() - 1).add(String.valueOf(image));
            imageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED && requestCode != STORAGE_PERMISSION) {
            Toast.makeText(this, "Storage Permission is required", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}


