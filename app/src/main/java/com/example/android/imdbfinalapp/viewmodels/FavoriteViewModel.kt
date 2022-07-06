package com.example.android.imdbfinalapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.imdbfinalapp.database.MovieDatabase
import com.example.android.imdbfinalapp.models.Movie
import com.example.android.imdbfinalapp.repository.MovieRepository
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

//    fun clearFavoriteMovies() {
//        viewModelScope.launch {
//            try {
//                val movieDao = movieDatabase.movieDao()
//                movieDao.clearFavoriteMovie()
//                getData()
//            }
//            catch (e:Exception){
//                Log.i("clear bookmarks error",e.message.toString())
//                _message.value = "something went wrong"
//            }
//        }
//    }

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
                Log.i("error",e.message.toString())
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
            }
        }
    }

//    class Factory(val app: Application) : ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
//                @Suppress("UNCHECKED_CAST")
//                return FavoriteViewModel(app) as T
//            }
//            throw IllegalArgumentException("Unable to construct viewmodel")
//        }
//    }
}