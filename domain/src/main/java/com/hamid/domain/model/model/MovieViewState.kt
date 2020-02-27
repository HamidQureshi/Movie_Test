package com.hamid.domain.model.model

import io.uniflow.core.flow.UIState

sealed class MovieViewState : UIState() {

    object Init : MovieViewState()

    data class Success(
        val movieList: List<MovieFormatted>
    ) : MovieViewState()

    data class Failed(val error: Exception) : MovieViewState()

}