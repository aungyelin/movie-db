package dev.yelinaung.moviedb.presentation.movie_list

import androidx.compose.foundation.lazy.LazyListState
import dev.yelinaung.moviedb.data.service.model.movie_list.Movie

data class MovieListState(
    val isLoading: Boolean = false,

    val popularMoviePage: Int = 1,
    val nowPlayingPage: Int = 1,

    val selectedCategory: String = "Popular",

    val popularMovieList: List<Movie> = emptyList(),
    val nowPlayingList: List<Movie> = emptyList(),
    val favoriteMovies: List<Movie> = emptyList(),

    //In order to remember the position when scrolling the movies
    val popularLazyListState: LazyListState = LazyListState(),
    val nowPlayingLazyListState: LazyListState = LazyListState(),
    val favoritesLazyListState: LazyListState = LazyListState()
)