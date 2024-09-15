package dev.yelinaung.moviedb.data.service

import dev.yelinaung.moviedb.data.service.model.movie_details.MovieDetailsResponse
import dev.yelinaung.moviedb.data.service.model.movie_list.now_playing.NowPlayingMovieResponse
import dev.yelinaung.moviedb.data.service.model.movie_list.popular.PopularMovieResponse
import dev.yelinaung.moviedb.data.service.model.player.GetVideosResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int?,
    ): NowPlayingMovieResponse

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int?,
    ): PopularMovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int
    ): MovieDetailsResponse

    @GET("movie/{movieId}/videos")
    suspend fun getVideos(
        @Path("movieId") movieId: Int,
        @Query("language") language: String? = "en-US"
    ): GetVideosResponse

}