package net.paulacr.movieslover.util

object PageUtil {

    private var page = 1

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