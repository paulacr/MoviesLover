package net.paulacr.movieslover.data.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import android.support.annotation.Keep
import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Entity(tableName = "moviesresult")
@Parcelize
data class MoviesResult @JvmOverloads constructor(

    @NonNull @PrimaryKey(autoGenerate = false)
    val id: Int?,

    @ColumnInfo(name = "page")
    @SerializedName("page")
    val page: String
) : Parcelable