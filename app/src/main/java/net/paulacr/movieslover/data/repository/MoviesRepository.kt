package net.paulacr.movieslover.data.repository

import io.reactivex.Observable
import net.paulacr.movieslover.data.model.Genres
import net.paulacr.movieslover.data.model.Movie

interface MoviesRepository {

    fun getPopularMovies(page: String): Observable<List<Movie>>

    fun getPopularMoviesFromDB(page: String): Observable<List<Movie>>

    fun getPopularMoviesFromAPI(page: String): Observable<List<Movie>>

    fun fetchMoviesBySearch(text: String): Observable<List<Movie>>

    fun getGenres(): Observable<Genres>
}