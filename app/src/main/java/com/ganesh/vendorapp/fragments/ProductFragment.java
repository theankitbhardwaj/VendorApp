package com.ganesh.vendorapp.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
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
import com.ganesh.vendorapp.api.APIs;
import com.ganesh.vendorapp.api.RetrofitClient;
import com.ganesh.vendorapp.models.Products;
import com.ganesh.vendorapp.adapters.ProductsAdapter;
import com.ganesh.vendorapp.models.ProductsItem;
import com.ganesh.vendorapp.models.ProductsResponse;
import com.ganesh.vendorapp.models.SaveResponse;
import com.ganesh.vendorapp.storage.ProductRoom;
import com.ganesh.vendorapp.storage.UsersSharedPrefManager;
import com.ganesh.vendorapp.utils.RandomID;
import com.ganesh.vendorapp.viewmodel.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends Fragment {

    private static final String TAG = "ProductFragment";
    public RecyclerView recyclerViewProducts;
    public List<ProductsItem> productsItems;
    public ProductViewModel productViewModel;
    private ProductsAdapter productsAdapter;
    private APIs api;
    private RandomID randomID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productsItems = new ArrayList<>();
        productsAdapter = new ProductsAdapter();
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        recyclerViewProducts = view.findViewById(R.id.recycler_view_products);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        api = RetrofitClient.getInstance().getApi();
        randomID = new RandomID();

        api.getProducts(UsersSharedPrefManager.getInstance(getContext()).getUid())
                .enqueue(new Callback<ProductsResponse>() {
                    @Override
                    public void onResponse(Call<ProductsResponse> call, Response<ProductsResponse> response) {
                        if (response.isSuccessful()) {
                            Log.e(TAG, "onResponse: " + response.body().getMessage());
                            productsItems = response.body().getProducts();
                            productsAdapter.differ.submitList(productsItems);
                            recyclerViewProducts.setAdapter(productsAdapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductsResponse> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t);
                    }
                });

        if (productsItems.isEmpty()) {
            productViewModel.getProductList().observe(getViewLifecycleOwner(), productRooms -> {
                Log.e(TAG, "onCreateView: " + productRooms.size());
                List<Products> products = new ArrayList<>();
                products.clear();
                for (ProductRoom a : productRooms) {
                    products.add(new Products(
                            a.productId, a.userId, a.title, a.company, a.description, a.variants)
                    );
                    Log.e(TAG, "onViewCreated: " + products.toString());
                }

                if (isNetworkConnected() && !products.isEmpty()) {
                    for (int i = 0; i < products.size(); i++) {
                        Log.e(TAG, "Product: " + products.get(i).toString());
                        api.saveProducts(
                                products.get(i).getCompany(),
                                products.get(i).getDescription(),
                                products.get(i).getProduct_id(),
                                products.get(i).getTitle(),
                                products.get(i).getUid(),
                                products.get(i).getVariants().toString()
                        ).enqueue(new Callback<SaveResponse>() {

                            @Override
                            public void onResponse(Call<SaveResponse> call, Response<SaveResponse> response) {
                                if (response.isSuccessful()) {
                                    Log.e(TAG, "onResponse: " + response.body().getMessage());
                                }
                            }

                            @Override
                            public void onFailure(Call<SaveResponse> call, Throwable t) {
                                Log.e(TAG, "onFailure: " + t);
                            }

                        });
                    }
                }
            });

        }


    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
