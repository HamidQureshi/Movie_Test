package com.hamid.domain.model.model

import io.uniflow.core.flow.UIState

sealed class MovieViewState : UIState() {

    object Init : MovieViewState()

    data class MovieFormatted(
        val response: Response
    ) : MovieViewState()

    data class Failed(val error: Exception) : MovieViewState()

}