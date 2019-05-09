package net.paulacr.movieslover.ui.moviedetail

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import net.paulacr.movieslover.data.model.MovieWithGenres
import net.paulacr.movieslover.livedata.LiveDataWithValue

class MovieDetailViewModel(app: Application) : AndroidViewModel(app),
    LifecycleObserver {

    private lateinit var moviesDisposable: Disposable
    private var compositeDisposable = CompositeDisposable()
    private var page: Int = 1

    var subject = BehaviorSubject.create<Unit>()
    var moviesAction = LiveDataWithValue<List<MovieWithGenres>>()

//    fun setImageBackdrop() {
//
//    }
//
//    fun setRuntime() {
//
//    }
}