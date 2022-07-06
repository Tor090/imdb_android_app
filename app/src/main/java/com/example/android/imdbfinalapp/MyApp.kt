package com.example.android.imdbfinalapp

import android.app.Application
import android.widget.Toast
import androidx.work.*
import com.example.android.imdbfinalapp.presentation.di.movieDBModule
import com.example.android.imdbfinalapp.presentation.di.networkModule
import com.example.android.imdbfinalapp.presentation.di.repositoryModule
import com.example.android.imdbfinalapp.presentation.di.viewModelModule
import com.example.android.imdbfinalapp.domain.workmanagers.MovieWorker
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class MyApp : Application() {
    override fun onCreate() {

        startKoin {
            androidContext(this@MyApp)
            modules(listOf(viewModelModule, networkModule, movieDBModule, repositoryModule))
        }

        val networkConstraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val movieWorker = PeriodicWorkRequest
            .Builder(MovieWorker::class.java, 15, TimeUnit.MINUTES)
            .setConstraints(networkConstraints)
            .addTag("movie_work")
            .build()
        WorkManager.getInstance(applicationContext).enqueue(movieWorker)
        super.onCreate()
    }
}