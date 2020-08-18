package com.ganesh.vendorapp.storage;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


@Database(entities = {ProductRoom.class}, version = 1)
@TypeConverters({Converter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProductDao productDao();

    private static volatile AppDatabase noteRoomInstance;

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
}
