package com.renatsayf.mobileclientsitec.repository.db;

import android.content.Context;

import com.renatsayf.mobileclientsitec.model.users.User;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = User.class, version = 1)
public abstract class AppDataBase extends RoomDatabase
{
    public abstract AppDao appDao();

    public static final String DB_NAME = "mobile-client.db";

    public static AppDataBase buildDataBase(Context context) {
        return Room.databaseBuilder(
                context,
                AppDataBase.class,
                DB_NAME
        ).build();
    }
}
