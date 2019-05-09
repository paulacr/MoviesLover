package net.paulacr.movieslover.data.repository

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import net.paulacr.movieslover.data.MoviesDatabase
import net.paulacr.movieslover.data.model.Genre
import net.paulacr.movieslover.network.ApiInterface

class GenresRepositoryImpl(val service: ApiInterface, val db: MoviesDatabase) : GenresRepository {

    override fun getGenresFromApi(): Observable<List<Genre>> {
        return service.getGenres()
            .map {
                it.genres
            }.doOnNext {
                db.genre().insertAll(it)
            }
    }

    override fun getGenresFromDatabase(): Observable<List<Genre>> {
        return db.genre().getAll().toObservable()
    }

    override fun getGenres(): Observable<List<Genre>> {
        return db.genre().getAll().toObservable().subscribeOn(Schedulers.io()).flatMap {
            if (it.isNullOrEmpty()) {
                getGenresFromApi()
            } else {
                Observable.just(it)
            }
        }
    }

    companion object {
        private const val EXPIRATION_TIME = 86400000
    }
}