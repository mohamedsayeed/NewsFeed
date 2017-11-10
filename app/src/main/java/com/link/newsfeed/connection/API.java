package com.link.newsfeed.connection;

import com.link.newsfeed.model.Result;

import retrofit2.Call;
import retrofit2.http.GET;
public interface API {
    @GET("articles?source=the-next-web&apiKey=533af958594143758318137469b41ba9")
    Call<Result> getArticles();
}
