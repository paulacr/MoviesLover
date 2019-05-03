package net.paulacr.movieslover.network

import io.reactivex.Observable
import net.paulacr.movieslover.model.Movie
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("movie/upcoming")
    fun getUpcomingMovies(@Query("language") language: String?, @Query("page") page: String?): Observable<List<Movie>>
}