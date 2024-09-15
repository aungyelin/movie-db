package dev.yelinaung.moviedb.data.service.model.movie_details

import com.google.gson.annotations.SerializedName

data class ProductionCompany(
    val id: Int,
    @SerializedName("logo_path")
    val logoPath: String,
    val name: String,
    @SerializedName("origin_country")
    val originCountry: String
)