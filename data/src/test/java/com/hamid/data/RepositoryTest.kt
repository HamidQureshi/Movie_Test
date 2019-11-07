package com.hamid.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hamid.data.local.db.MovieDaoImpl
import com.hamid.data.local.sharedPref.MovieSharedPreference
import com.hamid.data.model.MovieModelMapperImpl
import com.hamid.data.model.MovieResponse
import com.hamid.data.remote.APIService
import com.hamid.data.utils.helper.MockRepoResponse
import com.hamid.domain.model.utils.Constants
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.mockito.Mockito.`when`


class RepositoryTest {

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private var apiService: APIService = mock()
    private var movieDAOImpl: MovieDaoImpl =
        mock()
    private var sharedPreference: MovieSharedPreference =
        mock()
    private var mapper = MovieModelMapperImpl()

    private lateinit var movieRepoImpl: MovieRepositoryImpl

    private var pageNumber = 1

    @Before
    fun setUp() {

        `when`(
            apiService.fetchMovies(
                any(),
                any()
            )
        )
            .thenReturn(Single.just(MockRepoResponse.responsePage1))

        `when`(
            movieDAOImpl.getAllMovies()
        )
            .thenReturn(Flowable.just(MockRepoResponse.movieResponseList))

        `when`(
            sharedPreference.getPageNumber()
        ).thenReturn(pageNumber)

        movieRepoImpl =
            MovieRepositoryImpl(apiService, movieDAOImpl, sharedPreference, mapper)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getMoviesFromServer_apiCalled() {

        movieRepoImpl.getMoviesFromServer()

        verify(apiService, atLeastOnce())
            .fetchMovies(any(), any())
    }

    @Test
    fun insertMovieListToDB_insertToDbCall() {

        movieRepoImpl.insertMovieListToDB(MockRepoResponse.movieResponseList)

        verify(movieDAOImpl, atLeastOnce())
            .insertAll(MockRepoResponse.movieResponseList)
//        verify(movieDAOImpl, atLeastOnce()).getAllMovies()
    }

    @Test
    fun updateFavouriteMovie_updateDbCalled() {

        movieRepoImpl.updateFavouriteMovie(
            any(),
            any()
        )

        verify(movieDAOImpl, atLeastOnce()).updateFavouriteMovie(
            any(),
            any()
        )
//        verify(movieDAOImpl, atLeastOnce()).getAllMovies()
    }

    @Test
    fun currentPageNumber_returnCurrentPageInPref() {

        val pageNumber = movieRepoImpl.currentPageNumber()

        assert(pageNumber > 0)

        verify(
            sharedPreference,
            atLeastOnce()
        ).getPageNumber()
    }

    @Test
    fun incrementPageNumber_incrementPageNumberInPref() {

        movieRepoImpl.incrementPageNumber()

        verify(
            sharedPreference,
            atLeastOnce()
        ).incrementPageNumber()
    }

    @Test
    fun getMoviesFromServer_returnsMovies() {
        val expectedValue = MockRepoResponse.responsePage1

        val actualValue = apiService.fetchMovies("", 1)
            .test()
            .values()

        assertEquals(expectedValue, actualValue[0])
    }

    @Test
    fun getAllMovies_containsList() {
        val expectedValue = MockRepoResponse.movieResponseList

        val actualValue = movieRepoImpl.getMoviesFromDb()
            .test()
            .values()

        assertEquals(mapper.fromEntity(expectedValue).data, actualValue[0].data)

    }

    @Test
    fun getAllMovies_containsEmptyList() {
        val expectedValue = emptyList<MovieResponse>()

        `when`(movieDAOImpl.getAllMovies())
            .thenReturn(Flowable.just(expectedValue))

        movieRepoImpl =
            MovieRepositoryImpl(apiService, movieDAOImpl, sharedPreference, mapper)

        val actualValue = movieRepoImpl.getMoviesFromDb()
            .test()
            .values()

        assertEquals(expectedValue.size, actualValue[0].data.size)
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

        val actualValue = movieRepoImpl.getMoviesFromDb()
            .test()
            .values()

        assertEquals(
            mapper.fromEntity(MockRepoResponse.responsePage1.results).data,
            actualValue[0].data
        )

    }

    class RxSchedulerRule : TestRule {

        override fun apply(base: Statement, description: Description) =
            object : Statement() {
                override fun evaluate() {
                    RxAndroidPlugins.reset()
                    RxAndroidPlugins.setInitMainThreadSchedulerHandler { SCHEDULER_INSTANCE }

                    RxJavaPlugins.reset()
                    RxJavaPlugins.setIoSchedulerHandler { SCHEDULER_INSTANCE }
                    RxJavaPlugins.setNewThreadSchedulerHandler { SCHEDULER_INSTANCE }
                    RxJavaPlugins.setComputationSchedulerHandler { SCHEDULER_INSTANCE }

                    base.evaluate()
                }
            }

        companion object {
            private val SCHEDULER_INSTANCE = Schedulers.trampoline()
        }
    }

}