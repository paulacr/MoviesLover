package net.paulacr.movieslover.data.model

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import net.paulacr.movieslover.BuildConfig

@Parcelize
data class MovieWithGenres(val movie: Movie, val genres: List<Genre>) : Parcelable {

    @IgnoredOnParcel
    val posterPathUrl: String? = BuildConfig.BASE_IMGAGE_URL.plus(movie.posterPath)

    @IgnoredOnParcel
    val backdropPath: String? = BuildConfig.BASE_IMGAGE_URL.plus(movie.backdropPath)

    fun genresList(): String {
        return "genres: ".plus(genres.map { it.name }.joinToString(separator = " ; "))
    }
}