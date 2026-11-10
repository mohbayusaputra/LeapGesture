package com.example.leapgesture.Data.Model

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)