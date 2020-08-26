package com.ganesh.vendorapp.storage;

import androidx.room.TypeConverter;

import com.ganesh.vendorapp.models.Variants;
import com.ganesh.vendorapp.models.VariantsItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converter {
    @TypeConverter
    public String variantsListToString(List<Variants> value) {
        return new Gson().toJson(value);
    }

    @TypeConverter
    public List<Variants> variantsStringToList(String value) {
        Type type = new TypeToken<List<Variants>>() {
        }.getType();
        return new Gson().fromJson(value, type);
    }

    @TypeConverter
    public String variantsItemListToString(List<VariantsItem> value) {
        return new Gson().toJson(value);
    }

    @TypeConverter
    public List<VariantsItem> variantsItemStringToList(String value) {
        Type type = new TypeToken<List<VariantsItem>>() {
        }.getType();
        return new Gson().fromJson(value, type);
    }


}
