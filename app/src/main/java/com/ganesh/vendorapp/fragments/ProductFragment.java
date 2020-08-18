package com.ganesh.vendorapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ganesh.vendorapp.R;
import com.ganesh.vendorapp.models.Products;
import com.ganesh.vendorapp.adapters.ProductsAdapter;
import com.ganesh.vendorapp.storage.AppDatabase;
import com.ganesh.vendorapp.storage.ProductRoom;
import com.ganesh.vendorapp.viewmodel.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment {

    private static final String TAG = "ProductFragment";
    public RecyclerView recyclerViewProducts;
    public List<Products> products;
    public ProductViewModel productViewModel;
    private ProductsAdapter productsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        products = new ArrayList<>();
        productsAdapter = new ProductsAdapter();
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        recyclerViewProducts = view.findViewById(R.id.recycler_view_products);

        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        productViewModel.getProductList().observe(getViewLifecycleOwner(), productRooms -> {
            Log.e(TAG, "onCreateView: " + productRooms.size());
            products.clear();
            for (ProductRoom a : productRooms) {
                products.add(new Products(a.title, a.company, a.description, a.variants));
            }
            productsAdapter.differ.submitList(products);
            recyclerViewProducts.setAdapter(productsAdapter);
        });

    }
}
