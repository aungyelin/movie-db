package dev.yelinaung.moviedb.domain

import kotlinx.coroutines.flow.Flow
import dev.yelinaung.moviedb.data.database.MovieEntity
import dev.yelinaung.moviedb.data.service.model.movie_details.MovieDetailsResponse
import dev.yelinaung.moviedb.data.service.model.movie_list.Movie
import dev.yelinaung.moviedb.data.service.model.player.Video
import dev.yelinaung.moviedb.utils.ResultWrapper

interface MovieRepository {

    suspend fun getPopularMovies(page: Int) : Flow<ResultWrapper<List<Movie>>>

    suspend fun getNowPlayingMovies(page: Int) : Flow<ResultWrapper<List<Movie>>>

    suspend fun getMovieDetails(id: Int) : Flow<ResultWrapper<MovieDetailsResponse>>

    suspend fun addFavoriteMovie(movieEntity: MovieEntity)

    suspend fun deleteFavoriteMovie(movieEntity: MovieEntity)

    fun getAllFavoriteMovies(): Flow<List<MovieEntity>>

    suspend fun getMovieVideo(movieId: Int): Flow<ResultWrapper<List<Video>>>

}