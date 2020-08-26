package com.ganesh.vendorapp.storage;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.ganesh.vendorapp.models.Variants;
import com.ganesh.vendorapp.models.VariantsItem;

import java.util.List;

@Entity
public class SavedProductRoom {

    @PrimaryKey(autoGenerate = true)
    public int uid;
    @ColumnInfo(name = "product_id")
    public String productId;

    @ColumnInfo(name = "user_id")
    public String userId;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "company")
    public String company;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "variants")
    public List<VariantsItem> variants;
}
