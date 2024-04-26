package com.dicoding.asclepius.data.retrofit

import com.dicoding.asclepius.data.reponse.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("everything")
    fun getNews(
        @Query("q") query: String,
        @Query("sortBy") sort: String,
        @Query("language") lang: String,
        @Query("apiKey") token: String,
    ) : Call<NewsResponse>
}