package com.example.hamid.movies.presentation.di

import android.app.Application
import com.example.hamid.movies.presentation.di.koin.dbModule
import com.example.hamid.movies.presentation.di.koin.httpModule
import com.example.hamid.movies.presentation.di.koin.prefModule
import com.example.hamid.movies.presentation.di.koin.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AppController : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Use Koin Android Logger
            androidLogger()
            // declare Android context
            androidContext(this@AppController)
            // declare modules to use
            modules(listOf(viewModelModule, dbModule, httpModule, prefModule))
        }

    }
}