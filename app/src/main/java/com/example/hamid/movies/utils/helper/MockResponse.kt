package com.example.hamid.movies.utils.helper

import com.example.hamid.movies.data.model.MovieResponse
import com.example.hamid.movies.data.model.RemoteApiResponse
import com.example.hamid.movies.domain.model.MovieFormatted
import com.example.hamid.movies.domain.model.Response
import com.example.hamid.movies.domain.model.Status
import com.example.hamid.movies.utils.Constants

class MockResponse {
    companion object {

        private val respMovie1 = MovieResponse(
            1,
            475557,
            513.78F,
            4120,false,
            "/kvpNZAQow5es1tSY6XW2jAZuPPG.jpg",
            false,
            "/qAhedRxRYWZAgZ8O8pHIl6QHdD7.jpg",
            "en",
            "Fast & Furious Presents: Hobbs & Shaw",
            "Fast & Furious Presents: Hobbs & Shaw",
            6.5F,
            "Ever since US Diplomatic Security Service Agent Hob",
            "2019-08-02"
            )

        private val respMovie2 = MovieResponse(
            2,
            475547,
            513.78F,
            4420,false,
            "/udDclJoHjfjb8Ekgsd4FDteOkCU.jpg",
            false,
            "/n6bUvigpRFqSwmPp1m2YADdbRBc.jpg",
            "en",
            "Joker",
            "Joker",
            8.6F,
            "During the 1980s, a failed stand-up comedian is driven insane and turns",
            "2019-10-04"
        )

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

        val movieResponseList = listOf(respMovie1, respMovie2)
        private val formattedMovieList = listOf(formattedMovie1, formattedMovie2)

        val responsePage1 = RemoteApiResponse(1,10000,500, movieResponseList)

        val response_success = Response(Status.SUCCESS, formattedMovieList)
        val response_error = Response(Status.ERROR, emptyList())
    }
}