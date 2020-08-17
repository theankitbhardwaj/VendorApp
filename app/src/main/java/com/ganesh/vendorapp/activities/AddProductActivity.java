package com.ganesh.vendorapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ganesh.vendorapp.R;
import com.ganesh.vendorapp.models.Variants;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class AddProductActivity extends AppCompatActivity {

    private LinearLayout variantLayoutList;
    private TextView tv_counter_variant;
    private ArrayList<Variants> variantList = new ArrayList<>();
    private TextInputEditText et_item_title, et_item_company,et_item_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitvity_add_product);
        getSupportActionBar().setTitle("Add Product Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_item_title = findViewById(R.id.et_item_title);
        et_item_company = findViewById(R.id.et_item_company);
        et_item_desc = findViewById(R.id.et_item_desc);

        findViewById(R.id.main_layout_add_product).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    InputMethodManager inputMethodManager = (InputMethodManager) AddProductActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(AddProductActivity.this.getCurrentFocus().getWindowToken(), 0);
                }
            }
        });

        tv_counter_variant = findViewById(R.id.tv_count_variant);
        variantLayoutList = findViewById(R.id.add_variant_list);

        findViewById(R.id.add_variant_btn).setOnClickListener(view -> {
            addView();
        });

    }

    private void saveProductItem(){
        String item_title = et_item_title.getText().toString().trim();
        String item_company = et_item_company.getText().toString().trim();
        String item_desc = et_item_desc.getText().toString().trim();

        if(item_title.isEmpty()){
            et_item_title.setError("Required Pin No.");
            et_item_title.requestFocus();
            return;
        }
        if(checkIfValidAndReadVariants()){

//            Intent intent = new Intent(AddProductActivity.this,MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            Bundle bundle = new Bundle();
//            bundle.putString("title",item_title);
//            bundle.putString("company",item_company);
//            bundle.putString("description",item_desc);
//            bundle.putSerializable("variant_list",variantList);
//            intent.putExtras(bundle);
//            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.save_product_btn){
            saveProductItem();
        }
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkIfValidAndReadVariants() {
        variantList.clear();
        boolean result = true;

        for(int i=0; i<variantLayoutList.getChildCount(); i++){

            View variantView = variantLayoutList.getChildAt(i);
            TextInputEditText et_variant_name = variantView.findViewById(R.id.et_variant_name);
            TextInputEditText et_variant_quantity = variantView.findViewById(R.id.et_variant_quantity);
            TextInputEditText et_variant_price = variantView.findViewById(R.id.et_variant_price);

            if(et_variant_name.getText().toString().trim().equals("")){
                result = false;
                break;
            }

            if(et_variant_quantity.getText().toString().trim().equals("") ||
                    Integer.parseInt(et_variant_quantity.getText().toString().trim())<1){
                result = false;
                break;
            }

            if(et_variant_price.getText().toString().trim().equals("") ||
                    Integer.parseInt(et_variant_price.getText().toString().trim())<1){
                result = false;
                break;
            }

            variantList.add( new Variants(
                    et_variant_name.getText().toString() ,
                    Integer.parseInt(et_variant_quantity.getText().toString()) ,
                    Double.parseDouble(et_variant_price.getText().toString())
                    )
            );
        }
        if(variantList.size() == 0){
            result = false;
            Toast.makeText(this, "Add Variant First!", Toast.LENGTH_SHORT).show();
        } else if (!result) {

            Toast.makeText(this, "Enter All Details Correctly!", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void addView(){
        View variantView = getLayoutInflater().inflate(R.layout.row_add_variant,null,false);

        variantView.findViewById(R.id.close_imageButton).setOnClickListener(view -> {
            removeView(variantView);
        });

        variantLayoutList.addView(variantView);
        tv_counter_variant.setText(variantLayoutList.getChildCount()+"");
    }

    private void removeView(View view){
        variantLayoutList.removeView(view);
        tv_counter_variant.setText(variantLayoutList.getChildCount()+"");
    }
}