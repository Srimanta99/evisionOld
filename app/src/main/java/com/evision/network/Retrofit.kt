package com.wecompli.network

import java.util.concurrent.TimeUnit

import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object Retrofit {
    private var retrofit: retrofit2.Retrofit? = null

    /* OkHttpClient.Builder client = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.addInterceptor(loggingInterceptor);*/// .client(client.build())
    val retrofitInstance: retrofit2.Retrofit
        get() {
            val okHttpClient = OkHttpClient.Builder()
                .readTimeout(1000, TimeUnit.SECONDS)
                .connectTimeout(1000, TimeUnit.SECONDS)
                .build()

            if (retrofit == null) {
                retrofit = retrofit2.Retrofit.Builder()
                    .baseUrl("https://apisandbox.mpaycenter.com")
                    .client(okHttpClient)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }
}
