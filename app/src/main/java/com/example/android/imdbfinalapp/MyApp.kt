package com.example.android.imdbfinalapp

import android.app.Application
import android.widget.Toast
import androidx.work.*
import com.example.android.imdbfinalapp.di.*
import com.example.android.imdbfinalapp.workmanagers.MovieWorker
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class MyApp : Application() {
    override fun onCreate() {

        startKoin {
            androidContext(this@MyApp)
            modules(listOf(viewModelModule, networkModule, movieDBModule, repositoryModule))
        }

        Toast.makeText(applicationContext, "MYAPP", Toast.LENGTH_SHORT).show()
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