package com.example.hamid.movies.presentation.di.module

import android.app.Application
import androidx.room.Room
import androidx.test.espresso.idling.CountingIdlingResource
import com.hamid.data.MovieRepositoryImpl
import com.hamid.data.local.db.MovieDaoImpl
import com.hamid.data.local.db.MovieRoomDatabase
import com.hamid.data.local.sharedPref.MovieSharedPreference
import com.hamid.data.model.MovieModelMapperImpl
import com.hamid.data.remote.APIService
import com.hamid.domain.model.repository.MovieRepository
import com.hamid.domain.model.usecases.MoviesUseCase
import com.hamid.domain.model.utils.Constants
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
    fun provideItemDao(@NonNull appDatabase: MovieRoomDatabase): MovieDaoImpl {
        return appDatabase.movieDao()
    }

    @Singleton
    @Provides
    fun provideMovieModelMapper() = MovieModelMapperImpl()

    @Provides
    @Singleton
    fun provideRepository(@NonNull apiService: APIService, @NonNull movieDaoImpl: MovieDaoImpl, @NonNull sharedPreference: MovieSharedPreference, @NonNull mapper: MovieModelMapperImpl): MovieRepository {
        return MovieRepositoryImpl(
            apiService,
            movieDaoImpl,
            sharedPreference,
            mapper
        )
    }

    @Provides
    @Singleton
    fun provideIdlingResource(): CountingIdlingResource {
        return CountingIdlingResource(Constants.idlingResourceName)
    }


    @Provides
    @Singleton
    fun provideMoviesUseCase(@NonNull movieRepository: MovieRepository) =
        MoviesUseCase(movieRepository)

}
