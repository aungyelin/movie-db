package dev.yelinaung.moviedb.data.service.model.movie_list.now_playing

import com.google.gson.annotations.SerializedName
import dev.yelinaung.moviedb.data.service.model.movie_list.Movie

data class NowPlayingMovieResponse(
    val dates: Dates,
    val page: Int,
    @SerializedName("results") // Map "results" to "movies"
    val movies: List<Movie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)