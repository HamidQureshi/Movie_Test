package com.example.hamid.movies.presentation.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hamid.domain.model.model.MovieViewState
import com.hamid.domain.model.model.Response
import com.hamid.domain.model.usecases.MoviesUseCase
import io.uniflow.androidx.flow.AndroidDataFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MovieViewModel
constructor(
    val moviesUseCase: MoviesUseCase
) : AndroidDataFlow() {

    companion object {
        private const val TAG = "viewModel"
    }

    init {
        //set initial state
        setState { MovieViewState.Init }
    }

    val formattedMovieList = MutableLiveData<Response>()

    fun getData() {

        viewModelScope.launch {

            val response = moviesUseCase.getMoviesFromDB()

            response.collect { res ->
                if (res.data.isEmpty()) {
                    moviesUseCase.getMoviesFromServer()
                } else {
                    setState {
                        MovieViewState.MovieFormatted(res)
                    }
                }
            }
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
