package com.ganesh.vendorapp.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ganesh.vendorapp.storage.AppDatabase;
import com.ganesh.vendorapp.storage.ProductDao;
import com.ganesh.vendorapp.storage.ProductRoom;
import com.ganesh.vendorapp.storage.SavedProductDao;
import com.ganesh.vendorapp.storage.SavedProductRoom;

import java.util.List;

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


}
