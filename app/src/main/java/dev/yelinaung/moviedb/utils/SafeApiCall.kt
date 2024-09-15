package dev.yelinaung.moviedb.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(apiCall: suspend () -> T): ResultWrapper<T> {

    return withContext(Dispatchers.IO) {
        try {
            ResultWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> {
                    ResultWrapper.Error("Network Error. Please check your connection.")
                }

                is HttpException -> {
                    ResultWrapper.Error(
                        message = throwable.message() ?: "Something went wrong.",
                    )
                }

                else -> {
                    ResultWrapper.Error("Unexpected Error: ${throwable.message}")
                }
            }
        }
    }

}
