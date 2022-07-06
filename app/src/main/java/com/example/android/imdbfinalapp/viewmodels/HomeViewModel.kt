package com.example.android.imdbfinalapp.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.example.android.imdbfinalapp.models.Movie
import com.example.android.imdbfinalapp.repository.MovieRepository
import kotlinx.coroutines.*

class HomeViewModel(private val movieRepository: MovieRepository) : ViewModel() {


    private val _nowPlayingList = MutableLiveData<List<Movie>>()
    val nowPlayingList: LiveData<List<Movie>>
        get() = _nowPlayingList

    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message


    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _navigateToSelectedDetail = MutableLiveData<Movie?>()
    val navigateToSelectedDetail: MutableLiveData<Movie?>
        get() = _navigateToSelectedDetail

    private val _navigateToFavorite = MutableLiveData<Boolean>()
    val navigateToFavorite: LiveData<Boolean>
        get() = _navigateToFavorite

    private val _navigateToSearch = MutableLiveData<Boolean>()
    val navigateToSearch: LiveData<Boolean>
        get() = _navigateToSearch

    fun displayMovieDetail(movie: Movie){
        _navigateToSelectedDetail.value = movie
    }

    fun displayMovieDetailComplete(){
        _navigateToSelectedDetail.value = null
    }

    fun onNavigateToFavoriteClicked() {
        _navigateToFavorite.value = true
    }

    fun onNavigatedToFavorite() {
        _navigateToFavorite.value = false
    }

    fun onNavigateToSearchClicked() {
        _navigateToSearch.value = true
    }

    fun onNavigatedToSearch() {
        _navigateToSearch.value = false
    }

    init {
        getData()
        _loading.value = false
    }

    fun getData(){
        viewModelScope.launch {
            _loading.value = true
            try {
                _nowPlayingList.value = movieRepository.getData()
            }catch (e:Exception){
                Log.i("popular exeception", e.message.toString())
            }
            _loading.value = false
        }
    }

//    fun getData() {
//        viewModelScope.launch {
//            _loading.value = true
//            try {
//                val nowPlaying = movieDatabase.movieDao().getResultsByType(Constants.NOW_PLAYING)
//                if(nowPlaying.isNotEmpty()) {
//                    _nowPlayingList.value = nowPlaying
//                }else{
//                    val nowPlayingMovies=imdbService.getNowPlayingMovies()
//                    for(i:Movie in nowPlayingMovies?.results!!){
//                        i.type=Constants.NOW_PLAYING
//                    }
//                    movieDatabase.movieDao().clear()
//                    movieDatabase.movieDao().insertMany(nowPlayingMovies.results!!)
//                    _nowPlayingList.value = nowPlayingMovies.results!!
//                    Toast.makeText(app, "From Network", Toast.LENGTH_LONG).show()
//                }
//            } catch (exception: Exception) {
//                Log.i("popular exeception", exception.message.toString())
//            }
//            _loading.value = false
//        }
//    }



    fun setMovieToFavorite(movie: Movie) {
        movie.favorite=!movie.favorite
        _loading.value = true
        viewModelScope.launch {
            try {
                movieRepository.updateMovieToFavorite(movie)
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
//            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
//                @Suppress("UNCHECKED_CAST")
//                return HomeViewModel(app) as T
//            }
//            throw IllegalArgumentException("Unable to construct viewmodel")
//        }
//    }

}