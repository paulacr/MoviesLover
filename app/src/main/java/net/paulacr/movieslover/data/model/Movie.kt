package net.paulacr.movieslover.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.RawValue

@Entity(tableName = "movie")
data class Movie(

    @PrimaryKey
    @SerializedName("id") val id: Int,

    @SerializedName("title") val title: String,

    @SerializedName("genre_ids") val genresIds: @RawValue List<String>,

    val page: Int
)