package net.paulacr.movieslover

import android.app.Application
import net.paulacr.movieslover.di.moviesModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MoviesApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(moviesModule).androidContext(applicationContext)
        }
    }
}