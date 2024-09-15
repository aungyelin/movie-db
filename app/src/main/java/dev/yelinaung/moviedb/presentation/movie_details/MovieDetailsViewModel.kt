package dev.yelinaung.moviedb.presentation.movie_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import dev.yelinaung.moviedb.data.service.model.movie_details.MovieDetailsResponse
import dev.yelinaung.moviedb.data.service.model.movie_details.mapToMovieEntity
import dev.yelinaung.moviedb.data.service.model.player.Video
import dev.yelinaung.moviedb.domain.MovieRepository
import dev.yelinaung.moviedb.utils.ResultWrapper
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movieDetailsState = MutableStateFlow<MovieDetailsResponse?>(null)
    val movieDetailsState = _movieDetailsState.asStateFlow()

    private var _errorMessage = Channel<String?>()
    val errorMessage = _errorMessage.receiveAsFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()

    // State for the first video
    private val _videoState = MutableStateFlow<Video?>(null)
    val videoState = _videoState.asStateFlow()

    // Preventing race conditions or multiple database updates from over clicking.
    private val mutex = Mutex() // For debouncing

    fun fetchMovieDetails(id: Int) {
        viewModelScope.launch {
        repository.getMovieDetails(id).collectLatest { result ->
            when (result) {
                is ResultWrapper.Success -> {
                    _movieDetailsState.value = result.data
                    checkIfFavorite(id)
                    fetchMovieVideos(id) // Pre-fetch video key immediately
                }
                is ResultWrapper.Error -> {
                    _errorMessage.send(result.message)
                }

                else -> {}
            }
        }
        }
    }

     private suspend fun fetchMovieVideos(movieId: Int) {
            repository.getMovieVideo(movieId).collectLatest { videos ->
                when (videos) {
                    is ResultWrapper.Success -> {
                        // Update the video state with the first video code (if available)
                        _videoState.value = videos.data.firstOrNull()
                    }

                    is ResultWrapper.Error -> {
                        // Update the error message state
                        _errorMessage.send(videos.message)
                    }

                    else -> {}
                }
            }
        }


    private fun checkIfFavorite(movieId: Int) {
        viewModelScope.launch {
            repository.getAllFavoriteMovies().collect { favoriteMovies ->
                _isFavorite.value = favoriteMovies.any { it.id == movieId }
            }
        }
    }

    fun toggleFavorite(movieDetails: MovieDetailsResponse) {
        viewModelScope.launch {
            if (mutex.tryLock()) {
                try {
                    val movieEntity = movieDetails.mapToMovieEntity()
                    if (_isFavorite.value) {
                        repository.deleteFavoriteMovie(movieEntity)
                    } else {
                        repository.addFavoriteMovie(movieEntity)
                    }
                    _isFavorite.value = !_isFavorite.value
                } finally {
                    mutex.unlock()
                }
            }
        }
    }
}
