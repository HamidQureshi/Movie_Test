package com.hamid.data

import android.util.Log
import com.hamid.data.local.db.MovieDaoImpl
import com.hamid.data.local.sharedPref.MovieSharedPreference
import com.hamid.data.model.MovieModelMapperImpl
import com.hamid.data.model.MovieResponse
import com.hamid.data.remote.APIService
import com.hamid.data.utils.EspressoIdlingResource
import com.hamid.domain.model.model.Response
import com.hamid.domain.model.repository.MovieRepository
import com.hamid.domain.model.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepositoryImpl constructor(
    private val apiService: APIService,
    private val movieDAOImpl: MovieDaoImpl,
    private val sharedPreference: MovieSharedPreference,
    private val mapper: MovieModelMapperImpl
) : MovieRepository {


    override suspend fun getMoviesFromDb(): Flow<Response> {
        return movieDAOImpl.getAllMovies()
            .map { mapper.fromEntity(it) }
    }

    override suspend fun getMoviesFromServer() {
        EspressoIdlingResource.increment()

        val moviesResponse = apiService.fetchMovies(Constants.apiKey, currentPageNumber())

        try {
            if (moviesResponse.isSuccessful) {
                insertMovieListToDB(moviesResponse.body()!!.results)
                incrementPageNumber()
            } else {
                Log.e("Error: ", "${moviesResponse.code()}")
            }
        } catch (e: Throwable) {
            Log.e("error", e.message + "")

        }
        EspressoIdlingResource.decrement()

    }

    suspend fun insertMovieListToDB(movies: List<MovieResponse>) = movieDAOImpl.insertAll(movies)

    override suspend fun updateFavouriteMovie(movieID: Int, favourite: Boolean) =
        movieDAOImpl.updateFavouriteMovie(movieID, favourite)

    override suspend fun nukeDB() = movieDAOImpl.deleteAll()

    override suspend fun currentPageNumber(): Int {
        return sharedPreference.getPageNumber()
    }

    override suspend fun incrementPageNumber() = sharedPreference.incrementPageNumber()

}




