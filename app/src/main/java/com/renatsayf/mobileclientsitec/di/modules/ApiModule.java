package com.renatsayf.mobileclientsitec.di.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.renatsayf.mobileclientsitec.repository.net.IApi;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("UnnecessaryLocalVariable")
@InstallIn(SingletonComponent.class)
@Module
public class ApiModule
{
    private static final String baseUrl = "https://dev.sitec24.ru/";

    @Inject
    public ApiModule(){}

    @Provides
    @Singleton
    public IApi provideApi() {
        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient okHttpClient = createOkHttpClient();
        IApi api = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(IApi.class);
        return api;
    }

    private OkHttpClient createOkHttpClient() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager()
                    {
                        @Override
                        public void checkClientTrusted(X509Certificate[] x509Certificates, String s)
                        {}
                        @Override
                        public void checkServerTrusted(X509Certificate[] x509Certificates, String s)
                        {}
                        @Override
                        public X509Certificate[] getAcceptedIssuers()
                        {
                            return new X509Certificate[0];
                        }
                    }
            };
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());

            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory())
                    .hostnameVerifier((hostname, session) -> true)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build();
            return httpClient;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
