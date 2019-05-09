package net.paulacr.movieslover.data.repository

import android.content.SharedPreferences
import android.util.Log
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import net.paulacr.movieslover.data.MoviesDatabase
import net.paulacr.movieslover.data.model.Movie
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

    private fun isExpired(): Boolean {
        return System.currentTimeMillis() < (System.currentTimeMillis() - EXPIRATION_TIME)
    }

    companion object {
        private const val EXPIRATION_TIME = 86400000
    }
}