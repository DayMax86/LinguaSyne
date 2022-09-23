package com.example.linguasyne.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

class ApiViewModel : ViewModel() {

    interface NewsApi {
        @GET("search_free?q=france24&lang=fr&media=True")
        @Headers(
            "X-RapidAPI-Key: 387f51c318msh541d76400b314c1p15f5a6jsn28aba00ec1bf",
            "X-RapidAPI-Host: newscatcher.p.rapidapi.com",
        )
        suspend fun getNewsItems(): NewsResponse
    }

    var news = mutableStateListOf<NewsItem>()
    val BASE_URL =
        "https://newscatcher.p.rapidapi.com/v1/"

    private val newsService: NewsApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NewsApi::class.java)

    init {
        viewModelScope
            .launch {
                try {
                    val tempList: NewsResponse = newsService.getNewsItems()
                    for (i in 0 until 5) {
                        news.add(
                            tempList
                                .toModel(i)
                        )
                    }
                } catch (e: Exception) {
                    Log.e("ApiViewModel", "$e")
                }
            }
    }

    data class NewsResponse(
        val articles: List<Article>,
        val page: Int,
        val page_size: Int,
        val status: String,
        val total_hits: Int,
        val total_pages: Int,
        val user_input: UserInput
    ) {
        data class Article(
            val _id: String,
            val _score: Double,
            val author: String,
            val clean_url: String,
            val country: String,
            val language: String,
            val link: String,
            val media: String,
            val media_content: List<String>,
            val published_date: String,
            val rank: Int,
            val rights: String,
            val summary: String,
            val title: String,
            val topic: String
        )

        data class UserInput(
            val from: String,
            val lang: String,
            val media: String,
            val page: Int,
            val q: String,
            val ranked_only: String,
            val search_in: String,
            val size: Int,
            val sort_by: String
        )
    }

    data class NewsItem(
        var article: NewsResponse.Article,
        var title: String = "",
        var image: String = "",
    )

    private fun NewsResponse.toModel(index: Int) =
        NewsItem(
            article = articles[index],
            title = articles[index].title,
            image = articles[index].media,
        )

}
