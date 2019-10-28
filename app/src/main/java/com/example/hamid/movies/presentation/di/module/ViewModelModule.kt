package com.example.hamid.movies.presentation.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hamid.movies.presentation.factory.ViewModelFactory
import com.example.hamid.movies.presentation.ui.viewmodel.MovieViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory


    @Binds
    @IntoMap
    @ViewModelKey(MovieViewModel::class)
    protected abstract fun viewModel(viewModel: MovieViewModel): ViewModel

}
