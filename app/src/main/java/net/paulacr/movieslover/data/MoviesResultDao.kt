package net.paulacr.movieslover.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import io.reactivex.Single
import net.paulacr.movieslover.data.model.MoviesResult

@Dao
interface MoviesResultDao {

    @Query("SELECT * FROM moviesresult")
    fun getAll(): Single<List<MoviesResult>>

    @Insert
    fun insertAll(movies: MoviesResult)

    @Delete
    fun delete(moviesResult: MoviesResult)
}