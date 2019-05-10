package net.paulacr.movieslover.ui.movieslist

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import kotlinx.android.synthetic.main.activity_main.*
import net.paulacr.movieslover.R
import net.paulacr.movieslover.data.model.MovieWithGenres
import net.paulacr.movieslover.databinding.ActivityMainBinding
import net.paulacr.movieslover.ui.moviedetail.MovieDetailActivity
import net.paulacr.movieslover.ui.moviedetail.MovieListener
import net.paulacr.movieslover.util.InfiniteScrollManager
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class MoviesListActivity : AppCompatActivity(), InfiniteScrollManager.OnScrollMore, MovieListener {

    private val viewModel: MoviesListViewModel by viewModel()
    private var adapter: MoviesListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        applyDataBinding()
        viewModel.getPopularMovies()

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

    override fun onScrollMorePages(page: Int) {
        viewModel.subject.onNext(Unit)
    }

    override fun onItemClick(position: Int, movieWithGenres: MovieWithGenres) {
        startActivity(MovieDetailActivity.newIntent(this, movieWithGenres))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        // Set up the query listener that executes the search
        val disposable = Observable.create(ObservableOnSubscribe<String> { subscriber ->
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    subscriber.onNext(newText!!)
                    return false
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    subscriber.onNext(query!!)
                    return false
                }
            })
        }).map { text -> text.toLowerCase().trim() }
            .debounce(400, TimeUnit.MILLISECONDS)
            .subscribe { text ->
                viewModel.searchMovies(text)
                Log.d("Search", "subscriber: $text")
            }

        return super.onCreateOptionsMenu(menu)
    }
}