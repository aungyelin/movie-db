package dev.yelinaung.moviedb.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Upsert
    suspend fun addMovie(movieEntity: MovieEntity)

    @Query("SELECT * FROM favorite_movies")
    fun getFavoriteMovies(): Flow<List<MovieEntity>>

    @Delete
    suspend fun deleteMovie(movieEntity: MovieEntity)

}