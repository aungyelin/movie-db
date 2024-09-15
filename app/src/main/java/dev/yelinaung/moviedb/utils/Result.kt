package dev.yelinaung.moviedb.utils

sealed class ResultWrapper<out T> {
    data class Success<out T>(val data: T) : ResultWrapper<T>()
    data class Error(val message: String) : ResultWrapper<Nothing>()
    class Loading(val isLoading: Boolean = true): ResultWrapper<Nothing>()
}