package com.ganesh.vendorapp.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ganesh.vendorapp.R;
import com.ganesh.vendorapp.adapters.ImageSliderAdapter;
import com.ganesh.vendorapp.api.APIs;
import com.ganesh.vendorapp.api.RetrofitClient;
import com.ganesh.vendorapp.adapters.ProductsAdapter;
import com.ganesh.vendorapp.models.ProductsItem;
import com.ganesh.vendorapp.models.ProductsResponse;
import com.ganesh.vendorapp.storage.ProductRoom;
import com.ganesh.vendorapp.storage.SavedProductRoom;
import com.ganesh.vendorapp.storage.UsersSharedPrefManager;
import com.ganesh.vendorapp.utils.Helper;
import com.ganesh.vendorapp.viewmodel.ProductViewModel;
import com.ganesh.vendorapp.viewmodel.SavedProductViewModel;
import com.smarteist.autoimageslider.SliderView;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends Fragment {

    private static final String TAG = "ProductFragment";
    public RecyclerView productRecycler;
    public List<ProductsItem> productsItems;
    public ProductViewModel productViewModel;
    private ProductsAdapter productsAdapter;
    private APIs api;
    private SliderView imageSlider;
    private ImageSliderAdapter imageSliderAdapter;
    private SavedProductRoom savedProductRoom;
    private SavedProductViewModel savedProductViewModel;
    private SwipeRefreshLayout refresher;
    private ProgressDialog progressDialog;
    private Helper helper;
    private LinearLayout noData;
    private List<String> sliderImages;


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
        productRecycler = view.findViewById(R.id.recycler_view_products);
        refresher = view.findViewById(R.id.refresher);
        noData = view.findViewById(R.id.noDataLayout);
        imageSlider = view.findViewById(R.id.imageSlider);
        productRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        api = RetrofitClient.getInstance().getApi();
        progressDialog = new ProgressDialog(getContext());
        helper = new Helper();
        sliderImages = new ArrayList<>();

        noData.setVisibility(View.VISIBLE);
        productRecycler.setVisibility(View.GONE);


        Log.e(TAG, "TimeStamp: " + helper.getTimeStamp());
        sliderImages.add("https://sambalpurihaat.com/admin/images/banners/vendor_banner1.jpg");
        sliderImages.add("https://sambalpurihaat.com/admin/images/banners/vendor_banner2.jpg");
        sliderImages.add("https://sambalpurihaat.com/admin/images/banners/vendor_banner3.jpg");
        imageSliderAdapter = new ImageSliderAdapter(getContext(), sliderImages);
        imageSlider.setSliderAdapter(imageSliderAdapter);
        //get products from local db
        showSavedProducts();

        //refreshes data with api data
        refresher.setOnRefreshListener(this::getProducts);

    }

    /*private void checkAndSaveToServer() {
        List<Products> localProducts = new ArrayList<>();
        //Fetching products from local db and checking if saved to server or not
        productViewModel.getProductList().observe(getViewLifecycleOwner(), productRooms -> {
            if (!productRooms.isEmpty()) {
                Log.e(TAG, "Product room size: " + productRooms.size());
                for (ProductRoom a : productRooms) {
                    localProducts.add(new Products(a.productId, a.userId, a.title, a.company, a.description, a.variants));
                }
            }
        });

        //Products which are fetched and saved in local db2
        savedProductViewModel.getProductList().observe(getViewLifecycleOwner(), savedProductRooms -> {
            if (!localProducts.isEmpty()) {
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
                            List<String> deletedId;
                            deletedId = UsersSharedPrefManager.getInstance(getContext()).getDeletedProducts();
                            if (deletedId != null) {
                                if (!deletedId.contains(a.getProduct_id())) {
                                    List<Variants> newVariantList = new ArrayList<>();
                                    for (Variants b : a.getVariants()) {
                                        newVariantList.add(new Variants(
                                                b.getVariant_name(),
                                                b.getQuantity(),
                                                b.getPrice(),
                                                helper.base64String(b.getImage(), getContext())
                                        ));
                                    }
                                    api.saveProducts(
                                            a.getCompany(),
                                            a.getDescription(),
                                            a.getProduct_id(),
                                            a.getTitle(),
                                            a.getUid(),
                                            new Gson().toJson(newVariantList)
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
                            } else {
                                List<Variants> newVariantList = new ArrayList<>();
                                for (Variants b : a.getVariants()) {
                                    newVariantList.add(new Variants(
                                            b.getVariant_name(),
                                            b.getQuantity(),
                                            b.getPrice(),
                                            helper.base64String(b.getImage(), getContext())
                                    ));
                                }
                                api.saveProducts(
                                        a.getCompany(),
                                        a.getDescription(),
                                        a.getProduct_id(),
                                        a.getTitle(),
                                        a.getUid(),
                                        new Gson().toJson(newVariantList)
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
                    }
                } else {
                    //Products which are only saved in local db
                    Log.e(TAG, "Sending products to server: Fresh start");
                    for (Products a : localProducts) {
                        List<Variants> newVariantList = new ArrayList<>();
                        for (Variants b : a.getVariants()) {
                            newVariantList.add(new Variants(
                                    b.getVariant_name(),
                                    b.getQuantity(),
                                    b.getPrice(),
                                    helper.base64String(b.getImage(), getContext())
                            ));
                        }
                        api.saveProducts(
                                a.getCompany(),
                                a.getDescription(),
                                a.getProduct_id(),
                                a.getTitle(),
                                a.getUid(),
                                new Gson().toJson(newVariantList)
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
            }
        });

    }*/

    private void deleteProducts(List<ProductsItem> selectedProducts) {
        if (selectedProducts != null) {
            List<ProductsItem> whole = productsAdapter.getAdapterList();
            for (ProductsItem a : selectedProducts) {
                whole.remove(a);

                SavedProductRoom deleteProduct = new SavedProductRoom();
                deleteProduct.productId = a.getProductId();
                deleteProduct.title = a.getTitle();
                savedProductViewModel.delete(deleteProduct);

                productsAdapter.notifyDataSetChanged();
            }
            if (productsAdapter.getAdapterList().isEmpty()) {
                noData.setVisibility(View.VISIBLE);
                productRecycler.setVisibility(View.GONE);
            }
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
        if (!productsItems.isEmpty()) {
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
                    productRecycler.setAdapter(productsAdapter);
                }
            });
        } else
            getProducts();
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
        if (helper.isNetworkConnected(getContext())) {
            showLoading();
            api.getProducts(UsersSharedPrefManager.getInstance(getContext()).getUid())
                    .enqueue(new Callback<ProductsResponse>() {
                        @Override
                        public void onResponse(Call<ProductsResponse> call, Response<ProductsResponse> response) {
                            if (response.isSuccessful()) {
                                productsItems = response.body().getProducts();
                                if (productsItems != null) {
                                    noData.setVisibility(View.GONE);
                                    productRecycler.setVisibility(View.VISIBLE);
                                    for (ProductsItem a : productsItems) {
                                        Log.e(TAG, "Product from API: " + a.toString());
                                        savedProductRoom = new SavedProductRoom();
                                        savedProductRoom.title = a.getTitle();
                                        savedProductRoom.company = a.getCompany();
                                        savedProductRoom.description = a.getDescription();
                                        savedProductRoom.productId = a.getProductId();
                                        savedProductRoom.userId = UsersSharedPrefManager.getInstance(getContext()).getUid();
                                        savedProductRoom.variants = a.getVariants();
                                        savedProductViewModel.insert(savedProductRoom);
                                    }
                                    showSavedProducts();
                                } else {
                                    noData.setVisibility(View.VISIBLE);
                                    productRecycler.setVisibility(View.GONE);
                                }
                            }
                            refresher.setRefreshing(false);
                            cancelLoading();
                        }

                        @Override
                        public void onFailure(Call<ProductsResponse> call, Throwable t) {
                            Log.e(TAG, "onFailure: " + t);
                            refresher.setRefreshing(false);
                            noData.setVisibility(View.VISIBLE);
                            productRecycler.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Connection timed out", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


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
        if (item.getItemId() == R.id.item_delete) {
            deleteProducts(productsAdapter.getSelectedProducts());
        }
        return super.onOptionsItemSelected(item);
    }

}
