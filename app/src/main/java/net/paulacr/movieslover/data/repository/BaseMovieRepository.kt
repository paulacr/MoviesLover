package net.paulacr.movieslover.data.repository

import io.reactivex.Observable

abstract class BaseMovieRepository<T> {

    abstract fun getAllFromDB(): Observable<List<T>>

    abstract fun getItemFromDB(item: T): Observable<T>

    abstract fun getAllFromAPI(page: Int): Observable<List<T>>
}