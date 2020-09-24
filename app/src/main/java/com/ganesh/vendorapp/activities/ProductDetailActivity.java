package com.ganesh.vendorapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ganesh.vendorapp.R;
import com.ganesh.vendorapp.adapters.DisplayVariantAdapter;
import com.ganesh.vendorapp.adapters.ImageSliderAdapter;
import com.ganesh.vendorapp.models.ProductsItem;
import com.ganesh.vendorapp.models.VariantsItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.smarteist.autoimageslider.SliderView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    private ProductsItem product;
    private TextView title, description;
    private RecyclerView variantRecycler;
    private DisplayVariantAdapter displayVariantAdapter;
    private FloatingActionButton edit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        product = (ProductsItem) getIntent().getExtras().get("product");


        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        variantRecycler = findViewById(R.id.variantRecycler);
        edit = findViewById(R.id.editProduct);


        title.setText(product.getTitle());
        description.setText(product.getDescription());
        displayVariantAdapter = new DisplayVariantAdapter(product.getVariants(), this);
        variantRecycler.setLayoutManager(new LinearLayoutManager(this));
        variantRecycler.setAdapter(displayVariantAdapter);

        edit.setOnClickListener(view -> {
            Intent intent = new Intent(ProductDetailActivity.this, EditProductActivity.class);
            intent.putExtra("product", product);
            startActivity(intent);
        });
    }


}