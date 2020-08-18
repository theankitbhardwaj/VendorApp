package com.ganesh.vendorapp.storage;

import androidx.room.TypeConverter;

import com.ganesh.vendorapp.models.Variants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converter {
    @TypeConverter
    public String listToString(List<Variants> value) {
        return new Gson().toJson(value);
    }

    @TypeConverter
    public List<Variants> stringToList(String value) {
        Type type = new TypeToken<List<Variants>>() {
        }.getType();
        return new Gson().fromJson(value, type);
    }


}
