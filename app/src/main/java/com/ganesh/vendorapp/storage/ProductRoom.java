package com.ganesh.vendorapp.storage;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.ganesh.vendorapp.models.Variants;

import java.util.List;

@Entity
public class ProductRoom {

    @NonNull
    @PrimaryKey
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
    public List<Variants> variants;

}
