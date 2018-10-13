package com.github.bagiasn.nasavoicecrawler.data.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ApiClient {

    private static final String API_SERVER = "http://52.57.6.21:3254";

    private static ApiClient instance;
    private Retrofit retrofit;

    private ApiClient() {

        OkHttpClient client = new OkHttpClient.Builder().build();

        retrofit = new Retrofit.Builder()
                .baseUrl(API_SERVER)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build();
    }

    public static synchronized ApiClient getClient() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }

    public Retrofit getRetrofit() { return retrofit; }
}
