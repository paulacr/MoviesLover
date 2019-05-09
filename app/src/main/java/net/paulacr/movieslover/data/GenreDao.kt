package net.paulacr.movieslover.data

import android.arch.persistence.room.Delete
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Single
import net.paulacr.movieslover.data.model.Genre

@Dao
interface GenreDao {

    @Query("SELECT * FROM genre")
    fun getAll(): Single<List<Genre>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(movies: List<Genre>)

    @Delete
    fun delete(genre: Genre)
}