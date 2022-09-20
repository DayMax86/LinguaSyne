package com.example.linguasyne.managers

import com.example.linguasyne.classes.NewsItem
import com.squareup.okhttp.Interceptor
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import java.io.IOException

val BASE_URL =
    "https://newscatcher.p.rapidapi.com/v1/"

interface APIManager {

    @GET("search_free?q=lepoint&lang=fr&media=True")
    @Headers(
        "X-RapidAPI-Key: 387f51c318msh541d76400b314c1p15f5a6jsn28aba00ec1bf",
        "X-RapidAPI-Host: newscatcher.p.rapidapi.com",
    )
    fun getNewsItems(): Call<NewsResponse>

    companion object Factory {

        fun create(): APIManager {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(APIManager::class.java)
        }

    }

}

data class NewsResponse(
    var data: List<NewsItem.Data>,
    var pagination: NewsItem.Pagination,
)
