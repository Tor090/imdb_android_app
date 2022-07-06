package com.example.android.imdbfinalapp.data.database.dao

import androidx.room.*
import com.example.android.imdbfinalapp.data.models.Movie

@Dao
interface MovieDao {
    @Insert
    suspend fun insert(movie: Movie): Long

    @Delete
    suspend fun delete(movie: Movie): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMany(results: List<Movie>): List<Long>

    @Query("select * from movie where type=:type")
    suspend fun getResultsByType(type: String): List<Movie>

    @Query("select * from movie where favorite = 1")
    suspend fun favoriteMovie(): List<Movie>

    @Query("DELETE FROM movie where favorite = 0")
    suspend fun clear():Int

    @Query("UPDATE  movie SET favorite=0 where favorite = 1")
    suspend fun clearFavoriteMovie():Int

    @Update
    suspend fun updateFavorite(movie: Movie): Int
}