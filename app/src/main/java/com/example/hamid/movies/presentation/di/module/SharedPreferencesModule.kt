package com.example.hamid.movies.presentation.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.hamid.movies.data.local.sharedPref.MovieSharedPreference
import com.example.hamid.movies.utils.Constants
import dagger.Module
import dagger.Provides
import io.reactivex.annotations.NonNull
import javax.inject.Singleton

@Module
class SharedPreferencesModule {

    @Provides
    fun provideSharedPreferences(@NonNull context: Application): SharedPreferences {
        return context.getSharedPreferences(Constants.sharedPrefName, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideMovieSharedPreferences(sharedPreference: SharedPreferences): MovieSharedPreference {
        return MovieSharedPreference(sharedPreference)
    }
}