package com.ganesh.vendorapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ganesh.vendorapp.R;
import com.ganesh.vendorapp.models.Products;
import com.ganesh.vendorapp.models.ProductsAdapter;

import java.util.ArrayList;

public class ProductFragment extends Fragment {

    private static final String TAG = "ProductFragment";

    public RecyclerView recyclerViewProducts;
    public ArrayList<Products> products;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product,container,false);

        products = new ArrayList<>();
        recyclerViewProducts = view.findViewById(R.id.recycler_view_products);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerViewProducts.setLayoutManager(layoutManager);

        recyclerViewProducts.setAdapter(new ProductsAdapter(products));
        return view;
    }

}
