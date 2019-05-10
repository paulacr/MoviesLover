package net.paulacr.movieslover.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "movie_detail")
@Parcelize
data class MovieDetail(

    @PrimaryKey
    @SerializedName("id")
    val id: Int,

    @SerializedName("backdrop_path")
    val backdropPath: String,

    @SerializedName("genres")
    val genres: List<Genre>,

    @SerializedName("homepage")
    val homepage: String,

    @SerializedName("original_language")
    val language: String,

    @SerializedName("overview")
    val overview: String,

    @SerializedName("release_date")
    val releaseDate: String,

    @SerializedName("runtime")
    val runtime: String,

    @SerializedName("title")
    val title: String
) : Parcelable