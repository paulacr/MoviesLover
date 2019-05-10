package net.paulacr.movieslover.data.repository

import android.content.SharedPreferences
import android.util.Log
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import net.paulacr.movieslover.data.MoviesDatabase
import net.paulacr.movieslover.data.model.Movie
import net.paulacr.movieslover.data.model.MovieDetail
import net.paulacr.movieslover.network.ApiInterface

class MoviesRepositoryImpl(val service: ApiInterface, val db: MoviesDatabase, sharedPreferences: SharedPreferences) :
    MoviesRepository {

    override fun getPopularMovies(page: Int): Observable<List<Movie>> {
        if (false) {
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
        return service.getPopularMovies(page = page.toString()).subscribeOn(Schedulers.io())
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

    override fun fetchMoviesBySearch(text: String): Observable<List<Movie>> {
        return service.getPopularMovies(page = "1").map {
            it.results
        }
    }

    override fun getMovieDetail(movieId: Int): Observable<MovieDetail> {
        return getMovieDetailFromDB(movieId)
            .switchIfEmpty {
                getMovieDetailFromAPI(movieId)
            }
            .flatMap {
                Observable.just(it)
            }.doOnError {
                getMovieDetailFromAPI(movieId)
            }
    }

    override fun getMovieDetailFromDB(movieId: Int): Observable<MovieDetail> {
        return db.movieDetail().getMovieDetail(movieId).toObservable().subscribeOn(Schedulers.io())
            .doOnError {
                Log.e("Error database", "msg: ", it)
            }
    }

    override fun getMovieDetailFromAPI(movieId: Int): Observable<MovieDetail> {
        return service.getMovieDetail(movieId).subscribeOn(Schedulers.io())
            .doOnError {
                Log.e("Error detail", "api", it)
            }
            .doOnNext {
                db.movieDetail().saveMovieDetail(it)
            }.doOnError {
                Log.e("Error database", "msg: ", it)
            }
    }

    private fun isExpired(): Boolean {
        return System.currentTimeMillis() < (System.currentTimeMillis() - EXPIRATION_TIME)
    }

    companion object {
        private const val EXPIRATION_TIME = 86400000
    }
}