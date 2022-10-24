package com.renatsayf.mobileclientsitec.model.users;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ListUsers implements Serializable
{
    @SerializedName("ListUsers")
    public ArrayList<User> listUsers;
}
