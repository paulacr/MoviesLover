package net.paulacr.movieslover.data.repository

import android.content.SharedPreferences
import android.util.Log
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import net.paulacr.movieslover.data.MoviesDatabase
import net.paulacr.movieslover.data.model.Movie
import net.paulacr.movieslover.data.model.MovieDetail
import net.paulacr.movieslover.network.ApiInterface
import java.util.concurrent.TimeUnit

class MoviesRepositoryImpl(val service: ApiInterface, val db: MoviesDatabase, sharedPreferences: SharedPreferences) :
    MoviesRepository {

    override fun getPopularMovies(page: Int): Observable<List<Movie>> {
        if (TIME) {
            clearDatabase()
            return getPopularMoviesFromAPI(page).subscribeOn(Schedulers.io())
        } else {
            return getPopularMoviesFromDB(page).subscribeOn(Schedulers.io()).flatMap {
                if (it.isNullOrEmpty()) {
                    getPopularMoviesFromAPI(page)
                } else {
                    Observable.just(it)
                }
            }
        }
    }

    override fun getPopularMoviesFromAPI(page: Int): Observable<List<Movie>> {
        return service.getPopularMovies(page = page.toString())
            .subscribeOn(Schedulers.io())
            .debounce(TIME_OUT, TimeUnit.MILLISECONDS)
            .doOnError {
                Log.e("Error movies", "api", it)
            }
            .map {
                it.results
            }.doOnNext {
                it.forEach {
                    it.page = page
                }
                db.movie().insertAll(it)
            }.doOnError {
                Log.e("Error database", "msg: ", it)
            }
    }

    override fun getPopularMoviesFromDB(page: Int): Observable<List<Movie>> {
        return db.movie().getMoviesByPage(page).doOnError {
            Log.e("Error database", "msg: ", it)
        }.toObservable()
    }

    override fun fetchMoviesBySearch(text: String, page: Int): Observable<List<Movie>> {
        return db.movie().getMoviesByName(text)
            .toObservable()
            .flatMap { localResult ->
                if (localResult.isEmpty()) {
                    service.searchMovies(text, page = page)
                        .map {
                            it.results
                        }.doOnNext {
                            it.forEach {
                                it.page = page
                            }
                        }
                } else {
                    Observable.just(localResult)
                }
            }
    }

    override fun getMovieDetail(movieId: Int): Observable<MovieDetail> {
        return getMovieDetailFromDB(movieId)
            .debounce(TIME_OUT, TimeUnit.MILLISECONDS)
            .switchIfEmpty(getMovieDetailFromAPI(movieId))
            .onErrorResumeNext(getMovieDetailFromAPI(movieId))
    }

    override fun getMovieDetailFromDB(movieId: Int): Observable<MovieDetail> {
        return db.movieDetail().getMovieDetail(movieId).toObservable().subscribeOn(Schedulers.io())
            .doOnError {
                Log.e("Error database", "msg: ", it)
            }
    }

    override fun getMovieDetailFromAPI(movieId: Int): Observable<MovieDetail> {
        return service.getMovieDetail(movieId)
            .debounce(TIME_OUT, TimeUnit.MILLISECONDS)
            .doOnError {
                Log.e("Error detail", "api", it)
            }
            .doOnNext {
                db.movieDetail().saveMovieDetail(it)
            }.doOnError {
                Log.e("Error database", "msg: ", it)
            }
    }

    private fun clearDatabase() {
        db.clearAllTables()
    }

    private val TIME = false

    companion object {
        private const val TIME_OUT = 400.toLong()
    }
}