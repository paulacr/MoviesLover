package net.paulacr.movieslover.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import net.paulacr.movieslover.data.model.MoviesResult

@Database(entities = [MoviesResult::class], version = 1)
abstract class MoviesDatabase : RoomDatabase() {

    abstract fun moviesResultDao(): MoviesResultDao

    companion object {
        private var TEST_DATABASE = false
        private const val databaseName = "movie_database.db"

        private var instance: MoviesDatabase? = null

        private val lock = Any()

        fun getInstance(context: Context): MoviesDatabase {
            synchronized(lock) {
                if (instance == null) {
                    if (TEST_DATABASE) {
                        instance = Room.inMemoryDatabaseBuilder(
                            context,
                            MoviesDatabase::class.java
                        ).allowMainThreadQueries()
                            .build()
                    } else {
                        instance = Room.databaseBuilder(
                            context,
                            MoviesDatabase::class.java,
                            databaseName
                        ).build()
                    }
                }
            }

            return instance!!
        }

        fun setupDatabase(isTesting: Boolean) {
            TEST_DATABASE = isTesting
        }
    }
}