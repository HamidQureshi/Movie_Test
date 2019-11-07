package com.example.hamid.movies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.hamid.movies.presentation.ui.viewmodel.MovieViewModel
import com.hamid.data.MovieRepositoryImpl
import com.hamid.data.local.db.MovieDaoImpl
import com.hamid.data.local.sharedPref.MovieSharedPreference
import com.hamid.data.model.MovieModelMapperImpl
import com.hamid.data.remote.APIService
import com.hamid.data.utils.helper.MockRepoResponse
import com.hamid.domain.model.usecases.MoviesUseCase
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`

class IntegrationUnitTest {

    @get:Rule
    val rxSchedulerRule = ViewModelTest.RxSchedulerRule()
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private var apiService: APIService = mock()
    private var movieDAOImpl: MovieDaoImpl = mock()
    private var sharedPreference: MovieSharedPreference = mock()
    private var mapper = MovieModelMapperImpl()

    private lateinit var movieRepoImpl: MovieRepositoryImpl
    private lateinit var movieUseCase: MoviesUseCase
    private lateinit var viewModel: MovieViewModel

    @Before
    @Throws(Exception::class)
    fun setUp() {

        `when`(
            apiService.fetchMovies(
                any(),
                any()
            )
        ).thenReturn(Single.just(MockRepoResponse.responsePage1))

        `when`(
            movieDAOImpl.getAllMovies()
        ).thenReturn(Flowable.just(MockRepoResponse.movieResponseList))

        movieRepoImpl =
            MovieRepositoryImpl(apiService, movieDAOImpl, sharedPreference, mapper)
        movieUseCase = MoviesUseCase(movieRepoImpl)
        viewModel = MovieViewModel(movieUseCase)

        viewModel.getData()

    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
    }

    @Test
    fun verifyApiResponseAndLDSameSize() {

        val apiData = apiService.fetchMovies("", 1)
            .test()
            .values()

        viewModel.formattedMovieList.observeForTesting {
            assertEquals(viewModel.formattedMovieList.value!!.data.size, apiData[0].results.size)
            assertEquals(
                viewModel.formattedMovieList.value!!.data,
                mapper.fromEntity(apiData[0].results).data
            )
        }
    }

    @Test
    fun verifyDBAndLDSameSize() {

        val dbData = movieDAOImpl.getAllMovies()
            .test()
            .values()

        viewModel.formattedMovieList.observeForTesting {
            assertEquals(viewModel.formattedMovieList.value!!.data.size, dbData[0].size)
            assertEquals(
                viewModel.formattedMovieList.value!!.data,
                mapper.fromEntity(dbData[0]).data
            )
        }
    }

    private fun <T> LiveData<T>.observeForTesting(block: () -> Unit) {
        val observer = Observer<T> { }
        try {
            observeForever(observer)
            block()
        } finally {
            removeObserver(observer)
        }
    }

}