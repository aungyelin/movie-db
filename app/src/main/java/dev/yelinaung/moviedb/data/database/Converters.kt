package dev.yelinaung.moviedb.data.database

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromGenreIds(genreIds: List<Int>?): String? {
        return genreIds?.joinToString(separator = ",")
    }

    @TypeConverter
    fun toGenreIds(data: String?): List<Int>? {
        return data?.split(",")?.mapNotNull { it.toIntOrNull() }
    }

}
