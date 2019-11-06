package com.example.hamid.movies.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.hamid.movies.presentation.ViewModelTest
import com.example.hamid.movies.utils.helper.MockResponse
import com.hamid.data.MovieRepositoryImpl
import com.hamid.data.local.db.MovieDaoImpl
import com.hamid.data.local.sharedPref.MovieSharedPreference
import com.hamid.data.model.MovieResponse
import com.hamid.data.remote.APIService
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`

class RepositoryTest {

    @get:Rule
    val rxSchedulerRule = ViewModelTest.RxSchedulerRule()
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private var apiService: APIService = mock()
    private var movieDAOImpl: MovieDaoImpl = mock()
    private var sharedPreference: MovieSharedPreference = mock()

    private lateinit var movieRepoImpl: MovieRepositoryImpl

    private var pageNumber = 1

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
            movieDAOImpl.getAllMovies()
        ).thenReturn(Flowable.just(MockResponse.movieResponseList))

        `when`(
            sharedPreference.getPageNumber()
        ).thenReturn(pageNumber)

        movieRepoImpl =
            MovieRepositoryImpl(apiService, movieDAOImpl, sharedPreference)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
    }

    @Test
    fun getMoviesFromServer_apiCalled() {

        movieRepoImpl.getMoviesFromServer()

        verify(apiService, atLeastOnce()).fetchMovies(any(), any())
    }

    @Test
    fun insertMovieListToDB_insertToDbCall() {

        movieRepoImpl.insertMovieListToDB(MockResponse.movieResponseList)

        verify(movieDAOImpl, atLeastOnce()).insertAll(MockResponse.movieResponseList)
        verify(movieDAOImpl, atLeastOnce()).getAllMovies()
    }

    @Test
    fun updateFavouriteMovie_updateDbCalled() {

        movieRepoImpl.updateFavouriteMovie(any(), any())

        verify(movieDAOImpl, atLeastOnce()).updateFavouriteMovie(any(), any())
        verify(movieDAOImpl, atLeastOnce()).getAllMovies()
    }

    @Test
    fun currentPageNumber_returnCurrentPageInPref() {

        val pageNumber = movieRepoImpl.currentPageNumber()

        assert(pageNumber > 0)

        verify(sharedPreference, atLeastOnce()).getPageNumber()
    }

    @Test
    fun incrementPageNumber_incrementPageNumberInPref() {

        movieRepoImpl.incrementPageNumber()

        verify(sharedPreference, atLeastOnce()).incrementPageNumber()
    }

    @Test
    fun getMoviesFromServer_returnsMovies() {
        val expectedValue = MockResponse.responsePage1

        val actualValue = movieRepoImpl.getMoviesFromServer()
            .test()
            .values()

        assertEquals(expectedValue, actualValue[0])
    }

    @Test
    fun getAllMovies_containsList() {
        val expectedValue = MockResponse.movieResponseList

        val actualValue = movieRepoImpl.movieList
            .test()
            .values()

        assertEquals(expectedValue, actualValue[0])

    }

    @Test
    fun getAllMovies_containsEmptyList() {
        val expectedValue = emptyList<MovieResponse>()

        `when`(movieDAOImpl.getAllMovies()).thenReturn(Flowable.just(expectedValue))

        movieRepoImpl =
            MovieRepositoryImpl(apiService, movieDAOImpl, sharedPreference)

        val actualValue = movieRepoImpl.movieList
            .test()
            .values()

        assertEquals(expectedValue, actualValue[0])
    }

    @Test
    fun verifySizeOfApiResponseAndDbSame() {

        val apiData = apiService.fetchMovies(Constants.apiKey, pageNumber)
            .test()
            .values()

        val dbData = movieDAOImpl.getAllMovies()
            .test()
            .values()

        assertEquals(apiData[0].results.size, dbData[0].size)
    }

    @Test
    fun verifyApiResponseAndDbSame() {

        val apiData = apiService.fetchMovies(Constants.apiKey, pageNumber)
            .test()
            .values()

        val dbData = movieDAOImpl.getAllMovies()
            .test()
            .values()

        assertEquals(apiData[0].results, dbData[0])
    }

    @Test
    fun getDataFromServerAndDBDataSame() {

        val actualValue = movieRepoImpl.movieList
            .test()
            .values()

        assertEquals(MockResponse.responsePage1.results, actualValue[0])

    }

}