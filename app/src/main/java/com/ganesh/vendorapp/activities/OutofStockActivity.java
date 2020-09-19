package com.ganesh.vendorapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.ganesh.vendorapp.R;
import com.ganesh.vendorapp.adapters.DisplayVariantAdapter;
import com.ganesh.vendorapp.models.ProductsItem;
import com.ganesh.vendorapp.models.VariantsItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class OutofStockActivity extends AppCompatActivity {
    private ProductsItem product;
    private TextView title, description;
    private RecyclerView variantRecycler;
    private DisplayVariantAdapter displayVariantAdapter;

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
        displayVariantAdapter = new DisplayVariantAdapter(checkQuantity(product.getVariants()), this);
        variantRecycler.setLayoutManager(new LinearLayoutManager(this));
        variantRecycler.setAdapter(displayVariantAdapter);


    }

    private List<VariantsItem> checkQuantity(List<VariantsItem> variants) {
        List<VariantsItem> finalList = new ArrayList<>();
        for (int i = 0; i < variants.size(); i++) {
            if (variants.get(i).getQuantity() == 0)
                finalList.add(variants.get(i));
        }
        return finalList;
    }
}