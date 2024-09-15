package dev.yelinaung.moviedb.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import dev.yelinaung.moviedb.data.database.MovieDao
import dev.yelinaung.moviedb.data.database.MovieEntity
import dev.yelinaung.moviedb.domain.MovieRepository
import dev.yelinaung.moviedb.data.service.MovieApiService
import dev.yelinaung.moviedb.data.service.model.movie_details.MovieDetailsResponse
import dev.yelinaung.moviedb.data.service.model.movie_list.Movie
import dev.yelinaung.moviedb.data.service.model.player.Video
import dev.yelinaung.moviedb.utils.ResultWrapper
import dev.yelinaung.moviedb.utils.safeApiCall
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: MovieApiService,
    private val movieDao: MovieDao
) : MovieRepository {

    override suspend fun getPopularMovies(page: Int): Flow<ResultWrapper<List<Movie>>> {
        return flow {
            emit(ResultWrapper.Loading(true))
            emit(safeApiCall { apiService.getPopularMovies(page).movies })
        }
    }

    override suspend fun getNowPlayingMovies(page: Int): Flow<ResultWrapper<List<Movie>>> {
        return flow {
            emit(ResultWrapper.Loading(true))
            emit(safeApiCall { apiService.getNowPlayingMovies(page).movies })
        }
    }

    override suspend fun getMovieDetails(id: Int): Flow<ResultWrapper<MovieDetailsResponse>> {
        return flow {
            emit(ResultWrapper.Loading(true))
            emit(safeApiCall { apiService.getMovieDetails(id) })
        }
    }

    override suspend fun addFavoriteMovie(movieEntity: MovieEntity) {
        movieDao.addMovie(movieEntity)
    }

    override suspend fun deleteFavoriteMovie(movieEntity: MovieEntity) {
        movieDao.deleteMovie(movieEntity)
    }

    override fun getAllFavoriteMovies(): Flow<List<MovieEntity>> {
        return movieDao.getFavoriteMovies()
    }

    override suspend fun getMovieVideo(movieId: Int): Flow<ResultWrapper<List<Video>>> {
        return flow {
            emit(ResultWrapper.Loading(true))
            emit(safeApiCall { apiService.getVideos(movieId).results.filter { it.site == "YouTube" } })
        }
    }

}