package dev.yelinaung.moviedb.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.yelinaung.moviedb.data.service.model.movie_list.Movie

@Entity(tableName = "favorite_movies")
data class MovieEntity(
    @PrimaryKey
    val id: Int,
    val adult: Boolean?,
    val backdropPath: String?,
    val genreIds: String?, // Convert List<Int> to String for easier storage
    val originalLanguage: String?,
    val originalTitle: String?,
    val overview: String?,
    val popularity: Double?,
    val posterPath: String?,
    val releaseDate: String?,
    val title: String?,
    val video: Boolean?,
    val voteAverage: Double?,
    val voteCount: Int?
)

fun MovieEntity.mapToMovie(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        overview = this.overview,
        popularity = this.popularity,
        releaseDate = this.releaseDate,
        backdropPath = this.backdropPath,
        adult = this.adult,
        genreIds = this.genreIds?.split(",")?.mapNotNull { it.toIntOrNull() }, // Convert back to List<Int>
        originalLanguage = this.originalLanguage,
        originalTitle = this.originalTitle,
        posterPath = this.posterPath,
        video = this.video,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount
    )
}
