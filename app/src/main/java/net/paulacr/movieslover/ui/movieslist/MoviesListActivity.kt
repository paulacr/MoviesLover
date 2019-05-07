package net.paulacr.movieslover.ui.movieslist

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.android.databinding.library.baseAdapters.BR.activityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import net.paulacr.movieslover.R
import net.paulacr.movieslover.databinding.ActivityMainBinding
import net.paulacr.movieslover.util.InfiniteScrollManager
import org.koin.android.viewmodel.ext.android.viewModel

class MoviesListActivity : AppCompatActivity(), InfiniteScrollManager.OnScrollMore {

    private val viewModel: MoviesListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        applyDataBinding()
        viewModel.getPopularMovies()

        scrollMoreButton.setOnClickListener {
            onScrollMorePages(1)
        }
    }

    private fun applyDataBinding() {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
            activityViewModel = viewModel
        }
    }

    override fun onScrollMorePages(page: Int) {
        viewModel.subject.onNext(Unit)
    }
}