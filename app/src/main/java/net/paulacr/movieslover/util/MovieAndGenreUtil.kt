package net.paulacr.movieslover.util

import net.paulacr.movieslover.data.model.Genre
import net.paulacr.movieslover.data.model.Movie
import net.paulacr.movieslover.data.model.MovieWithGenres

object MovieAndGenreUtil {

    /**
     * Combine Movies with Genres into a Wrapper
     *
     * @param movies
     * @param genres list with all genres
     * @return MovieWithGenre
     */
    fun moviesWithGenres(movies: List<Movie>, genres: List<Genre>): List<MovieWithGenres> {

        val moviesWithGenresList = mutableListOf<MovieWithGenres>()
        val listOfGenres = mutableListOf<Genre>()

        movies.forEach { movie ->

            listOfGenres.clear()
            for (id in movie.genresIds) {
                // Filter genres by movie genres ids
                val list = filterGenresById(genres, id)
                listOfGenres.addAll(list)
            }

            // encapsulate Genres list into a Wrapper that join Movie and Genres
            moviesWithGenresList.add(MovieWithGenres(movie, listOfGenres.toMutableList()))
        }
        return moviesWithGenresList
    }

    /**
     * Filter List of Genres by Movie genres list ids
     *
     * @param genres
     * @param id
     * @return
     */
    private fun filterGenresById(
        genres: List<Genre>,
        id: String
    ): MutableList<Genre> {
        val list = genres.filter {
            it.id.toString() == id
        }.toMutableList()
        return list
    }
}