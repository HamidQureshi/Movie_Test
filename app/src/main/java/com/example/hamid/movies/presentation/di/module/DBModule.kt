package com.example.hamid.movies.presentation.di.module

import android.app.Application
import androidx.room.Room
import androidx.test.espresso.idling.CountingIdlingResource
import com.example.hamid.movies.data.DataRepository
import com.example.hamid.movies.data.local.db.MovieDao
import com.example.hamid.movies.data.local.db.MovieRoomDatabase
import com.example.hamid.movies.data.local.sharedPref.MovieSharedPreference
import com.example.hamid.movies.data.remote.APIService
import com.example.hamid.movies.utils.Constants
import dagger.Module
import dagger.Provides
import io.reactivex.annotations.NonNull

import javax.inject.Singleton

@Module
class DBModule {


    @Provides
    @Singleton
    fun provideDatabase(@NonNull application: Application): MovieRoomDatabase {
        return Room.databaseBuilder(
            application,
            MovieRoomDatabase::class.java,
            Constants.dbName
        )
            .allowMainThreadQueries().build()
    }


    @Provides
    @Singleton
    fun provideItemDao(@NonNull appDatabase: MovieRoomDatabase): MovieDao {
        return appDatabase.movieDao()
    }

    @Provides
    @Singleton
    fun provideRepository(@NonNull apiService: APIService, @NonNull movieDao: MovieDao, @NonNull sharedPreference: MovieSharedPreference): DataRepository {
        return DataRepository(apiService, movieDao, sharedPreference)
    }

    @Provides
    @Singleton
    fun provideIdlingResource(): CountingIdlingResource {
        return CountingIdlingResource(Constants.idlingResourceName)
    }


}
