package com.renatsayf.mobileclientsitec.repository.net;

import com.renatsayf.mobileclientsitec.model.auth.AuthCode;
import com.renatsayf.mobileclientsitec.model.users.Users;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IApi
{
    @GET("/{imei}/authentication")
    @Headers(value = "authorization: Basic aHR0cDpodHRw")
    Single<AuthCode> auth(
            @Path(value = "imei", encoded = true)
            String imei,
            @Query(value = "uid", encoded = true)
            String uid,
            @Query(value = "pass", encoded = true)
            String pass,
            @Query(value = "copyFromDevice", encoded = true)
            Boolean copyFromDevice,
            @Query(value = "nfc", encoded = true)
            String nfc
    );

    @GET("/{imei}/form/users")
    @Headers(value = "authorization: Basic aHR0cDpodHRw")
    Single<Users> getUsers(
            @Path(value = "imei", encoded = true) String imei
    );
}
