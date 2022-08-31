package com.example.linguasyne

import android.util.Log
import com.example.linguasyne.classes.NewsItem
import com.example.linguasyne.managers.APIManager
import com.example.linguasyne.managers.NewsResponse
import com.example.linguasyne.viewmodels.HomeViewModel
import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class APITests {

    @Test
    fun asynchronousAPIcall() {

        val viewModel = HomeViewModel()
        viewModel.APIcall{
            viewModel.newsItems.let {
                assertEquals(5, it.size)
            }
        }
    }

    @Test
    fun synchronousAPIcall() {
        val newsItems: List<NewsItem.Data>?
        val apiCall = APIManager.create()
        newsItems = apiCall.getNewsItems().execute().body()?.data

        newsItems?.let { assertEquals(5, it.size) }

    }

}