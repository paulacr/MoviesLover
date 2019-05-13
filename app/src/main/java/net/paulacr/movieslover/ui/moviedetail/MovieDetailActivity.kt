package net.paulacr.movieslover.ui.moviedetail

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.text.HtmlCompat
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_movie_detail.*
import net.paulacr.movieslover.R
import net.paulacr.movieslover.data.model.MovieDetail
import net.paulacr.movieslover.data.model.MovieWithGenres
import net.paulacr.movieslover.databinding.ActivityMovieDetailBinding
import org.koin.android.viewmodel.ext.android.viewModel

class MovieDetailActivity : AppCompatActivity() {

    private val viewModel: MovieDetailViewModel by viewModel()
    private var movieDetail: MovieDetail? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        applyDataBinding()

        val movieWithGenres = getMovieExtra()
        movieWithGenres?.let {
            viewModel.getMovieDetail(movieWithGenres)
            setBackdropImage(movieWithGenres.backdropPath)
            observeActions()
        }

        movieLinkDetail.setOnClickListener {
            openMovieUrl()
        }
    }

    private fun setBackdropImage(backdropPath: String?) {
        backdropPath?.let {
            Glide.with(this).load(backdropPath)
                .placeholder(R.mipmap.ic_launcher)
                .into(movieBackdropDetail)
        }
    }

    private fun observeActions() {
        viewModel.movieDetailAction.observe(this) {
            movieDetail = it
            // update view with additional info
            movieDetail?.let {
                movieRuntimeDetail.text = it.runtime
                movieLinkDetail.text =
                    HtmlCompat.fromHtml("Link: ${it.homepage}", HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
            }
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

    private fun openMovieUrl() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(movieDetail?.homepage)
        startActivity(intent)
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