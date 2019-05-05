package net.paulacr.movieslover

import android.app.Application
import net.paulacr.movieslover.di.moviesApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MoviesApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(moviesApp).androidContext(applicationContext)
        }
    }
}