package com.hamid.data.utils.helper

import com.hamid.data.model.MovieResponse
import com.hamid.data.model.RemoteApiResponse

class MockRepoResponse {
    companion object {

        private val respMovie1 = MovieResponse(
            1,
            475557,
            513.78F,
            4120, false,
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
            4420, false,
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

        val movieResponseList = listOf(respMovie1, respMovie2)

        val responsePage1 = RemoteApiResponse(1, 10000, 500, movieResponseList)

    }
}