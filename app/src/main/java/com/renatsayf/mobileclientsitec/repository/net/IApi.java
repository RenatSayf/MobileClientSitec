package com.renatsayf.mobileclientsitec.repository.net;

import com.renatsayf.mobileclientsitec.model.auth.AuthCode;
import com.renatsayf.mobileclientsitec.model.users.Users;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IApi
{
    @Headers(value = "authorization: Basic aHR0cDpodHRw")
    @GET("UKA_TRADE/hs/MobileClient/{imei}/authentication")
    Single<Response<AuthCode>> auth(
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

    @Headers(value = "authorization: Basic aHR0cDpodHRw")
    @GET("UKA_TRADE/hs/MobileClient/{imei}/form/users")
    Single<Response<Users>> getUsers(
            @Path(value = "imei", encoded = true) String imei
    );
}
