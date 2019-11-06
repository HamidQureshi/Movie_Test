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
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val apiService: APIService,
    private val movieDAOImpl: MovieDaoImpl,
    private val sharedPreference: MovieSharedPreference,
    private val mapper: MovieModelMapperImpl
): MovieRepository {

    private var disposable = CompositeDisposable()


    override fun getMoviesFromDb(): Flowable<Response> {
        return movieDAOImpl.getAllMovies()
            .map { mapper.fromEntity(it) }
    }

    override fun getMoviesFromServer() {
        EspressoIdlingResource.increment()

        disposable.add(
            apiService.fetchMovies(Constants.apiKey, currentPageNumber())
                .map { it.results }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(object : DisposableSingleObserver<List<MovieResponse>>() {

                    override fun onError(e: Throwable) {
                        Log.e("error", e.message + "")
                        EspressoIdlingResource.decrement()
                    }

                    override fun onSuccess(response: List<MovieResponse>) {
                        insertMovieListToDB(response)
                        incrementPageNumber()
                        EspressoIdlingResource.decrement()
                    }
                })
        )

    }

    fun insertMovieListToDB(movies: List<MovieResponse>) = movieDAOImpl.insertAll(movies)

    override fun updateFavouriteMovie(movieID: Int, favourite: Boolean) =
        movieDAOImpl.updateFavouriteMovie(movieID, favourite)

    override fun nukeDB() = movieDAOImpl.deleteAll()

    override fun currentPageNumber(): Int {
        return sharedPreference.getPageNumber()
    }

    override fun incrementPageNumber() = sharedPreference.incrementPageNumber()

    override fun clearDisposable() =
        disposable.clear()

}




