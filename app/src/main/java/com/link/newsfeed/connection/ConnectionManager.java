package com.link.newsfeed.connection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.link.newsfeed.model.Result;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mohamed Sayed on 11/10/2017.
 */

public class ConnectionManager {

    private API api;

    public static final String BASE_URL = "https://newsapi.org/v1/articles?source=the-next-web&apiKey=533af958594143758318137469b41ba9";



    private static ConnectionManager connectionManager;

    public static ConnectionManager getInstance() {
        if (connectionManager == null)
            connectionManager = new ConnectionManager();

        return connectionManager;
    }

    private ConnectionManager() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        api = retrofit.create(API.class);

    }

    public Call<Result> getArticles() {
        return api.getArticles();
    }

}
