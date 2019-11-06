package com.example.hamid.movies.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.hamid.movies.presentation.ViewModelTest
import com.example.hamid.movies.utils.helper.MockResponse
import com.hamid.data.MovieRepositoryImpl
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`

class MovieProcessorTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val rxSchedulerRule = ViewModelTest.RxSchedulerRule()

    private var movieRepoImpl: MovieRepositoryImpl = mock()
    private lateinit var movieProcessor: MovieProcessor

    @Before
    @Throws(Exception::class)
    fun setUp() {
        movieProcessor = MovieProcessor(movieRepoImpl)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
    }

    @Test
    fun getData_whenNoDataSaved() {

        `when`(
            movieRepoImpl.movieList
        ).thenReturn(Flowable.just(emptyList()))

        `when`(
            movieRepoImpl.getMoviesFromServer()
        ).thenReturn(Single.just(MockResponse.responsePage1))


        val expectedResponse = MockResponse.response_error

        val actualResponse = movieProcessor.getData()
            .test()
            .values()

        assertEquals(expectedResponse.status, actualResponse[0].status)
        assertEquals(expectedResponse.data, actualResponse[0].data)
    }

    @Test
    fun getData_whenDataSaved() {
        `when`(movieRepoImpl.movieList).thenReturn(Flowable.just(MockResponse.movieResponseList))

        var expectedResponse = MockResponse.response_success


        var actualResponse =
            movieProcessor.getData()
                .test()
                .values()

        assertEquals(expectedResponse.status, actualResponse[0].status)
        assertEquals(expectedResponse.data, actualResponse[0].data)
    }

    @Test
    fun getMovieListFromServer_callsDataRepo() {

        `when`(movieRepoImpl.getMoviesFromServer()).thenReturn(Single.just(MockResponse.responsePage1))

        movieProcessor.getMoviesFromServer()
        verify(movieRepoImpl, atLeast(1)).getMoviesFromServer()
    }

    @Test
    fun updateFavouriteMovie_callsUpdateFromRepository() {
        movieProcessor.updateFavouriteMovie(any(), any())
        verify(movieRepoImpl, only()).updateFavouriteMovie(any(), any())
    }

}


