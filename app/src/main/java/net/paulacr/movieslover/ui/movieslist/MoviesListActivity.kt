package net.paulacr.movieslover.ui.movieslist

import android.content.Context
import android.databinding.DataBindingUtil
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.View
import android.widget.SearchView
import kotlinx.android.synthetic.main.activity_main.*
import net.paulacr.movieslover.R
import net.paulacr.movieslover.data.model.MovieWithGenres
import net.paulacr.movieslover.databinding.ActivityMainBinding
import net.paulacr.movieslover.ui.moviedetail.MovieDetailActivity
import net.paulacr.movieslover.ui.moviedetail.MovieListener
import net.paulacr.movieslover.util.InfiniteScrollManager
import org.koin.android.viewmodel.ext.android.viewModel

class MoviesListActivity : AppCompatActivity(), InfiniteScrollManager.OnScrollMore, MovieListener {

    private val viewModel: MoviesListViewModel by viewModel()
    private var adapter: MoviesListAdapter? = null
    private var searchTypeIsActive = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        applyDataBinding()
        setupPullToRefresh()
        observeActions()

        if (isInternetAvailable()) {
            viewModel.getPopularMovies()
        } else {
            showRetryButton()
        }
    }

    private fun observeActions() {
        viewModel.moviesAction.observe(this) {
            if (adapter == null) {
                setupRecyclerView(it)
            } else {
                addMoreMovies(it)
            }
        }

        viewModel.searchAction.observe(this) {
            adapter?.showItemsBySearch(it)
        }
    }

    private fun setupRecyclerView(items: List<MovieWithGenres>) {
        adapter = MoviesListAdapter(items, this)
        rvMoviesList.adapter = adapter

        val manager = LinearLayoutManager(this)
        rvMoviesList.layoutManager = manager

        val inifiniteScroll = InfiniteScrollManager(manager)
        inifiniteScroll.setListenerScroll(this)
        rvMoviesList.addOnScrollListener(inifiniteScroll)
    }

    private fun addMoreMovies(items: List<MovieWithGenres>) {
        adapter?.updateItems(items)
    }

    private fun applyDataBinding() {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
            activityViewModel = viewModel
        }
    }

    private fun setupPullToRefresh() {
        refresh.setOnRefreshListener {
            adapter = null
            refresh.isRefreshing = false
        }
    }

    override fun onScrollMorePages(page: Int) {
        if (searchTypeIsActive) {
            viewModel.searchMoreMovies()
        } else {
            viewModel.getMoreMovies()
        }
    }

    override fun onItemClick(position: Int, movieWithGenres: MovieWithGenres) {
        startActivity(MovieDetailActivity.newIntent(this, movieWithGenres))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        // Set up the query listener that executes the search
        viewModel.subjectTypeSearch
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.subjectTypeSearch.onNext(newText!!)
                    adapter?.clearList()
                    searchTypeIsActive = true
                    return false
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.subjectTypeSearch.onNext(query!!)
                    adapter?.clearList()
                    searchTypeIsActive = true
                    return false
                }
            })

        viewModel.subscribeTypeSearchSubject()
        return super.onCreateOptionsMenu(menu)
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected
    }

    private fun showRetryButton() {
        Snackbar.make(rvMoviesList, "Internet is out", Snackbar.LENGTH_INDEFINITE)
            .setAction("RETRY", View.OnClickListener {
                viewModel.getPopularMovies()
            })
            .show()
    }
}