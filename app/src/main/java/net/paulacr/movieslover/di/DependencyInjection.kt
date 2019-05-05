package net.paulacr.movieslover.di

import net.paulacr.movieslover.data.repository.MoviesRepositoryImpl
import net.paulacr.movieslover.network.NetworkManager.generalApi
import net.paulacr.movieslover.ui.movieslist.MoviesListViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val moviesModule = module {
    viewModel { MoviesListViewModel(androidApplication(), get()) }
}

val networkModule = module {
    single { generalApi }
}

val repositoryModule = module {
    single { MoviesRepositoryImpl(get()) }
}

val moviesApp = listOf(moviesModule, networkModule, repositoryModule)
