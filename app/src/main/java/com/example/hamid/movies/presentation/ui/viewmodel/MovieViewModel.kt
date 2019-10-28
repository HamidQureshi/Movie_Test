package com.example.hamid.movies.presentation.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hamid.movies.domain.MovieProcessor
import com.example.hamid.movies.domain.model.Response
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MovieViewModel @Inject
constructor(
    val movieProcessor: MovieProcessor
) : ViewModel() {

    companion object {
        private const val TAG = "viewModel"
    }

    val formattedMovieList = MutableLiveData<Response>()
    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun getData() {

        compositeDisposable.add( movieProcessor.getData()
            .subscribe({ response ->
                Log.d(TAG, "On Next Called")
                formattedMovieList.postValue(response)
            }, { error ->
                Log.d(TAG, "On Error Called $error")
            }, {
                Log.d(TAG, "On Complete Called")
            }))

    }

    fun getMoviesFromServer() =
        movieProcessor.getMoviesFromServer()

    fun updateFavouriteMovie(movieID: Int, favourite: Boolean) =
        movieProcessor.updateFavouriteMovie(movieID, favourite)

    public override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        movieProcessor.clearDisposable()
    }

}
