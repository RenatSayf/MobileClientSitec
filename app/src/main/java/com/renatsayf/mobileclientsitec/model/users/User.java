package com.renatsayf.mobileclientsitec.model.users;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Users")
public class User implements Serializable
{
    @SerializedName("User")
    @ColumnInfo(name = "user")
    public String user;

    @NonNull
    @SerializedName("Uid")
    @PrimaryKey
    @ColumnInfo(name = "uid")
    public String uid;

    @SerializedName("Language")
    @ColumnInfo(name = "language")
    public String language;
}
