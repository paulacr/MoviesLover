package net.paulacr.movieslover.di

import android.arch.persistence.room.Room
import net.paulacr.movieslover.data.MoviesDatabase
import net.paulacr.movieslover.data.repository.MoviesRepositoryImpl
import net.paulacr.movieslover.network.NetworkManager.generalApi
import net.paulacr.movieslover.ui.movieslist.MoviesListViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val moviesModule = module {
    viewModel { MoviesListViewModel(androidApplication(), get()) }
}

val apiInterface = module {
    single { generalApi }
}

val databaseModule = module {
    single { Room.databaseBuilder(androidContext(), MoviesDatabase::class.java, "movie_database.db").build() }
}

val repositoryModule = module {
    single { MoviesRepositoryImpl(get(), get()) }
}

val moviesApp = listOf(databaseModule, moviesModule, apiInterface, repositoryModule)
