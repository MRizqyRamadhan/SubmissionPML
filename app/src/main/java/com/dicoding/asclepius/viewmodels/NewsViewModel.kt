package com.dicoding.asclepius.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.reponse.ArticlesItem
import com.dicoding.asclepius.data.reponse.NewsResponse
import com.dicoding.asclepius.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel: ViewModel() {
    private val _news = MutableLiveData<NewsResponse>()
    val news: LiveData<NewsResponse> = _news

    val _listNews = MutableLiveData<List<ArticlesItem>>()
    val listNews: LiveData<List<ArticlesItem>> = _listNews

    companion object{
        const val TAG = "NewsViewModel"
    }

    init {
        getNews()
    }

    private fun getNews() {
        val query = "cancer"
        val sort = "publishedAt"
        val lang = "en"
        val token: String = BuildConfig.TOKEN
        val client = ApiConfig.getApiService().getNews(query, sort, lang, token)
        client.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(
                call: Call<NewsResponse>,
                response: Response<NewsResponse>
            ) {
                if (response.isSuccessful) {
                    _news.value = response.body()
                    _listNews.value = response.body()?.articles
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}