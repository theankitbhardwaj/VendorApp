package com.ganesh.vendorapp.storage;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM productroom")
    LiveData<List<ProductRoom>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ProductRoom... products);

    @Delete
    void delete(ProductRoom... products);

    @Update
    void updateProduct(ProductRoom product);
}
