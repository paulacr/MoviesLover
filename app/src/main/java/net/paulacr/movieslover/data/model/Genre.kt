package net.paulacr.movieslover.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "genre")
@Parcelize
data class Genre(

    @PrimaryKey
    @SerializedName("id") val id: Int,

    @SerializedName("name") val name: String
) : Parcelable