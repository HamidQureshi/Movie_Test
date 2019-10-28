package com.example.hamid.movies.domain

import android.util.Log
import com.example.hamid.movies.data.DataRepository
import com.example.hamid.movies.data.model.MovieResponse
import com.example.hamid.movies.domain.model.MovieFormatted
import com.example.hamid.movies.domain.model.Response
import com.example.hamid.movies.domain.model.Status
import com.example.hamid.movies.utils.Constants
import com.example.hamid.movies.utils.EspressoIdlingResource
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MovieProcessor @Inject constructor(val dataRepository: DataRepository) {

    private var disposable = CompositeDisposable()

    fun getData(): Flowable<Response> {
        return dataRepository.movieList
            .map { formatMovies(it) }
            .map { convertResponseForUI(it) }
    }

    private fun formatMovies(movieResponse: List<MovieResponse>): List<MovieFormatted> {

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
    }

    private fun convertVoteToPercentage(voteAverage: Float?): String {
        return "${voteAverage?.times(10)}%"
    }

    private fun convertResponseForUI(formattedMovieList: List<MovieFormatted>): Response {

        val response = Response(Status.SUCCESS, formattedMovieList)

        if (formattedMovieList.isEmpty()) {
            response.status = Status.ERROR
            getMoviesFromServer()
        }

        return response
    }

    fun getMoviesFromServer() {
        EspressoIdlingResource.increment()

        disposable.add(
            dataRepository.getMoviesFromServer()
                .map { it.results }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(object : DisposableSingleObserver<List<MovieResponse>>() {

                    override fun onError(e: Throwable) {
                        Log.e("error", e.message + "")
                        EspressoIdlingResource.decrement()
                    }

                    override fun onSuccess(response: List<MovieResponse>) {
                        dataRepository.insertMovieListToDB(response)
                        dataRepository.incrementPageNumber()
                        EspressoIdlingResource.decrement()
                    }
                })
        )

    }

    fun updateFavouriteMovie(movieID: Int, favourite: Boolean) =
        dataRepository.updateFavouriteMovie(movieID, favourite)

    fun clearDisposable() =
        disposable.clear()

}