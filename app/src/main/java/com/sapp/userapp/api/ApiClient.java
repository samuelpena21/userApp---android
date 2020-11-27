package com.sapp.userapp.api;

import com.sapp.userapp.api.response.UserResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiClient {

    @GET("?inc=name,picture,email,gender,phone,cell")
    Call<UserResponse> getUserListPaginated(@Query("page") int page, @Query("results") int result, @Query("seed") String seed);
}
