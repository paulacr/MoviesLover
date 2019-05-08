package net.paulacr.movieslover

import android.app.Application
import com.facebook.stetho.Stetho
import net.paulacr.movieslover.di.moviesApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MoviesApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)
        startKoin {
            modules(moviesApp).androidContext(applicationContext)
        }
    }
}