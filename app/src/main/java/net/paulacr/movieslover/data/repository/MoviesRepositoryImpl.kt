package net.paulacr.movieslover.data.repository

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import net.paulacr.movieslover.data.MoviesDatabase
import net.paulacr.movieslover.data.model.Genres
import net.paulacr.movieslover.data.model.MoviesResult
import net.paulacr.movieslover.network.ApiInterface

class MoviesRepositoryImpl(val service: ApiInterface, val db: MoviesDatabase) :
    MoviesRepository {

    override fun getPopularMovies(page: String): Observable<MoviesResult> {
        return Observable.concatArrayDelayError(getPopularMoviesFromDB(page), getPopularMoviesFromAPI(page))
    }

    override fun getPopularMoviesFromAPI(page: String): Observable<MoviesResult> {
        return service.getPopularMovies(page = page).subscribeOn(Schedulers.io())
            .doOnNext {
                (db as MoviesDatabase).moviesResultDao().insertAll(it)
            }
    }

    override fun getPopularMoviesFromDB(page: String): Observable<MoviesResult> {
        return db.moviesResultDao().getAll().map {
            it.get(0)
        }.toObservable()
    }

    override fun fetchMoviesBySearch(text: String): Observable<MoviesResult> {
        return service.getPopularMovies(page = "1")
    }

    override fun getGenres(): Observable<Genres> {
        return service.getGenres()
    }
}