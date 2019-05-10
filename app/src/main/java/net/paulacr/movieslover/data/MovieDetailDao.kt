package net.paulacr.movieslover.data

import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Dao
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Delete
import io.reactivex.Maybe
import net.paulacr.movieslover.data.model.MovieDetail

@Dao
interface MovieDetailDao {

    @Query("SELECT * FROM movie_detail WHERE :id")
    fun getMovieDetail(id: Int): Maybe<MovieDetail>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMovieDetail(movie: MovieDetail)

    @Delete
    fun delete(movie: MovieDetail)
}