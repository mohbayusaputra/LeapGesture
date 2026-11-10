package com.example.leapgesture.Data.Repository

import com.example.leapgesture.Data.Api.RetrofitInstance
import com.example.leapgesture.Data.Model.NewsResponse
import retrofit2.Response

class NewsRepository {
    suspend fun getNews(query: String, language: String, apiKey: String): Response<NewsResponse> {
        return RetrofitInstance.api.getNews(query, language, apiKey)
    }
}