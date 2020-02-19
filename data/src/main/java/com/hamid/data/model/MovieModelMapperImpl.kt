package com.hamid.data.model

import com.hamid.domain.model.model.MovieFormatted
import com.hamid.domain.model.model.Response
import com.hamid.domain.model.model.Status
import com.hamid.domain.model.repository.MovieModelMapper
import com.hamid.domain.model.utils.Constants

/**
 * Maps between Room database entity and model.
 */
class MovieModelMapperImpl : MovieModelMapper<List<MovieResponse>, Response> {
    override fun fromEntity(from: List<MovieResponse>) = convertResponseForUI(formatMovies(from))

    private fun formatMovies(movieResponse: List<MovieResponse>): List<MovieFormatted> {

//        withContext(Dispatchers.Default){

        val formattedMovie = ArrayList<MovieFormatted>()

        for (movie in movieResponse) {
            formattedMovie.add(
                MovieFormatted(
                    movie.movieId,
                    movie.title,
                    "${Constants.posterURL}${movie.posterPath}",
                    convertVoteToPercentage(movie.voteAverage),
                    movie.releaseDate,
                    movie.favourite
                )
            )
        }

        return formattedMovie

//        }

    }

    private fun convertVoteToPercentage(voteAverage: Float?): String {
        return "${voteAverage?.times(10)}%"
    }

    private fun convertResponseForUI(formattedMovieList: List<MovieFormatted>): Response {

        val response =
            Response(Status.SUCCESS, formattedMovieList)

        if (formattedMovieList.isEmpty()) {
            response.status = Status.ERROR
//            getMoviesFromServer()
        }

        return response
    }
}