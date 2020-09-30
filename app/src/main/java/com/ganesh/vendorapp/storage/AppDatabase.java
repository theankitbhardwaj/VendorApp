package com.ganesh.vendorapp.storage;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


@Database(entities = {ProductRoom.class, SavedProductRoom.class}, version = 1)
@TypeConverters({Converter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract ProductDao productDao();

    public abstract SavedProductDao savedProductDao();

    private static volatile AppDatabase noteRoomInstance;
    private static volatile AppDatabase noteRoomInstance2;

    public static AppDatabase getDatabase(final Context context) {
        if (noteRoomInstance == null) {
            synchronized (AppDatabase.class) {
                if (noteRoomInstance == null) {
                    noteRoomInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "product_db")
                            .build();
                }
            }
        }
        return noteRoomInstance;
    }

    public static AppDatabase getDatabase2(final Context context) {
        if (noteRoomInstance2 == null) {
            synchronized (AppDatabase.class) {
                if (noteRoomInstance2 == null) {
                    noteRoomInstance2 = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "saved_product_db")
                            .build();
                }
            }
        }
        return noteRoomInstance2;
    }


}
