package com.example.hamid.movies.presentation.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hamid.domain.model.model.Response
import com.hamid.domain.model.usecases.MoviesUseCase
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MovieViewModel @Inject
constructor(
    val moviesUseCase: MoviesUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "viewModel"
    }

    val formattedMovieList = MutableLiveData<Response>()
    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun getData() {

        compositeDisposable.add(
            moviesUseCase.getMoviesFromDB()
                .subscribe({ response ->
                    Log.d(TAG, "On Next Called")
                    if (response.data.isEmpty()) {
                        moviesUseCase.getMoviesFromServer()
                    }
                    formattedMovieList.postValue(response)
                }, { error ->
                    Log.d(TAG, "On Error Called $error")
                    moviesUseCase.getMoviesFromServer()
                }, {
                    Log.d(TAG, "On Complete Called")
                })
        )

    }

    fun getMoviesFromServer() =
        moviesUseCase.getMoviesFromServer()

    fun updateFavouriteMovie(movieID: Int, favourite: Boolean) =
        moviesUseCase.markMovieFavourite(movieID, favourite)

    public override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        moviesUseCase.clearDisposable()
    }

}
