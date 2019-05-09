package net.paulacr.movieslover.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Entity(tableName = "movie")
@Parcelize
data class Movie(

    @PrimaryKey
    @SerializedName("id") val id: Int,

    @SerializedName("title") val title: String,

    @SerializedName("genre_ids") val genresIds: @RawValue List<String>,

    @SerializedName("poster_path") val posterPath: String?,

    @SerializedName("release_date") val releaseDate: String,

    @SerializedName("popularity") val popularity: Double,

    @SerializedName("overview") val overview: String,

    var page: Int
) : Parcelable