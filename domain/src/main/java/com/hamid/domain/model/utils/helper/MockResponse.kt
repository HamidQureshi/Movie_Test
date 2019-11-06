package com.hamid.domain.model.utils.helper

import com.hamid.domain.model.model.MovieFormatted
import com.hamid.domain.model.model.Response
import com.hamid.domain.model.model.Status
import com.hamid.domain.model.utils.Constants

class MockResponse {
    companion object {

        private val formattedMovie1 = MovieFormatted(
            475557,
            "Fast & Furious Presents: Hobbs & Shaw",
            "${Constants.posterURL}/kvpNZAQow5es1tSY6XW2jAZuPPG.jpg",
            "65.0%",
            "2019-08-02",
            false
        )

        private val formattedMovie2 = MovieFormatted(
            475547,
            "Joker",
            "${Constants.posterURL}/udDclJoHjfjb8Ekgsd4FDteOkCU.jpg",
            "86.0%",
            "2019-10-04",
            false
        )

        private val formattedMovieList = listOf(formattedMovie1, formattedMovie2)

        val response_success = Response(Status.SUCCESS, formattedMovieList)
        val response_error = Response(Status.ERROR, emptyList())
    }
}