package dev.yelinaung.moviedb.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

private const val DB_VERSION = 1

@Database(
    entities = [MovieEntity::class],
    version = DB_VERSION
)
@TypeConverters(Converters::class)
abstract class MovieDatabase: RoomDatabase() {

    abstract fun movieDao(): MovieDao

}
