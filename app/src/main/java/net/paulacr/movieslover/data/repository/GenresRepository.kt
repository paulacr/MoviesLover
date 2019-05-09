package net.paulacr.movieslover.data.repository

import io.reactivex.Observable
import net.paulacr.movieslover.data.model.Genre

interface GenresRepository {

    fun getGenresFromApi(): Observable<List<Genre>>

    fun getGenresFromDatabase(): Observable<List<Genre>>

    fun getGenres(): Observable<List<Genre>>
}