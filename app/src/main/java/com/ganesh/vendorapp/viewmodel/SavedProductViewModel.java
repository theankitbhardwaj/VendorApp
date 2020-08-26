package com.ganesh.vendorapp.viewmodel;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ganesh.vendorapp.api.RetrofitClient;
import com.ganesh.vendorapp.models.SaveResponse;
import com.ganesh.vendorapp.storage.AppDatabase;
import com.ganesh.vendorapp.storage.ProductDao;
import com.ganesh.vendorapp.storage.ProductRoom;
import com.ganesh.vendorapp.storage.SavedProductDao;
import com.ganesh.vendorapp.storage.SavedProductRoom;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavedProductViewModel extends AndroidViewModel {

    private String TAG = this.getClass().getSimpleName();
    private SavedProductDao productDao;
    private AppDatabase appDatabase;
    private LiveData<List<SavedProductRoom>> productList;

    public SavedProductViewModel(@NonNull Application application) {
        super(application);

        appDatabase = AppDatabase.getDatabase2(application);
        productDao = appDatabase.savedProductDao();
        productList = productDao.getAll();

    }

    public LiveData<List<SavedProductRoom>> getProductList() {
        return productList;
    }

    public void insert(SavedProductRoom productRoom) {
        new SavedProductViewModel.InsertAsyncTask(productDao).execute(productRoom);
    }

    public void delete(SavedProductRoom productRoom) {
        RetrofitClient.getInstance().getApi().deleteProduct(productRoom.productId).enqueue(new Callback<SaveResponse>() {
            @Override
            public void onResponse(Call<SaveResponse> call, Response<SaveResponse> response) {
                Log.e(TAG, "Delete Api message: " + response.body().getMessage());
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("false")) {
                        Toast.makeText(getApplication(), "Product " + productRoom.title + " is deleted successfully", Toast.LENGTH_SHORT).show();
                        new SavedProductViewModel.DeleteAsyncTask(productDao).execute(productRoom);
                    }
                }
            }

            @Override
            public void onFailure(Call<SaveResponse> call, Throwable t) {

            }
        });
    }

    private class OperationsAsyncTask extends AsyncTask<SavedProductRoom, Void, Void> {

        SavedProductDao mAsyncTaskDao;

        OperationsAsyncTask(SavedProductDao dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(SavedProductRoom... productRooms) {
            return null;
        }
    }

    private class InsertAsyncTask extends SavedProductViewModel.OperationsAsyncTask {

        InsertAsyncTask(SavedProductDao mNoteDao) {
            super(mNoteDao);
        }

        @Override
        protected Void doInBackground(SavedProductRoom... productRooms) {
            mAsyncTaskDao.insertAll(productRooms);
            return null;
        }
    }

    private class DeleteAsyncTask extends SavedProductViewModel.OperationsAsyncTask {

        public DeleteAsyncTask(SavedProductDao noteDao) {
            super(noteDao);
        }

        @Override
        protected Void doInBackground(SavedProductRoom... productRooms) {
            mAsyncTaskDao.delete(productRooms);
            return null;
        }
    }


}
