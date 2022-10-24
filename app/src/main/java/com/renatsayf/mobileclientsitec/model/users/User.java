package com.renatsayf.mobileclientsitec.model.users;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable
{
    @SerializedName("User")
    public String user;
    @SerializedName("Uid")
    public String uid;
    @SerializedName("Language")
    public String language;
}
