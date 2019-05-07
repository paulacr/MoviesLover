package net.paulacr.movieslover.data.model

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("genre_ids") val genresIds: List<String>
)