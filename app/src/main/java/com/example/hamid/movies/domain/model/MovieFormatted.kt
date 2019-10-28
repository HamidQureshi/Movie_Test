package com.example.hamid.movies.domain.model

data class MovieFormatted(
    val id: Int,
    val title: String?,
    val posterPath: String?,
    val voteAverage: String?,
    val releaseDate: String?,
    val favourite: Boolean
)