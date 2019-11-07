package com.example.hamid.movies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.hamid.movies.presentation.ui.viewmodel.MovieViewModel
import com.hamid.data.MovieRepositoryImpl
import com.hamid.domain.model.model.Status
import com.hamid.domain.model.usecases.MoviesUseCase
import com.hamid.domain.model.utils.helper.MockResponse
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Flowable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.*
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.mockito.Mockito.`when`


class ViewModelTest {

    // RxSchedulerRule rule for making the RxJava to run synchronously in unit test
    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val repo: MovieRepositoryImpl = mock()

    private lateinit var movieUseCase: MoviesUseCase
    private lateinit var viewModel: MovieViewModel


    @Before
    @Throws(Exception::class)
    fun setUp() {

        `when`(
            repo.getMoviesFromDb()
        ).thenReturn(Flowable.just(MockResponse.response_success))

        movieUseCase = MoviesUseCase(repo)
        viewModel = MovieViewModel(movieUseCase)

        viewModel.getData()

    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
    }


    @Test
    fun getData_getsDataFromDomain() {
        verify(repo, only()).getMoviesFromDb()
    }

    @Test
    fun verifyLiveDataNotNull() {

        viewModel.formattedMovieList.observeForTesting {
            Assert.assertNotNull(viewModel.formattedMovieList)
        }
    }

    @Test
    fun verifyLiveData_StatusSuccess() {

        viewModel.formattedMovieList.observeForTesting {
            Assert.assertTrue(viewModel.formattedMovieList.value!!.status == Status.SUCCESS)
        }
    }

    @Test
    fun verifyLiveData_StatusError() {

        `when`(
            repo.getMoviesFromDb()
        ).thenReturn(Flowable.just(MockResponse.response_error))

        viewModel.getData()

        viewModel.formattedMovieList.observeForTesting {
            Assert.assertTrue(viewModel.formattedMovieList.value!!.status == Status.ERROR)
        }
    }

    @Test
    fun getMoviesFromServer_callsGetMoviesFromDomain() {
        viewModel.getMoviesFromServer()
        verify(repo, atLeastOnce()).getMoviesFromServer()
    }

    @Test
    fun updateFavouriteMovie_callsUpdateMovieFromDomain() {
        viewModel.updateFavouriteMovie(any(), any())
        verify(repo, atLeastOnce()).updateFavouriteMovie(any(), any())
    }

    @Test
    fun clearDisposable_callsClearDisposableFromDomain() {
        viewModel.onCleared()
        verify(repo, atLeastOnce()).clearDisposable()
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


