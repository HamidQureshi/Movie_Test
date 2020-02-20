package com.example.hamid.movies.presentation.di.koin

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.hamid.data.local.sharedPref.MovieSharedPreference
import com.hamid.domain.model.utils.Constants
import io.reactivex.annotations.NonNull
import org.koin.dsl.module

val prefModule = module {

    single<SharedPreferences> {
        provideSharedPreferences(get())
    }

    single<MovieSharedPreference> {
        provideMovieSharedPreferences(get())
    }

}

fun provideSharedPreferences(@NonNull context: Application): SharedPreferences {
    return context.getSharedPreferences(Constants.sharedPrefName, Context.MODE_PRIVATE)
}

fun provideMovieSharedPreferences(sharedPreference: SharedPreferences): MovieSharedPreference {
    return MovieSharedPreference(sharedPreference)
}