package net.paulacr.movieslover.data.repository

import io.reactivex.Observable
import net.paulacr.movieslover.data.model.Genres
import net.paulacr.movieslover.data.model.MoviesResult

interface MoviesRepository {

    fun getPopularMovies(page: String): Observable<MoviesResult>

    fun getPopularMoviesFromDB(page: String): Observable<MoviesResult>

    fun getPopularMoviesFromAPI(page: String): Observable<MoviesResult>

    fun fetchMoviesBySearch(text: String): Observable<MoviesResult>

    fun getGenres(): Observable<Genres>
}