package dev.yelinaung.moviedb.data.service

import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(private val apiKey: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val url = original.url.newBuilder()
            .addQueryParameter("api_key", apiKey)
            .build()

        val request = original.newBuilder().url(url).build()

        return chain.proceed(request)
    }

}