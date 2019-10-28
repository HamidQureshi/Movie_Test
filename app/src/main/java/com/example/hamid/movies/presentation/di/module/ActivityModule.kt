package com.example.hamid.movies.presentation.di.module

import com.example.hamid.movies.presentation.ui.activity.MovieActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeCaseActivity(): MovieActivity

}
