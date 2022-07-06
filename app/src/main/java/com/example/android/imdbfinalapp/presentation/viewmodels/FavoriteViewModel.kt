package com.example.android.imdbfinalapp.presentation.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.imdbfinalapp.data.database.MovieDatabase
import com.example.android.imdbfinalapp.data.models.Movie
import com.example.android.imdbfinalapp.domain.repository.MovieRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val _favoriteList = MutableLiveData<List<Movie>>()
    val favoriteList: LiveData<List<Movie>>
        get() = _favoriteList

    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    private val _navigateToSelectedDetail = MutableLiveData<Movie?>()
    val navigateToSelectedDetail: MutableLiveData<Movie?>
        get() = _navigateToSelectedDetail

    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: MutableLiveData<Boolean>
        get() = _navigateBack

    fun onNavigateBackClicked() {
        _navigateBack.value = true
    }

    fun onNavigatedBack() {
        _navigateBack.value = false
    }

    fun displayMovieDetail(movie: Movie){
        _navigateToSelectedDetail.value = movie
    }

    fun displayMovieDetailComplete(){
        _navigateToSelectedDetail.value = null
    }

    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            try{
                val movies = movieRepository.getFavoriteMovies()
                _favoriteList.value = movies
            }
            catch (e:Exception){
                Log.i("Exception",e.message.toString())
            }
        }
    }

    fun setMovieToFavorite(movie: Movie) {
        movie.favorite=!movie.favorite
        viewModelScope.launch {
            try {
                movieRepository.updateMovieToFavorite(movie)
                getData()
            }
            catch (e:Exception){
                _message.value = "operation failed"
                Log.i("Exception", e.message.toString())
            }
        }
    }
}