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
import androidx.lifecycle.Observer;
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
import com.ganesh.vendorapp.storage.SavedProductRoom;
import com.ganesh.vendorapp.storage.UsersSharedPrefManager;
import com.ganesh.vendorapp.utils.Helper;
import com.ganesh.vendorapp.viewmodel.ProductViewModel;
import com.ganesh.vendorapp.viewmodel.SavedProductViewModel;
import com.google.gson.Gson;

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
    private Helper helper;
    private SavedProductRoom productRoom;
    private SavedProductViewModel savedProductViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productsItems = new ArrayList<>();
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        savedProductViewModel = ViewModelProviders.of(this).get(SavedProductViewModel.class);
        recyclerViewProducts = view.findViewById(R.id.recycler_view_products);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        api = RetrofitClient.getInstance().getApi();
        helper = new Helper();

        //get products from api and save in local db
        getProducts();


        //Send products through api
        productViewModel.getProductList().observe(getViewLifecycleOwner(), productRooms -> {
            List<Products> products = new ArrayList<>();
            products.clear();
            for (ProductRoom a : productRooms) {
                products.add(new Products(
                        a.productId, a.userId, a.title, a.company, a.description, a.variants)
                );
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
                            new Gson().toJson(products.get(i).getVariants())
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

        //get products from local db
        savedProductViewModel.getProductList().observe(getViewLifecycleOwner(), savedProductRooms -> {
            List<ProductsItem> temp = new ArrayList<>();
            for (SavedProductRoom a : savedProductRooms) {
                temp.add(new ProductsItem(a.productId, a.description, a.company, a.title, a.variants));
            }
            productsAdapter = new ProductsAdapter(temp, getContext());
            recyclerViewProducts.setAdapter(productsAdapter);
        });


    }

    private void saveProducts() {
        Log.e(TAG, "onViewCreated: " + productsItems.toString());
        if (productsItems.isEmpty()) {


        }
    }

    private void getProducts() {
        if (isNetworkConnected() && productsItems.isEmpty()) {
            api.getProducts(UsersSharedPrefManager.getInstance(getContext()).getUid())
                    .enqueue(new Callback<ProductsResponse>() {
                        @Override
                        public void onResponse(Call<ProductsResponse> call, Response<ProductsResponse> response) {
                            if (response.isSuccessful()) {
                                Log.e(TAG, "onResponse: " + response.body().getMessage());
                                productsItems.clear();
                                productsItems = response.body().getProducts();
                                for (ProductsItem a : productsItems) {
                                    productRoom = new SavedProductRoom();
                                    productRoom.title = a.getTitle();
                                    productRoom.company = a.getCompany();
                                    productRoom.description = a.getDescription();
                                    productRoom.productId = a.getProductId();
                                    productRoom.userId = UsersSharedPrefManager.getInstance(getContext()).getUid();
                                    productRoom.variants = a.getVariants();
                                    savedProductViewModel.insert(productRoom);
                                }
                                productsAdapter = new ProductsAdapter(productsItems, getContext());
                                recyclerViewProducts.setAdapter(productsAdapter);
                            }
                        }

                        @Override
                        public void onFailure(Call<ProductsResponse> call, Throwable t) {
                            Log.e(TAG, "onFailure: " + t);
                        }
                    });
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
