package net.paulacr.movieslover.util

object PageUtil {

    const val INITIAL_PAGE = 1
    private var page = INITIAL_PAGE

    fun increasePageNumber() {
        page++
    }

    fun decreasePageNumber() {
        page--
    }

    fun resetPage() {
        page = 1
    }

    fun getCurrentPage(): Int {
        return page
    }
}