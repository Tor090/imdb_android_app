package com.example.android.imdbfinalapp.data.models


data class NowPlaying(
    var dates: Dates? = null,
    var page: Int = 0,
    var results: List<Movie>? = null,
    var total_pages: Int = 0,
    var total_results: Int = 0,
) {
    inner class Dates {
        var maximum: String? = null
        var minimum: String? = null
        override fun toString(): String {
            return "Dates{" +
                    "maximum='" + maximum + '\'' +
                    ", minimum='" + minimum + '\'' +
                    '}'
        }
    }
}
