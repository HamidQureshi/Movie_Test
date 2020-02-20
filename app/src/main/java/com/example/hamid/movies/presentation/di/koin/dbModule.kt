package com.example.hamid.movies.presentation.di.koin

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
import io.reactivex.annotations.NonNull
import org.koin.dsl.module

val dbModule = module {

    single<MovieRoomDatabase> {
        provideDatabase(get())
    }

    single<MovieDaoImpl> {
        provideItemDao(get())
    }

    single<MovieModelMapperImpl> {
        provideMovieModelMapper()
    }

    single<MovieRepository> {
        provideRepository(get(), get(), get(), get())
    }

    single<CountingIdlingResource> {
        provideIdlingResource()
    }

    single<MoviesUseCase> {
        provideMoviesUseCase(get())
        provideMoviesUseCase(get())
    }

}

fun provideDatabase(@NonNull application: Application): MovieRoomDatabase {
    return Room.databaseBuilder(
        application,
        MovieRoomDatabase::class.java,
        Constants.dbName
    )
        .allowMainThreadQueries().build()
}

fun provideItemDao(@NonNull appDatabase: MovieRoomDatabase): MovieDaoImpl {
    return appDatabase.movieDao()
}

fun provideMovieModelMapper() = MovieModelMapperImpl()

fun provideRepository(@NonNull apiService: APIService, @NonNull movieDaoImpl: MovieDaoImpl, @NonNull sharedPreference: MovieSharedPreference, @NonNull mapper: MovieModelMapperImpl): MovieRepository {
    return MovieRepositoryImpl(
        apiService,
        movieDaoImpl,
        sharedPreference,
        mapper
    )
}

fun provideIdlingResource(): CountingIdlingResource {
    return CountingIdlingResource(Constants.idlingResourceName)
}

fun provideMoviesUseCase(@NonNull movieRepository: MovieRepository) =
    MoviesUseCase(movieRepository)