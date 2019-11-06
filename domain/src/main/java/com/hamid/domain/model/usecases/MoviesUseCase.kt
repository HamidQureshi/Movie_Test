package com.hamid.domain.model.usecases

import com.hamid.domain.model.model.Response
import com.hamid.domain.model.repository.MovieRepository
import io.reactivex.Flowable

class MoviesUseCase(private val repository: MovieRepository) {

    fun getMoviesFromDB(): Flowable<Response> = repository.getMoviesFromDb()

    fun getMoviesFromServer() = repository.getMoviesFromServer()

    fun markMovieFavourite(movieID: Int, favourite: Boolean) = repository.updateFavouriteMovie(movieID, favourite)

    fun nukeDB() = repository.nukeDB()

    fun getcurrentPageNumber():Int = repository.currentPageNumber()

    fun incrementPageNumber() = repository.incrementPageNumber()

    fun clearDisposable() = repository.clearDisposable()

}