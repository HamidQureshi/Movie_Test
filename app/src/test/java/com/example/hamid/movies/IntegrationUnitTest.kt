package com.example.hamid.movies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.hamid.movies.data.DataRepository
import com.example.hamid.movies.data.local.db.MovieDao
import com.example.hamid.movies.data.local.sharedPref.MovieSharedPreference
import com.example.hamid.movies.data.remote.APIService
import com.example.hamid.movies.domain.MovieProcessor
import com.example.hamid.movies.presentation.ViewModelTest
import com.example.hamid.movies.presentation.ui.viewmodel.MovieViewModel
import com.example.hamid.movies.utils.helper.MockResponse
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
    private var movieDAO: MovieDao = mock()
    private var sharedPreference: MovieSharedPreference = mock()

    private lateinit var dataRepo: DataRepository
    private lateinit var movieProcessor: MovieProcessor
    private lateinit var viewModel: MovieViewModel

    @Before
    @Throws(Exception::class)
    fun setUp() {

        `when`(
            apiService.fetchMovies(
                any(),
                any()
            )
        ).thenReturn(Single.just(MockResponse.responsePage1))

        `when`(
            movieDAO.getAllMovies()
        ).thenReturn(Flowable.just(MockResponse.movieResponseList))

        dataRepo = DataRepository(apiService, movieDAO, sharedPreference)
        movieProcessor = MovieProcessor(dataRepo)
        viewModel = MovieViewModel(movieProcessor)

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
        }
    }

    @Test
    fun verifyDBAndLDSameSize() {

        val dbData = movieDAO.getAllMovies()
            .test()
            .values()

        viewModel.formattedMovieList.observeForTesting {
            assertEquals(viewModel.formattedMovieList.value!!.data.size, dbData[0].size)
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