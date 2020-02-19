package com.hamid.domain.model.repository

import com.hamid.domain.model.model.Response
import kotlinx.coroutines.flow.Flow


/**
 * Repository interface to be implemented by Data layer.
 */
interface MovieRepository {

    suspend fun getMoviesFromDb(): Flow<Response>

    suspend fun getMoviesFromServer()

    suspend fun updateFavouriteMovie(movieID: Int, favourite: Boolean)

    suspend fun nukeDB()

    suspend fun currentPageNumber(): Int

    suspend fun incrementPageNumber()

}