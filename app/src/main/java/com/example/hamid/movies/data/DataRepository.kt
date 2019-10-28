package com.example.hamid.movies.data

import com.example.hamid.movies.data.local.db.MovieDao
import com.example.hamid.movies.data.local.sharedPref.MovieSharedPreference
import com.example.hamid.movies.data.model.MovieResponse
import com.example.hamid.movies.data.model.RemoteApiResponse
import com.example.hamid.movies.data.remote.APIService
import com.example.hamid.movies.utils.Constants
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor(
    private val apiService: APIService,
    private val movieDAO: MovieDao,
    private val sharedPreference: MovieSharedPreference
) {

    var movieList = movieDAO.getAllMovies()

    fun getMoviesFromServer(): Single<RemoteApiResponse> {
        return apiService.fetchMovies(Constants.apiKey, currentPageNumber())
    }

    fun insertMovieListToDB(movies: List<MovieResponse>) = movieDAO.insertAll(movies)


    fun updateFavouriteMovie(movieID: Int, favourite: Boolean) = movieDAO.updateFavouriteMovie(movieID, favourite)


    fun nukeDB() = movieDAO.deleteAll()

    fun currentPageNumber(): Int {
        return sharedPreference.getPageNumber()
    }

    fun incrementPageNumber() = sharedPreference.incrementPageNumber()

}




