package com.example.hamid.movies.presentation.di.koin

import com.example.hamid.movies.presentation.ui.viewmodel.MovieViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        MovieViewModel(get())
    }


}
