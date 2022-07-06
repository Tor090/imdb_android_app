package com.example.android.imdbfinalapp.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.example.android.imdbfinalapp.network.ImdbService
import com.example.android.imdbfinalapp.repository.MovieRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val id: Int, private val movieRepository: MovieRepository) : ViewModel() {


    private val _trailerPath = MutableLiveData<Uri>()
    val trailerPath: LiveData<Uri>
    get() = _trailerPath


    private val _navigateTrailer = MutableLiveData<Boolean>()
    val navigateTrailer: LiveData<Boolean>
        get() = _navigateTrailer

    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean>
        get() = _navigateBack

    init {
        getTrailerPath()
    }

    fun onNavigateBackClicked() {
        _navigateBack.value = true
    }

    fun onNavigatedBack() {
        _navigateBack.value = false
    }


    fun getTrailerPath(){
        viewModelScope.launch {
            try {
                val responce = movieRepository.getMovieTrailer(id)
                _trailerPath.value = Uri.parse("https://www.youtube.com/embed/" + responce!!.results[0].key)
            }catch (e:Exception){
                Log.d("DetailException", e.message.toString())
            }
            Log.d("TRAILERRRRRR", _trailerPath.value.toString())
        }
    }


    fun onPlayClicked(){
        Log.d("TRAILERRRRRR", _trailerPath.value.toString())
        _navigateTrailer.value = true
    }

    fun onPlayed(){
        _navigateTrailer.value = false
    }




//    class Factory(val id: Int) : ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
//                @Suppress("UNCHECKED_CAST")
//                return DetailViewModel(id) as T
//            }
//            throw IllegalArgumentException("Unable to construct viewmodel")
//        }
//    }
}