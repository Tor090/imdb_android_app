package com.example.android.imdbfinalapp.domain.repository

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.example.android.imdbfinalapp.data.database.MovieDatabase
import com.example.android.imdbfinalapp.data.database.dao.MovieDao
import com.example.android.imdbfinalapp.data.models.Movie
import com.example.android.imdbfinalapp.data.models.SearchResult
import com.example.android.imdbfinalapp.data.models.VideoResult
import com.example.android.imdbfinalapp.data.network.ImdbService
import com.example.android.imdbfinalapp.domain.util.Constants


class MovieRepository(private val movieDatabase: MovieDatabase, private val imdbService: ImdbService) {

    suspend fun getData(): List<Movie>{
        var nowPlayingList : List<Movie> = listOf()
            try {
                val nowPlaying = movieDatabase.movieDao().getResultsByType(Constants.NOW_PLAYING)
                if(nowPlaying.isNotEmpty()) {
                    nowPlayingList = nowPlaying
                }else{
                    val nowPlayingMovies=imdbService.getNowPlayingMovies()
                    for(i: Movie in nowPlayingMovies?.results!!){
                        i.type= Constants.NOW_PLAYING
                    }
                    movieDatabase.movieDao().clear()
                    movieDatabase.movieDao().insertMany(nowPlayingMovies.results!!)
                    nowPlayingList = nowPlayingMovies.results!!
                }
            } catch (exception: Exception) {
                Log.i("popular exeception", exception.message.toString())
            }
        return nowPlayingList
    }

    suspend fun updateMovieToFavorite(movie: Movie){
        movieDatabase.movieDao().updateFavorite(movie)
    }

    suspend fun setMovieToFavorite(movie: Movie){
        movieDatabase.movieDao().insertMovie(movie)
    }

    suspend fun getFavoriteMovies(): List<Movie> {
        return movieDatabase.movieDao().favoriteMovie()
    }

    suspend fun getMovieTrailer(id: Int): VideoResult?{
       return imdbService.getVideoResults(id)
    }

    suspend fun getSearchResults(query: String) : SearchResult?{
        return imdbService.getSearchResults(query,true)
    }
}