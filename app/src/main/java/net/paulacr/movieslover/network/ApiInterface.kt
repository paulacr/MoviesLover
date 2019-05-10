package net.paulacr.movieslover.network

import io.reactivex.Observable
import net.paulacr.movieslover.BuildConfig
import net.paulacr.movieslover.data.model.Genres
import net.paulacr.movieslover.data.model.MovieDetail
import net.paulacr.movieslover.data.model.MoviesResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("movie/popular")
    fun getPopularMovies(@Query("api_key") apiKey: String? = BuildConfig.API_KEY, @Query("language") language: String? = DEFAULT_LANGUAGE, @Query("page") page: String?): Observable<MoviesResult>

    @GET("movie/{movie_id}")
    fun getMovieDetail(@Path("movie_id") movieId: Int, @Query("api_key") apiKey: String? = BuildConfig.API_KEY, @Query("language") language: String? = DEFAULT_LANGUAGE): Observable<MovieDetail>

    @GET("genre/movie/list")
    fun getGenres(@Query("api_key") apiKey: String? = BuildConfig.API_KEY, @Query("language") language: String? = DEFAULT_LANGUAGE): Observable<Genres>

    companion object {
        private const val DEFAULT_LANGUAGE = "en"
    }
}