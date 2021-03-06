package com.hamid.domain.model.model

data class MovieFormatted(
    val id: Int,
    val title: String?,
    val posterPath: String?,
    val voteAverage: String?,
    val releaseDate: String?,
    val favourite: Boolean
)