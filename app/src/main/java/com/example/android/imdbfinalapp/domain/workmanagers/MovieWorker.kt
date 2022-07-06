package com.example.android.imdbfinalapp.domain.workmanagers

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.android.imdbfinalapp.data.database.MovieDatabase
import com.example.android.imdbfinalapp.data.models.Movie
import com.example.android.imdbfinalapp.data.network.ImdbService
import com.example.android.imdbfinalapp.domain.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieWorker(
    private val ctx: Context,
    private val imdbService: ImdbService,
    private val movieDatabase: MovieDatabase,
    params: WorkerParameters) : Worker(ctx, params){
    override fun doWork(): Result {
        CoroutineScope(Dispatchers.IO).launch{
            try {
                Toast.makeText(ctx,"Pizda",Toast.LENGTH_LONG).show()
                val nowPlayingMovies=imdbService.getNowPlayingMovies()
                for(i: Movie in nowPlayingMovies?.results!!){
                    i.type= Constants.NOW_PLAYING
                }
                movieDatabase.movieDao().clear()
                movieDatabase.movieDao().insertMany(nowPlayingMovies.results!!)
            }catch (e:Exception){
                Log.i("exeception", e.message.toString())
            }
        }
        return Result.success()
    }
}