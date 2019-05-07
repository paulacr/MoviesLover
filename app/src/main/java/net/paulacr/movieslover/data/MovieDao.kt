package net.paulacr.movieslover.data

import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import io.reactivex.Observable
import net.paulacr.movieslover.data.model.Movie

interface MovieDao {

    @Query("SELECT * FROM movie")
    fun getAll(): Observable<List<Movie>>

    @Insert
    fun insertAll(movies: List<Movie>)

    @Delete
    fun delete(user: Movie)
}