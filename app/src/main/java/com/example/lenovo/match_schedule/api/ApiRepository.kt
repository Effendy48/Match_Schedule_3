package com.example.lenovo.match_schedule.api

import com.example.lenovo.match_schedule.BuildConfig
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiRepository{
    companion object {
        fun getRetrofit(): Retrofit{
            return Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
    }
}