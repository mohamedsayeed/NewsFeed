package com.link.newsfeed.connection;

import com.link.newsfeed.model.Result;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Mohamed Sayed on 11/10/2017.
 */

public interface API {
    @GET()
    Call<Result> getArticles();
}
