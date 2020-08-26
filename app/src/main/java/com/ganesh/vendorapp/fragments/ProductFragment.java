package com.ganesh.vendorapp.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ganesh.vendorapp.R;
import com.ganesh.vendorapp.activities.MainActivity;
import com.ganesh.vendorapp.activities.ProfileActivity;
import com.ganesh.vendorapp.api.APIs;
import com.ganesh.vendorapp.api.RetrofitClient;
import com.ganesh.vendorapp.models.Products;
import com.ganesh.vendorapp.adapters.ProductsAdapter;
import com.ganesh.vendorapp.models.ProductsItem;
import com.ganesh.vendorapp.models.ProductsResponse;
import com.ganesh.vendorapp.models.SaveResponse;
import com.ganesh.vendorapp.models.Variants;
import com.ganesh.vendorapp.models.VariantsItem;
import com.ganesh.vendorapp.storage.ProductDao;
import com.ganesh.vendorapp.storage.ProductRoom;
import com.ganesh.vendorapp.storage.SavedProductRoom;
import com.ganesh.vendorapp.storage.UsersSharedPrefManager;
import com.ganesh.vendorapp.utils.Helper;
import com.ganesh.vendorapp.viewmodel.ProductViewModel;
import com.ganesh.vendorapp.viewmodel.SavedProductViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStreamWriter;
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
    private SavedProductRoom productRoom;
    private SavedProductViewModel savedProductViewModel;
    private SwipeRefreshLayout refresher;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_delete:
                deleteProducts(productsAdapter.getSelectedProducts());
                break;
        }
        return super.onOptionsItemSelected(item);
    }


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
        refresher = view.findViewById(R.id.refresher);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        api = RetrofitClient.getInstance().getApi();
        progressDialog = new ProgressDialog(getContext());

        //get products from api and save in local db
        getProducts();

        refresher.setOnRefreshListener(this::getProducts);

        checkAndSaveToServer();

    }

    private void checkAndSaveToServer() {
        //Fetching products from local db and checking if saved to server or not
        productViewModel.getProductList().observe(getViewLifecycleOwner(), productRooms -> {
            if (!productRooms.isEmpty()) {
                Log.e(TAG, "Product room size: " + productRooms.size());
                List<Products> localProducts = new ArrayList<>();
                for (ProductRoom a : productRooms) {
                    localProducts.add(new Products(a.productId, a.userId, a.description, a.company, a.title, a.variants));
                }

                //Products which are fetched and saved in local db2
                savedProductViewModel.getProductList().observe(getViewLifecycleOwner(), savedProductRooms -> {
                    if (!savedProductRooms.isEmpty()) {
                        Log.e(TAG, "Saved room size: " + savedProductRooms.size());
                        List<ProductsItem> serverProducts = new ArrayList<>();
                        List<String> productIds = new ArrayList<>();
                        for (SavedProductRoom a : savedProductRooms) {
                            serverProducts.add(new ProductsItem(
                                    a.productId, a.description, a.company, a.title, a.variants)
                            );
                        }
                        for (ProductsItem a : serverProducts) {
                            productIds.add(a.getProductId());
                        }
                        for (Products a : localProducts) {
                            if (localProducts.size() > serverProducts.size() && !productIds.contains(a.getProduct_id())) {
                                Log.e(TAG, "Sending products to server: local products " + localProducts.size() + "\n" + "Server products " + savedProductRooms.size());
                                api.saveProducts(
                                        a.getCompany(),
                                        a.getDescription(),
                                        a.getProduct_id(),
                                        a.getTitle(),
                                        a.getUid(),
                                        new Gson().toJson(a.getVariants())
                                ).enqueue(new Callback<SaveResponse>() {
                                    @Override
                                    public void onResponse(Call<SaveResponse> call, Response<SaveResponse> response) {
                                        if (response.isSuccessful()) {
                                            Log.e(TAG, "onResponse: " + response.body().getMessage());
                                            if (response.body().getError().equals("false")) {
                                                Log.e(TAG, "Product: " + a.getTitle() + " is saved successfully");
                                                Toast.makeText(getContext(), "Product: " + a.getTitle() + " is saved successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<SaveResponse> call, Throwable t) {
                                        Log.e(TAG, "onFailure: " + t);
                                    }
                                });
                            }
                        }
                    } else {
                        //Products which are only saved in local db
                        Log.e(TAG, "Sending products to server: Fresh start");
                        for (Products a : localProducts) {
                            api.saveProducts(
                                    a.getCompany(),
                                    a.getDescription(),
                                    a.getProduct_id(),
                                    a.getTitle(),
                                    a.getUid(),
                                    new Gson().toJson(a.getVariants())
                            ).enqueue(new Callback<SaveResponse>() {
                                @Override
                                public void onResponse(Call<SaveResponse> call, Response<SaveResponse> response) {
                                    if (response.isSuccessful()) {
                                        Log.e(TAG, "onResponse: " + response.body().getMessage());
                                        if (response.body().getError().equals("false")) {
                                            Log.e(TAG, "Product: " + a.getTitle() + " is saved successfully");
                                            Toast.makeText(getContext(), "Product: " + a.getTitle() + " is saved successfully", Toast.LENGTH_SHORT).show();
                                        }
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
        });
    }

    private void deleteProducts(List<ProductsItem> selectedProducts) {
        if (selectedProducts != null) {
            List<ProductsItem> whole = productsAdapter.getAdapterList();
            for (ProductsItem a : selectedProducts) {
                whole.remove(a);
                ProductRoom productRoom = new ProductRoom();
                productRoom.productId = a.getProductId();
                productRoom.title = a.getTitle();
                productViewModel.delete(productRoom);
                SavedProductRoom deleteProduct = new SavedProductRoom();
                deleteProduct.productId = a.getProductId();
                deleteProduct.title = a.getTitle();
                savedProductViewModel.delete(deleteProduct);
            }
            productsAdapter.notifyDataSetChanged();
        }
    }

    private void showLoading() {
        progressDialog.setMessage("Loading products...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void cancelLoading() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void showSavedProducts() {
        savedProductViewModel.getProductList().observe(getViewLifecycleOwner(), savedProductRooms -> {
            if (!savedProductRooms.isEmpty()) {
                List<ProductsItem> savedProducts = new ArrayList<>();
                Log.e(TAG, "savedProductViewModel: " + savedProductRooms.size());
                for (SavedProductRoom a : savedProductRooms) {
                    savedProducts.add(new ProductsItem(a.productId, a.description, a.company, a.title, a.variants));
                    Log.e(TAG, "Saved Product: " + a.productId);
                }
                Log.e(TAG, "savedProducts: " + savedProducts.size());
                productsAdapter = new ProductsAdapter(savedProducts, getContext());
                recyclerViewProducts.setAdapter(productsAdapter);
                cancelLoading();
                refresher.setRefreshing(false);
            }
        });
    }

    private void writeToAFile(List<String> image) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getContext().openFileOutput("confi70.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(image.get(0));
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void getProducts() {
        if (isNetworkConnected()) {
            showLoading();
            api.getProducts(UsersSharedPrefManager.getInstance(getContext()).getUid())
                    .enqueue(new Callback<ProductsResponse>() {
                        @Override
                        public void onResponse(Call<ProductsResponse> call, Response<ProductsResponse> response) {
                            if (response.isSuccessful()) {
                                productsItems.clear();
                                productsItems = response.body().getProducts();
                                if (productsItems != null) {
                                    Log.e(TAG, "Products from Api: " + productsItems.size());
                                    for (ProductsItem a : productsItems) {
                                        Log.e(TAG, "Product from API: " + a.toString());
                                        productRoom = new SavedProductRoom();
                                        productRoom.title = a.getTitle();
                                        productRoom.company = a.getCompany();
                                        productRoom.description = a.getDescription();
                                        productRoom.productId = a.getProductId();
                                        productRoom.userId = UsersSharedPrefManager.getInstance(getContext()).getUid();
                                        productRoom.variants = a.getVariants();
                                        savedProductViewModel.insert(productRoom);

                                        ProductRoom localdb = new ProductRoom();
                                        localdb.title = a.getTitle();
                                        localdb.productId = a.getProductId();
                                        localdb.userId = UsersSharedPrefManager.getInstance(getContext()).getUid();
                                        localdb.description = a.getDescription();
                                        localdb.company = a.getCompany();
                                        List<Variants> variants = new ArrayList<>();
                                        for (VariantsItem b : a.getVariants()) {
                                            variants.add(new Variants(b.getVariantName(), b.getQuantity(), b.getPrice(), b.getImage()));
                                        }
                                        localdb.variants = variants;
                                        productViewModel.insert(localdb);
                                    }
                                    showSavedProducts();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ProductsResponse> call, Throwable t) {
                            Log.e(TAG, "onFailure: " + t);
                            Toast.makeText(getContext(), "Connection timed out", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
