package net.paulacr.movieslover.ui.movieslist

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleObserver
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import net.paulacr.movieslover.data.model.MoviesResult
import net.paulacr.movieslover.data.repository.MoviesRepositoryImpl

class MoviesListViewModel(app: Application, private val repository: MoviesRepositoryImpl) : AndroidViewModel(app),
    LifecycleObserver {

    private lateinit var moviesDisposable: Disposable
    private var compositeDisposable = CompositeDisposable()

    fun getPopularMovies() {
        // perform call for testing service
        moviesDisposable = repository.getPopularMovies("1")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ movies: MoviesResult ->
                Log.i("Log movies", "movies list $movies")
            }, { error ->
                Log.e("Log movies", "movies error $error")
            })
        compositeDisposable.add(moviesDisposable)
    }
}