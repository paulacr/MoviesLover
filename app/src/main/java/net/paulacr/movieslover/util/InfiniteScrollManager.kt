package net.paulacr.movieslover.util

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class InfiniteScrollManager(private val linearLayoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    private var previousTotal = 0
    private var loading = true
    private var currentPage = 1
    private lateinit var listener: OnScrollMore

    interface OnScrollMore {
        fun onScrollMorePages(page: Int)
    }

    fun setListenerScroll(instance: OnScrollMore) {
        this.listener = instance
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        val visibleItemCount = recyclerView.childCount ?: 0
        val totalItemCount = linearLayoutManager.itemCount
        val firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition()

        if (totalItemCount > previousTotal) {
            loading = false
            previousTotal = totalItemCount
        }

        val visibleThreshold = 5
        if (!loading && (totalItemCount - visibleItemCount)
            <= (firstVisibleItem + visibleThreshold)) {
            currentPage++
            listener.onScrollMorePages(currentPage)
            loading = true
        }
    }
}