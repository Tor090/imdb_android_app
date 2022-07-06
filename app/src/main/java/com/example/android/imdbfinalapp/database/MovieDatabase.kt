package com.example.android.imdbfinalapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.android.imdbfinalapp.database.dao.MovieDao
import com.example.android.imdbfinalapp.models.Movie

@Database(entities = [Movie::class], exportSchema = false, version = 1)
@TypeConverters(
    IdsToStringConverter::class
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
