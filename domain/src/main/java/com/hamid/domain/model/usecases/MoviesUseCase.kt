package com.hamid.domain.model.usecases

import com.hamid.domain.model.model.Response
import com.hamid.domain.model.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class MoviesUseCase(private val repository: MovieRepository) {

    suspend fun getMoviesFromDB(): Flow<Response> = repository.getMoviesFromDb()

    suspend fun getMoviesFromServer() = repository.getMoviesFromServer()

    suspend fun markMovieFavourite(movieID: Int, favourite: Boolean) =
        repository.updateFavouriteMovie(movieID, favourite)

    suspend fun nukeDB() = repository.nukeDB()

    suspend fun getcurrentPageNumber(): Int = repository.currentPageNumber()

    suspend fun incrementPageNumber() = repository.incrementPageNumber()

}