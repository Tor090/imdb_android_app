package com.example.android.imdbfinalapp.di

import android.content.Context
import androidx.room.Room
import com.example.android.imdbfinalapp.database.MovieDatabase
import com.example.android.imdbfinalapp.network.ImdbService
import com.example.android.imdbfinalapp.repository.MovieRepository
import com.example.android.imdbfinalapp.util.Constants
import com.example.android.imdbfinalapp.viewmodels.DetailViewModel
import com.example.android.imdbfinalapp.viewmodels.FavoriteViewModel
import com.example.android.imdbfinalapp.viewmodels.HomeViewModel
import com.example.android.imdbfinalapp.viewmodels.SearchViewModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val movieDBModule = module {
    fun getDatabase(context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            MovieDatabase::class.java,
            "imdb-db")
            .build()
    }

    single { getDatabase(get()) }
}

val networkModule = module {
    fun getInstance(): ImdbService {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder().apply {
            addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val originalRequest = chain.request()
                    val originalUrl = originalRequest.url
                    val newRequest=originalRequest.newBuilder().apply {
                        url(originalUrl.newBuilder().addQueryParameter(
                            "api_key","4b0d45b575e726aa5813c161fdc13b17").build())
                    }.build()
                    return chain.proceed(newRequest)
                }

            })
        }
        httpClient.addInterceptor(logging)
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
            .create(ImdbService::class.java)
    }

    single { getInstance() }
}

val repositoryModule = module {
    factory { MovieRepository(get(), get()) }
}

val viewModelModule = module {

    viewModel {
        HomeViewModel(get())
    }

    viewModel(){
        DetailViewModel(get(), get())
    }

    viewModel {
        FavoriteViewModel(get())
    }

    viewModel {
        SearchViewModel(get())
    }
}

