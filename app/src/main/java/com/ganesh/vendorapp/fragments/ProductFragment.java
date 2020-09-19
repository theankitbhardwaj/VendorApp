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
import com.ganesh.vendorapp.adapters.OutofStockAdapter;
import com.ganesh.vendorapp.api.APIs;
import com.ganesh.vendorapp.api.RetrofitClient;
import com.ganesh.vendorapp.adapters.ProductsAdapter;
import com.ganesh.vendorapp.models.ProductsItem;
import com.ganesh.vendorapp.models.ProductsResponse;
import com.ganesh.vendorapp.models.VariantsItem;
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
                    for (SavedProductRoom a : savedProductRooms) {
                        List<VariantsItem> outOfStockVariants = new ArrayList<>();
                        for (int i = 0; i < a.variants.size(); i++) {
                            if (a.variants.get(i).getQuantity() != 0) {
                                outOfStockVariants.add(a.variants.get(i));
                            }
                        }
                        if (!outOfStockVariants.isEmpty())
                            savedProducts.add(new ProductsItem(a.productId, a.description, a.company, a.title, outOfStockVariants));
                    }
                    productsAdapter = new ProductsAdapter(savedProducts, getContext());
                    productRecycler.setAdapter(productsAdapter);
                }
            });
        } else
            getProducts();
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
