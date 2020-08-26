package com.ganesh.vendorapp.viewmodel;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ganesh.vendorapp.storage.AppDatabase;
import com.ganesh.vendorapp.storage.ProductDao;
import com.ganesh.vendorapp.storage.ProductRoom;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {

    private String TAG = this.getClass().getSimpleName();
    private ProductDao productDao;
    private AppDatabase appDatabase;
    private LiveData<List<ProductRoom>> productList;

    public ProductViewModel(@NonNull Application application) {
        super(application);

        appDatabase = AppDatabase.getDatabase(application);
        productDao = appDatabase.productDao();
        productList = productDao.getAll();
    }

    public LiveData<List<ProductRoom>> getProductList() {
        return productList;
    }

    public void insert(ProductRoom productRoom) {
        new InsertAsyncTask(productDao).execute(productRoom);
    }

    public void delete(ProductRoom productRoom) {
        new DeleteAsyncTask(productDao).execute(productRoom);
    }

    private class OperationsAsyncTask extends AsyncTask<ProductRoom, Void, Void> {

        ProductDao mAsyncTaskDao;

        OperationsAsyncTask(ProductDao dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(ProductRoom... productRooms) {
            return null;
        }
    }

    private class InsertAsyncTask extends OperationsAsyncTask {

        InsertAsyncTask(ProductDao mNoteDao) {
            super(mNoteDao);
        }

        @Override
        protected Void doInBackground(ProductRoom... productRooms) {
            mAsyncTaskDao.insertAll(productRooms);
            return null;
        }
    }

    private class DeleteAsyncTask extends OperationsAsyncTask {

        public DeleteAsyncTask(ProductDao noteDao) {
            super(noteDao);
        }

        @Override
        protected Void doInBackground(ProductRoom... productRooms) {
            mAsyncTaskDao.delete(productRooms);
            return null;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i(TAG, "ViewModel Destroyed");
    }
}
