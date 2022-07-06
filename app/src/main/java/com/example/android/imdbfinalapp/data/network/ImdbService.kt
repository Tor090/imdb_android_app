package com.example.android.imdbfinalapp.data.network

import android.security.KeyChainAliasCallback
import com.example.android.imdbfinalapp.data.models.NowPlaying
import com.example.android.imdbfinalapp.data.models.SearchResult
import com.example.android.imdbfinalapp.data.models.VideoResult
import com.example.android.imdbfinalapp.domain.util.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface ImdbService {
    @GET(Constants.NOW_PLAYING_URL)
    suspend fun getNowPlayingMovies(): NowPlaying?

    @GET(Constants.SEARCH_MULTI)
    suspend fun getSearchResults(
        @Query("query") query: String?,
        @Query("include_adult") includeAdult: Boolean?
    ): SearchResult?

    @GET(Constants.VIDEO_SEARCH)
    suspend fun getVideoResults(
        @Path("id") id: Int
    ) : VideoResult?

}