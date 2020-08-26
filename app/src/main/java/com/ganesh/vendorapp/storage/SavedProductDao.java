package com.ganesh.vendorapp.storage;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SavedProductDao {
    @Query("SELECT * FROM savedproductroom")
    LiveData<List<SavedProductRoom>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(SavedProductRoom... products);

    @Delete
    void delete(SavedProductRoom... products);
}
