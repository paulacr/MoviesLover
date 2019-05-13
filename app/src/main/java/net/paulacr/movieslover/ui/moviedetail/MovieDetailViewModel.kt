package net.paulacr.movieslover.ui.moviedetail

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleObserver
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import net.paulacr.movieslover.data.model.MovieDetail
import net.paulacr.movieslover.data.model.MovieWithGenres
import net.paulacr.movieslover.data.repository.MoviesRepositoryImpl
import net.paulacr.movieslover.livedata.LiveDataWithValue

class MovieDetailViewModel(app: Application, val moviesRepository: MoviesRepositoryImpl) : AndroidViewModel(app),
    LifecycleObserver {

    private lateinit var moviesDetailDisposable: Disposable
    private var compositeDisposable = CompositeDisposable()

    var movieDetailAction = LiveDataWithValue<MovieDetail>()
    val movieTitle = ObservableField<String>()
    val movieDescription = ObservableField<String>()
    val movieLanguage = ObservableField<String>()
    val movieRuntime = ObservableField<String>()
    val movieLink = ObservableField<String>()
    val showAdditionalInfo = ObservableBoolean(false)

    fun getMovieDetail(movieWithGenres: MovieWithGenres) {
        setPreviousData(movieWithGenres)
        val id = movieWithGenres.movie.id
        moviesDetailDisposable = moviesRepository.getMovieDetail(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                showAdditionalInfo.set(true)
                setAdditionalData(it)
            }, {
                Log.e("Error movie detail", "->", it)
            })
        compositeDisposable.add(moviesDetailDisposable)
    }

    private fun setAdditionalData(movieDetail: MovieDetail) {
        movieDetailAction.actionOccuredPost(movieDetail)
    }

    private fun setPreviousData(movieWithGenres: MovieWithGenres) {
        movieTitle.set(movieWithGenres.movie.title)
        movieDescription.set(movieWithGenres.movie.overview)
        movieLanguage.set("Language ${movieWithGenres.movie.language}")
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}