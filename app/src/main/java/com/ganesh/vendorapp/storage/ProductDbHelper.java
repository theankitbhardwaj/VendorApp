package com.ganesh.vendorapp.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ProductDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "vendor_db";
    private static final int VERSION = 1;

    public ProductDbHelper(Context context){
        super(context,DB_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlQuery = "CREATE TABLE PRODUCTS (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "NAME TEXT, COMPANY TEXT, DESCRIPTION TEXT)";
        sqLiteDatabase.execSQL(sqlQuery);

    }

    private void insertData(String name, String company, String description, SQLiteDatabase database){
        ContentValues values = new ContentValues();
        values.put("NAME",name);
        values.put("COMPANY",company);
        values.put("DESCRIPTION",description);
        database.insert("PRODUCTS",null,values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }



}
