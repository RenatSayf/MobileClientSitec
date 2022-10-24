package com.renatsayf.mobileclientsitec.di.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.renatsayf.mobileclientsitec.repository.net.IApi;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@InstallIn(SingletonComponent.class)
@Module
public class ApiModule
{
    private static final String baseUrl = "https://dev.sitec24.ru/UKA_TRADE/hs/";

    @Inject
    public ApiModule(){}

    @Provides
    @Singleton
    public IApi provideApi() {
        Gson gson = new GsonBuilder().setLenient().create();
        IApi api = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(IApi.class);
        return api;
    }

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();


}
