package com.sapp.userapp.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceGenerator {
    private const val BASE_API_URL = "https://randomuser.me/api/"
    private var retrofit: Retrofit? = null
    private val gson = GsonBuilder().create()
    private val okHttpClientBuilder = OkHttpClient.Builder()
    private val okHttpClient = okHttpClientBuilder.build()

    fun <T> createService(serviceClass: Class<T>?): T {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        return retrofit!!.create(serviceClass)
    }
}