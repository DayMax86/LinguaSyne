package com.example.linguasyne.managers

import com.example.linguasyne.classes.NewsItem
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

val BASE_URL =
    "http://api.mediastack.com/v1/"

interface APIManager {

    @GET("news?access_key=e5437ae1845a01e1a3c2ef67481b840f&countries=fr&limit=5&sources=lepoint")
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
