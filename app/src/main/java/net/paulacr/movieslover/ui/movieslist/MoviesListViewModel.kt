package net.paulacr.movieslover.ui.movieslist

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleObserver
import android.databinding.ObservableBoolean
import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import net.paulacr.movieslover.data.model.Genre
import net.paulacr.movieslover.data.model.MovieWithGenres
import net.paulacr.movieslover.data.model.Movie
import net.paulacr.movieslover.data.repository.GenresRepositoryImpl
import net.paulacr.movieslover.data.repository.MoviesRepositoryImpl
import net.paulacr.movieslover.livedata.LiveDataWithValue
import net.paulacr.movieslover.util.MovieAndGenreUtil
import net.paulacr.movieslover.util.PageUtil

class MoviesListViewModel(
    app: Application,
    private val moviesRepository: MoviesRepositoryImpl,
    private val genresRepository: GenresRepositoryImpl
) : AndroidViewModel(app),
    LifecycleObserver {

    private lateinit var moviesDisposable: Disposable
    private var compositeDisposable = CompositeDisposable()

    var subject = BehaviorSubject.create<Unit>()
    var moviesAction = LiveDataWithValue<List<MovieWithGenres>>()
    var loadingMoreItems = ObservableBoolean(false)
    var loadingEmptyState = ObservableBoolean(true)

    init {
        loadingEmptyState.set(true)
    }

    fun getPopularMovies() {
        subscribeSubject()
    }

    fun refreshData() {
        PageUtil.resetPage()
        subscribeSubject()
    }

    private fun subscribeSubject() {
        val moviesObservable = subject.startWith(Unit)
            .flatMap {
                showLoadingMoreItems()
                moviesRepository.getPopularMovies(PageUtil.getCurrentPage())
            }

        val genresObservable: Observable<List<Genre>> = genresRepository.getGenres()

        moviesDisposable = Observable.combineLatest(moviesObservable, genresObservable,
            BiFunction { movies: List<Movie>, genres: List<Genre> -> MovieAndGenreUtil.moviesWithGenres(movies, genres) })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ moviesWithGenres ->
                loadingMoreItems.set(false)
                loadingEmptyState.set(false)
                PageUtil.increasePageNumber()
                moviesAction.actionOccuredPost(moviesWithGenres)
            }, { error ->
                Log.e("Test error", "test", error)
            }, {
            })

        compositeDisposable.add(moviesDisposable)
    }

    private fun showLoadingMoreItems() {
        if (PageUtil.getCurrentPage() > 1) {
            loadingMoreItems.set(true)
        }
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}