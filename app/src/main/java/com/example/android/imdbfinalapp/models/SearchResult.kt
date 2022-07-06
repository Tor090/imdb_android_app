package com.example.android.imdbfinalapp.models

data class SearchResult (
    var page: Int = 0,
    var results: List<Movie>? = null,
    var total_pages: Int = 0,
    var total_results: Int = 0,
)