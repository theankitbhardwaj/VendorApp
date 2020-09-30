package com.ganesh.vendorapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.ganesh.vendorapp.R;
import com.ganesh.vendorapp.adapters.OutOfStockVariantAdapter;
import com.ganesh.vendorapp.models.ProductsItem;

public class OutofStockActivity extends AppCompatActivity {
    private ProductsItem product;
    private TextView title, description;
    private RecyclerView variantRecycler;
    private OutOfStockVariantAdapter outOfStockVariantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outof_stock);
        product = (ProductsItem) getIntent().getExtras().get("product");


        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        variantRecycler = findViewById(R.id.variantRecycler);


        title.setText(product.getTitle());
        description.setText(product.getDescription());
        outOfStockVariantAdapter = new OutOfStockVariantAdapter(product.getVariants(), this);
        variantRecycler.setLayoutManager(new LinearLayoutManager(this));
        variantRecycler.setAdapter(outOfStockVariantAdapter);


    }
}