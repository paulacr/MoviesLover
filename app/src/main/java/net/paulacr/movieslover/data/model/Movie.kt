package net.paulacr.movieslover.data.model

import android.arch.persistence.room.Entity
import android.support.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
@Entity(tableName = "movie")
data class Movie(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("genre_ids") val genresIds: List<String>
)