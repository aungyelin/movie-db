package dev.yelinaung.moviedb.presentation.movie_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.yelinaung.moviedb.data.database.mapToMovie
import dev.yelinaung.moviedb.domain.MovieRepository
import dev.yelinaung.moviedb.utils.ResultWrapper
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository,
) : ViewModel() {

    private var _movieListState = MutableStateFlow(MovieListState())
    val movieListState = _movieListState.asStateFlow()

    private var _errorMessage = Channel<String?>()
    val errorMessage = _errorMessage.receiveAsFlow()

    // Prevent duplicate calls
    private var isFetchingPopular = false
    private var isFetchingNowPlaying = false

    init {
        fetchPopularMovies()
        fetchNowPlayingMovies()
        fetchFavoriteMovies()
    }


    private fun fetchPopularMovies() {
        if (isFetchingPopular) return
        isFetchingPopular = true

        viewModelScope.launch {
            try {
                _movieListState.update {
                    it.copy(isLoading = true)
                }
                repository.getPopularMovies(movieListState.value.popularMoviePage)
                    .collectLatest { result ->
                        when (result) {
                            is ResultWrapper.Success -> {
                                result.data.let { popularMovies ->
                                    _movieListState.update {
                                        it.copy(
                                            popularMovieList = movieListState.value.popularMovieList + popularMovies.distinctBy { movie -> movie.id },
                                            popularMoviePage = movieListState.value.popularMoviePage + 1
                                        )
                                    }
                                }
                                Log.d("PAGE", _movieListState.value.popularMoviePage.toString())
                            }

                            is ResultWrapper.Error -> {
                                _movieListState.update {
                                    it.copy(isLoading = false)
                                }
                                postError(result.message)
                            }

                            is ResultWrapper.Loading -> {
                                _movieListState.update {
                                    it.copy(isLoading = true)
                                }
                            }
                        }
                    }
            } catch (e: Exception) {
                Log.d("TAG", "fetchPopularMovies: Something Went Wrong")
            } finally {
                isFetchingPopular = false
            }
        }
    }

    private fun fetchNowPlayingMovies() {
        if (isFetchingNowPlaying) return // Prevent duplicate calls
        isFetchingNowPlaying = true

        viewModelScope.launch {
            try {
                _movieListState.update {
                    it.copy(isLoading = true)
                }
                repository.getNowPlayingMovies(movieListState.value.nowPlayingPage)
                    .collectLatest { result ->
                        when (result) {
                            is ResultWrapper.Success -> {
                                result.data.let { nowPlayingMovies ->
                                    _movieListState.update {
                                        it.copy(nowPlayingList = movieListState.value.nowPlayingList.distinctBy { movie -> movie.id } + nowPlayingMovies,
                                            nowPlayingPage = movieListState.value.nowPlayingPage + 1)
                                    }
                                }
                                Log.d("PAGE now", _movieListState.value.nowPlayingPage.toString())
                            }

                            is ResultWrapper.Error -> {
                                _movieListState.update {
                                    it.copy(isLoading = false)
                                }
                                postError(result.message)
                            }

                            is ResultWrapper.Loading -> {
                                _movieListState.update {
                                    it.copy(isLoading = true)
                                }
                            }
                        }
                    }
            } catch (e: Exception) {
                Log.d("TAG", "fetchPopularMovies: Something Went Wrong")
            } finally {
                isFetchingNowPlaying = false
            }
        }
    }

    private fun fetchFavoriteMovies() {
        viewModelScope.launch {
            repository.getAllFavoriteMovies().collectLatest { favoriteMovies ->
                _movieListState.update {
                    it.copy(
                        favoriteMovies = favoriteMovies.map { movieEntity ->
                            movieEntity.mapToMovie()
                        })
                }
            }
        }
    }

    fun onAction(action: MovieListAction) {
        when (action) {

            MovieListAction.PopularClick -> {
                _movieListState.update {
                    it.copy(
                        selectedCategory = "Popular"
                    )
                }
            }

            MovieListAction.NowPlayingClick -> {
                _movieListState.update {
                    it.copy(
                        selectedCategory = "Now Playing"
                    )
                }
            }

            MovieListAction.FavoritesClick -> {
                _movieListState.update {
                    it.copy(
                        selectedCategory = "Favorites"
                    )
                }
                fetchFavoriteMovies()
            }


            is MovieListAction.Paginate -> {
                if (action.category == "Popular") {
                    fetchPopularMovies()
                } else if (action.category == "Now Playing") {
                    fetchNowPlayingMovies()
                }
            }
        }
    }

    private suspend fun postError(message: String) {
        _errorMessage.send(message)
    }

}
