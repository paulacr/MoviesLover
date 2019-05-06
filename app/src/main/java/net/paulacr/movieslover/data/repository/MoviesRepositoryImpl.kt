package net.paulacr.movieslover.data.repository

import io.reactivex.Observable
import net.paulacr.movieslover.data.model.Genre
import net.paulacr.movieslover.data.model.MoviesResult
import net.paulacr.movieslover.network.ApiInterface

class MoviesRepositoryImpl(val service: ApiInterface) : MoviesRepository {

    override fun getPopularMovies(page: String): Observable<MoviesResult> {
        return service.getPopularMovies(page = page)
    }

    override fun fetchMoviesBySearch(text: String): Observable<MoviesResult> {
        return service.getPopularMovies(page = "1")
    }

    override fun getGenres(): Observable<List<Genre>> {
        return service.getGenres()
    }
}