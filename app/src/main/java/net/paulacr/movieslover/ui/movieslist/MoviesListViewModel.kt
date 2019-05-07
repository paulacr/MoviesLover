package net.paulacr.movieslover.ui.movieslist

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleObserver
import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import net.paulacr.movieslover.data.model.Genre
import net.paulacr.movieslover.data.model.Genres
import net.paulacr.movieslover.data.model.MoviesResult
import net.paulacr.movieslover.data.model.MovieWithGenres
import net.paulacr.movieslover.data.model.Movie
import net.paulacr.movieslover.data.repository.MoviesRepositoryImpl

class MoviesListViewModel(app: Application, private val repository: MoviesRepositoryImpl) : AndroidViewModel(app),
    LifecycleObserver {

    private lateinit var moviesDisposable: Disposable
    private var compositeDisposable = CompositeDisposable()
    private var page: Int = 1
    var subject = BehaviorSubject.create<Unit>()

    fun getPopularMovies() {
        subscribeSubject()
    }

    fun subscribeSubject() {
        val moviesObservable = subject.startWith(Unit)
            .flatMap {
                repository.getPopularMovies(getPage())
            }.map {
                it.results
            }

        val genresObservable: Observable<Genres> = repository.getGenres()

        moviesDisposable = Observable.combineLatest(moviesObservable, genresObservable,
            BiFunction { movies: List<Movie>, genres: Genres -> moviesWithGenres(movies, genres) })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ moviesWithGenres ->
                Log.i("Test", "test")
            }, { error ->
                Log.e("Test", "test")
            })

        compositeDisposable.add(moviesDisposable)
    }

    fun getMoreMovies(): Observable<MoviesResult> {
        return repository.getPopularMovies(getPage())
    }

    /**
     * Combine Movies with Genres into a Wrapper
     *
     * @param movies
     * @param genres list with all genres
     * @return MovieWithGenre
     */
    private fun moviesWithGenres(movies: List<Movie>, genres: Genres): List<MovieWithGenres> {

        val moviesWithGenresList = mutableListOf<MovieWithGenres>()
        val listOfGenres = mutableListOf<Genre>()

        movies.forEach { movie ->

            listOfGenres.clear()
            for (id in movie.genresIds) {
                // Filter genres by movie genres ids
                val list = filterGenresById(genres, id)
                listOfGenres.addAll(list)
            }

            // encapsulate Genres list into a Wrapper that join Movie and Genres
            moviesWithGenresList.add(MovieWithGenres(movie, listOfGenres.toMutableList()))
        }
        return moviesWithGenresList
    }

    /**
     * Filter List of Genres by Movie genres list ids
     *
     * @param genres
     * @param id
     * @return
     */
    private fun filterGenresById(
        genres: Genres,
        id: String
    ): MutableList<Genre> {
        val list = genres.genres.filter {
            it.id == id
        }.toMutableList()
        return list
    }

    private fun getPage(): String {
        return page.toString()
    }

    private fun increasePageNumber() {
        page++
    }

    private fun decreasePageNumber() {
        page--
    }
}