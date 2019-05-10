package net.paulacr.movieslover.data.repository

import io.reactivex.Observable
import net.paulacr.movieslover.data.model.Movie
import net.paulacr.movieslover.data.model.MovieDetail

interface MoviesRepository {

    fun getPopularMovies(page: Int): Observable<List<Movie>>

    fun getPopularMoviesFromDB(page: Int): Observable<List<Movie>>

    fun getPopularMoviesFromAPI(page: Int): Observable<List<Movie>>

    fun fetchMoviesBySearch(text: String): Observable<List<Movie>>

    fun getMovieDetail(movieId: Int): Observable<MovieDetail>

    fun getMovieDetailFromDB(movieId: Int): Observable<MovieDetail>

    fun getMovieDetailFromAPI(movieId: Int): Observable<MovieDetail>
}