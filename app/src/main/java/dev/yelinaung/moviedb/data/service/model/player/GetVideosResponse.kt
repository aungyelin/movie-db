package dev.yelinaung.moviedb.data.service.model.player

data class GetVideosResponse(
    val id: Int,
    val results: List<Video>
)