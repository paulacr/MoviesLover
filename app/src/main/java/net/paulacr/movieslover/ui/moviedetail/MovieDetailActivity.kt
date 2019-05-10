package net.paulacr.movieslover.ui.moviedetail

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_movie_detail.*
import net.paulacr.movieslover.R
import net.paulacr.movieslover.data.model.MovieWithGenres
import net.paulacr.movieslover.databinding.ActivityMovieDetailBinding
import org.koin.android.viewmodel.ext.android.viewModel

class MovieDetailActivity : AppCompatActivity() {

    private val viewModel: MovieDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        applyDataBinding()

        val movieWithGenres = getMovieExtra()
        movieWithGenres?.let {
            viewModel.getMovieDetail(movieWithGenres)
            observeActions()
        }
    }

    private fun observeActions() {
        viewModel.movieDetailAction.observe(this) {
            // update view with additional info
            movieRuntimeDetail.text = it.runtime
            movieLinkDetail.text = it.homepage
        }
    }

    private fun getMovieExtra(): MovieWithGenres? {
        return intent?.extras?.getParcelable<MovieWithGenres>(MOVIE_EXTRA)
    }

    private fun applyDataBinding() {
        DataBindingUtil.setContentView<ActivityMovieDetailBinding>(this, R.layout.activity_movie_detail).apply {
            activityViewModel = viewModel
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