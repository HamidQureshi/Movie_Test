package com.hamid.domain.model.repository

import com.hamid.domain.model.model.Response
import io.reactivex.Flowable


/**
 * Repository interface to be implemented by Data layer.
 */
interface MovieRepository {

    fun getMoviesFromDb(): Flowable<Response>

    fun getMoviesFromServer()

    fun updateFavouriteMovie(movieID: Int, favourite: Boolean)

    fun nukeDB()

    fun currentPageNumber(): Int

    fun incrementPageNumber()

    fun clearDisposable()

}