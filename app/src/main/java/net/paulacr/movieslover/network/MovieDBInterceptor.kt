package net.paulacr.movieslover.network

import net.paulacr.movieslover.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class MovieDBInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val newRequest = chain.request().newBuilder()
            .addHeader(API_KEY_NAME, BuildConfig.API_KEY)
            .build()

        return chain.proceed(newRequest)
    }

    companion object {
        const val API_KEY_NAME = "x-api-key"
    }
}