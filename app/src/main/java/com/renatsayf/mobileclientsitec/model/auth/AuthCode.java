package com.renatsayf.mobileclientsitec.model.auth;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AuthCode implements Serializable
{
    @SerializedName("Code")
    public int code;
}
