package com.cdsg.ficheaqui.data.network.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

public object RetrofitService {

     var logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
     var client : OkHttpClient = OkHttpClient.Builder().addInterceptor(logging).build()

    private var retrofit : Retrofit = Retrofit.Builder()
        .baseUrl("https://api.unsplash.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    public fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}