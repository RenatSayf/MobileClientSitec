package com.renatsayf.mobileclientsitec.model.users;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Users implements Serializable
{
    @SerializedName("Users")
    public ListUsers users;
}
