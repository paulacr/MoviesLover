package net.paulacr.movieslover

import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import net.paulacr.movieslover.data.model.Genre
import net.paulacr.movieslover.data.model.Genres
import net.paulacr.movieslover.data.model.Movie
import net.paulacr.movieslover.data.model.MoviesResult
import net.paulacr.movieslover.data.repository.GenresRepositoryImpl
import net.paulacr.movieslover.data.repository.MoviesRepositoryImpl
import net.paulacr.movieslover.di.moviesApp
import net.paulacr.movieslover.network.ApiInterface
import net.paulacr.movieslover.ui.movieslist.MoviesListViewModel
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.Mockito.`when`

class MoviesListViewModelTest : KoinTest {

    private val testScheduler = TestScheduler()

    val repository: MoviesRepositoryImpl by inject()
    val genresRepository: GenresRepositoryImpl by inject()
    val moviesListViewModel: MoviesListViewModel by inject()
    val service: ApiInterface by inject()

    val movieMock = Movie(1, "title", listOf("1", "2"), "test", "2019-04-05", 20.toDouble(),
        "Some overview string", "en", "path", 1)

    @Before
    fun onStart() {
        startKoin {
            modules(moviesApp)
        }
    }

    @Test
    fun combineMovieWithGenresTest() {
//        `when`(movieDatabase.movie().getAll()).thenReturn(Single.never())
        `when`(service.getGenres()).thenReturn(Observable.just(Genres(listOf(Genre(1, "comedy")))))
        `when`(service.getPopularMovies(page = "1")).thenReturn(Observable.just(MoviesResult(1, 29, listOf(movieMock))))

        moviesListViewModel.getPopularMovies()
    }
}