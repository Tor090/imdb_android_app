package com.example.android.imdbfinalapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.imdbfinalapp.models.Movie
import com.example.android.imdbfinalapp.models.SearchResult
import com.example.android.imdbfinalapp.repository.MovieRepository
import kotlinx.coroutines.launch

class SearchViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val _searchList = MutableLiveData<List<Movie>>()
    val searchList: LiveData<List<Movie>>
        get() = _searchList

    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _navigateToSelectedDetail = MutableLiveData<Movie?>()
    val navigateToSelectedDetail: MutableLiveData<Movie?>
        get() = _navigateToSelectedDetail

    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: MutableLiveData<Boolean>
        get() = _navigateBack

    fun getSearchResults(query: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val res = movieRepository.getSearchResults(query)
                Log.d("SearchFragment", "$res")
                _searchList.value = res!!.results!!
            } catch(exception:Exception){
                _searchList.value = SearchResult().results!!
                _message.value = "Something went wrong"
            }
            finally {
                _loading.value = false
            }
        }
    }

    fun displayMovieDetail(movie: Movie){
        _navigateToSelectedDetail.value = movie
    }

    fun displayMovieDetailComplete(){
        _navigateToSelectedDetail.value = null
    }

    fun onNavigateBackClicked() {
        _navigateBack.value = true
    }

    fun onNavigatedBack() {
        _navigateBack.value = false
    }

    fun setMovieToFavorite(movie: Movie) {
        movie.favorite=!movie.favorite
        _loading.value = true
        viewModelScope.launch {
            try {
                movieRepository.setMovieToFavorite(movie)
            }
            catch (e:Exception){
                _message.value = "operation failed"
            }
            finally {
                _loading.value = false
            }
        }
    }

//    class Factory(val app: Application) : ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
//                @Suppress("UNCHECKED_CAST")
//                return SearchViewModel(app) as T
//            }
//            throw IllegalArgumentException("Unable to construct viewmodel")
//        }
//    }
}