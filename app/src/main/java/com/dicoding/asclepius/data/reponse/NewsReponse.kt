package com.dicoding.asclepius.data.reponse

data class NewsResponse(
    val totalResults: Int,
    val articles: List<ArticlesItem>,
    val status: String
)

data class ArticlesItem(
    val publishedAt: String,
    val author: Any,
    val urlToImage: String,
    val description: String,
    val source: Source,
    val title: String,
    val url: String,
    val content: String
)

data class Source(
	val name: String,
	val id: String
)

