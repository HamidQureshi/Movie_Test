package com.example.hamid.movies.presentation.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamid.domain.model.model.Response
import com.hamid.domain.model.model.Status
import com.hamid.domain.model.usecases.MoviesUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

class MovieViewModel @Inject
constructor(
    val moviesUseCase: MoviesUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "viewModel"
    }

    val formattedMovieList = MutableLiveData<Response>()

    fun getData() = viewModelScope.launch {

        val response = moviesUseCase.getMoviesFromDB()

        try {
            response.collect {
                if (it.data.isEmpty()) {
                    moviesUseCase.getMoviesFromServer()
                }
                if (it.status == Status.SUCCESS) {
                    formattedMovieList.postValue(it)
                } else {
                    Log.e("Error: ", "${it.status}")
                    moviesUseCase.getMoviesFromServer()
                }
            }
        } catch (e: HttpException) {
            Log.e("Error: ", "${e.message}")
        } catch (e: Throwable) {
            Log.e("error", e.message + "")
        }

    }

    fun getMoviesFromServer() = viewModelScope.launch {
        moviesUseCase.getMoviesFromServer()
    }

    fun updateFavouriteMovie(movieID: Int, favourite: Boolean) = viewModelScope.launch {
        moviesUseCase.markMovieFavourite(movieID, favourite)
    }

    public override fun onCleared() {
        super.onCleared()
    }

}
