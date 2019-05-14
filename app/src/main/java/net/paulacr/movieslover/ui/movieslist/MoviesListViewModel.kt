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
import java.util.concurrent.TimeUnit

class MoviesListViewModel(
    app: Application,
    private val moviesRepository: MoviesRepositoryImpl,
    private val genresRepository: GenresRepositoryImpl
) : AndroidViewModel(app),
    LifecycleObserver {

    private lateinit var moviesDisposable: Disposable
    private lateinit var moviesSearchDisposable: Disposable
    private lateinit var typeSearchDisposable: Disposable
    private var compositeDisposable = CompositeDisposable()

    var subject = BehaviorSubject.create<Unit>()
    var subjectSearch = BehaviorSubject.create<Unit>()
    var subjectTypeSearch = BehaviorSubject.create<String>()

    var moviesAction = LiveDataWithValue<List<MovieWithGenres>>()
    var searchAction = LiveDataWithValue<List<MovieWithGenres>>()
    var loadingMoreItems = ObservableBoolean(false)
    var loadingEmptyState = ObservableBoolean(true)

    init {
        loadingEmptyState.set(true)
    }

    fun getPopularMovies() {
        subscribeSubject(getMoviesObservable(), getGenresObservable())
    }

    fun subscribeTypeSearchSubject() {
        typeSearchDisposable = subjectTypeSearch.map { text -> text.trim() }
            .debounce(400, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { text ->
                searchMovies(text)
            }
    }

    private fun searchMovies(text: String?) {
        PageUtil.resetPage()
        if (text.isNullOrEmpty()) {
            subject.onNext(Unit)
            // get popular movies when search is empty and show in the list
        } else {
            subscribeSubjectSearch(getMoviesSearchObservable(text), getGenresObservable())
        }
    }

    private fun movieWithGenreObservable(
        moviesObservable: Observable<List<Movie>>,
        genresObservable: Observable<List<Genre>>
    ): Observable<List<MovieWithGenres>> {
        return Observable.combineLatest(moviesObservable, genresObservable,
            BiFunction { movies: List<Movie>, genres: List<Genre> ->
                MovieAndGenreUtil.moviesWithGenres(
                    movies,
                    genres
                )
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun subscribeSubject(moviesObservable: Observable<List<Movie>>, genresObservable: Observable<List<Genre>>) {

        moviesDisposable = movieWithGenreObservable(moviesObservable, genresObservable)
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

    private fun subscribeSubjectSearch(
        moviesObservable: Observable<List<Movie>>,
        genresObservable: Observable<List<Genre>>
    ) {

        moviesSearchDisposable = movieWithGenreObservable(moviesObservable, genresObservable)
            .subscribe({ moviesWithGenres ->
                loadingMoreItems.set(false)
                loadingEmptyState.set(false)
                PageUtil.increasePageNumber()
                moviesWithGenres.distinctBy {
                    it.movie.title
                }
                // show items on list
                searchAction.actionOccuredPost(moviesWithGenres)
            }, { error ->
                Log.e("Test error", "test", error)
            }, {
            })
        compositeDisposable.add(moviesDisposable)
    }

    // Observables
    private fun getMoviesObservable(): Observable<List<Movie>> {
        return subject.startWith(Unit)
            .subscribeOn(Schedulers.io())
            .flatMap {
                showLoadingMoreItems()
                moviesRepository.getPopularMovies(PageUtil.getCurrentPage())
            }
    }

    private fun getMoviesSearchObservable(text: String): Observable<List<Movie>> {
        return subject.startWith(Unit)
            .flatMap {
                showLoadingMoreItems()
                moviesRepository.fetchMoviesBySearch(text, PageUtil.getCurrentPage())
            }
    }

    private fun getGenresObservable(): Observable<List<Genre>> {
        return genresRepository.getGenres()
    }

    private fun showLoadingMoreItems() {
        if (PageUtil.getCurrentPage() > PageUtil.INITIAL_PAGE) {
            loadingMoreItems.set(true)
        }
    }

    fun searchMoreMovies() {
        subjectSearch.onNext(Unit)
    }

    fun getMoreMovies() {
        subject.onNext(Unit)
    }

    fun resetMoviesList() {
        PageUtil.resetPage()
        subject.onNext(Unit)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}