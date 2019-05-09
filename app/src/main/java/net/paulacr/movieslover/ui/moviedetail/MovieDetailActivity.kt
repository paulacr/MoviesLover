package net.paulacr.movieslover.ui.moviedetail

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_movie_detail.*
import net.paulacr.movieslover.R
import net.paulacr.movieslover.data.model.MovieWithGenres
import net.paulacr.movieslover.databinding.ActivityMainBinding

class MovieDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        getMovie()
    }

    private fun getMovie() {
        val movie = intent?.extras?.getParcelable<MovieWithGenres>(MOVIE_EXTRA)
        movie?.let {
            setViewData(movie)
        }
    }

    private fun setViewData(movieWithGenre: MovieWithGenres) {
        movieTitleDetail.text = movieWithGenre.movie.title
        movieDescriptionDetail.text = movieWithGenre.movie.overview
        movieRuntimeDetail.text = "x"
        movieLinkDetail.text = "x"
    }

    private fun applyDataBinding() {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
//            activityViewModel = viewModel
        }
    }

    companion object {

        private const val MOVIE_EXTRA = "movie_extra"

        fun newIntent(context: Context, movieWithGenres: MovieWithGenres): Intent {
            val intent = Intent(context, MovieDetailActivity::class.java)
            intent.putExtra(MOVIE_EXTRA, movieWithGenres)
            return intent
        }
    }
}

interface MovieListener {
    fun onItemClick(position: Int, movieWithGenres: MovieWithGenres)
}