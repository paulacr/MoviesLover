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
    private lateinit var typeSearchDisposable: Disposable
    private var compositeDisposable = CompositeDisposable()

    var subject = BehaviorSubject.create<Unit>()
    var subjectSearch = BehaviorSubject.create<Unit>()
    var subjectTypeSearch = BehaviorSubject.create<String>()

    var moviesAction = LiveDataWithValue<List<MovieWithGenres>>()
    var searchAction = LiveDataWithValue<List<MovieWithGenres>>()
    var loadingMoreItems = ObservableBoolean(false)
    var loadingEmptyState = ObservableBoolean(true)
    var emptyStateView = ObservableBoolean(false)

    init {
        loadingEmptyState.set(true)
        emptyStateView.set(false)
    }

    fun getPopularMovies() {
        subscribeSubject(getMoviesObservable(), getGenresObservable())
    }

    private fun createCombination(searchText: String): Observable<List<MovieWithGenres>>? {
        return Observable.combineLatest(getMoviesSearchObservable(searchText), getGenresObservable(),
            BiFunction { movies: List<Movie>, genres: List<Genre> ->
                MovieAndGenreUtil.moviesWithGenres(
                    movies,
                    genres
                )
            })
            .onErrorResumeNext(Observable.just(emptyList()))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun subscribeTypeSearchSubject() {
        typeSearchDisposable = subjectTypeSearch
            .map { text -> text.trim() }
            .debounce(400, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap {
                createCombination(it)
            }
            .subscribe({ movieWithGenresList ->
                setMoviesList(movieWithGenresList)
            }, {
                setSearchEmptyState()
            })
    }

    private fun setMoviesList(movieWithGenresList: List<MovieWithGenres>) {
        if (movieWithGenresList.isNotEmpty()) {
            emptyStateView.set(false)
            onMoviesResult(movieWithGenresList)
            searchAction.actionOccuredPost(movieWithGenresList)
        } else {
            emptyStateView.set(true)
        }
    }

    private fun setSearchEmptyState() {
        emptyStateView.set(true)
    }

    private fun onMoviesResult(moviesWithGenres: List<MovieWithGenres>) {
        loadingMoreItems.set(false)
        loadingEmptyState.set(false)
        PageUtil.increasePageNumber()
        moviesWithGenres.distinctBy {
            it.movie.title
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
                if (moviesWithGenres.isEmpty()) {
                    emptyStateView.set(true)
                } else {
                    emptyStateView.set(false)
                    onMoviesResult(moviesWithGenres)
                    moviesAction.actionOccuredPost(moviesWithGenres)
                }
            }, { error ->
                Log.e(" Test error", "test", error)
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
        return subjectSearch.startWith(Unit)
            .flatMap {
                showLoadingMoreItems()
                moviesRepository.fetchMoviesBySearch(text, PageUtil.getCurrentPage()).subscribeOn(Schedulers.io())
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

    fun searchMoreMovies(text: String) {
        subjectTypeSearch.onNext(text)
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