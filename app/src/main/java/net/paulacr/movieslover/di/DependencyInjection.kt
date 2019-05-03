package net.paulacr.movieslover.di

import net.paulacr.movieslover.movieslist.MoviesListViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val moviesModule = module {
    viewModel { MoviesListViewModel(androidApplication()) }
}