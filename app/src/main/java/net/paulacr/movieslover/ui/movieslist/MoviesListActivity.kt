package net.paulacr.movieslover.ui.movieslist

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import net.paulacr.movieslover.R
import org.koin.android.viewmodel.ext.android.viewModel

class MoviesListActivity : AppCompatActivity() {

    private val viewModel: MoviesListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.getPopularMovies()
    }
}