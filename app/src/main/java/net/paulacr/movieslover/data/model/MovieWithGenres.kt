package net.paulacr.movieslover.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import net.paulacr.movieslover.BuildConfig

@Parcelize
data class MovieWithGenres(val movie: Movie, val genres: List<Genre>) : Parcelable {

    val posterPathUrl: String? = BuildConfig.BASE_IMGAGE_URL.plus(movie.posterPath)

    fun genresList(): String {
        return "genres: ".plus(genres.map { it.name }.joinToString(separator = ";"))
    }
}