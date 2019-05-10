package net.paulacr.movieslover.data

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.paulacr.movieslover.data.model.Genre

class Converter {

    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType = object : TypeToken<ArrayList<String>>() {
        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: List<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun stringToList(value: String?): List<Genre> {
        val listType = object : TypeToken<List<Genre>>() {
        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromObjectList(list: List<Genre>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}