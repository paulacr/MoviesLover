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
import net.paulacr.movieslover.data.model.MovieWithGenres
import net.paulacr.movieslover.data.model.Movie
import net.paulacr.movieslover.data.repository.GenresRepositoryImpl
import net.paulacr.movieslover.data.repository.MoviesRepositoryImpl
import net.paulacr.movieslover.livedata.LiveDataWithValue

class MoviesListViewModel(app: Application, private val moviesRepository: MoviesRepositoryImpl, private val genresRepository: GenresRepositoryImpl) : AndroidViewModel(app),
    LifecycleObserver {

    private lateinit var moviesDisposable: Disposable
    private lateinit var moviesSearchDisposable: Disposable
    private var compositeDisposable = CompositeDisposable()
    private var page: Int = 1

    var subject = BehaviorSubject.create<Unit>()
    var moviesAction = LiveDataWithValue<List<MovieWithGenres>>()
    var searchAction = LiveDataWithValue<List<MovieWithGenres>>()

    fun getPopularMovies() {
        subscribeSubject()
    }

    private fun subscribeSubject() {
        val moviesObservable = subject.startWith(Unit)
            .flatMap {
                moviesRepository.getPopularMovies(page)
            }

        val genresObservable: Observable<List<Genre>> = genresRepository.getGenres()

        moviesDisposable = Observable.combineLatest(moviesObservable, genresObservable,
            BiFunction { movies: List<Movie>, genres: List<Genre> -> moviesWithGenres(movies, genres) })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ moviesWithGenres ->
                increasePageNumber()
                moviesAction.actionOccuredPost(moviesWithGenres)
            }, { error ->
                Log.e("Test error", "test", error)
            }, {
            })

        compositeDisposable.add(moviesDisposable)
    }

    fun searchMovies(text: String) {
        val moviesObservable = moviesRepository.fetchMoviesBySearch(text)
            .subscribeOn(Schedulers.io())

        val genresObservable: Observable<List<Genre>> = genresRepository.getGenres()

        moviesSearchDisposable = Observable.combineLatest(moviesObservable, genresObservable,
            BiFunction { movies: List<Movie>, genres: List<Genre> -> moviesWithGenres(movies, genres) })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ moviesWithGenres ->
                searchAction.actionOccuredPost(moviesWithGenres)
            }, { error ->
                Log.e("Test error", "test", error)
            }, {
            })
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    /**
     * Combine Movies with Genres into a Wrapper
     *
     * @param movies
     * @param genres list with all genres
     * @return MovieWithGenre
     */
    private fun moviesWithGenres(movies: List<Movie>, genres: List<Genre>): List<MovieWithGenres> {

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
        genres: List<Genre>,
        id: String
    ): MutableList<Genre> {
        val list = genres.filter {
            it.id.toString() == id
        }.toMutableList()
        return list
    }

    private fun increasePageNumber() {
        page++
    }

    private fun decreasePageNumber() {
        page--
    }
}