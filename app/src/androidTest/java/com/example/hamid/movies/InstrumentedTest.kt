package com.example.hamid.movies

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.espresso.IdlingRegistry
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.hamid.movies.presentation.di.module.HttpClientModule
import com.example.hamid.movies.presentation.ui.activity.MovieActivity
import com.example.hamid.movies.presentation.ui.viewmodel.MovieViewModel
import com.hamid.data.MovieRepositoryImpl
import com.hamid.data.local.db.MovieDaoImpl
import com.hamid.data.local.db.MovieRoomDatabase
import com.hamid.data.local.sharedPref.MovieSharedPreference
import com.hamid.data.model.MovieModelMapperImpl
import com.hamid.data.remote.APIService
import com.hamid.data.utils.EspressoIdlingResource
import com.hamid.domain.model.model.Status
import com.hamid.domain.model.usecases.MoviesUseCase
import com.hamid.domain.model.utils.Constants
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.*
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class InstrumentedTest {

    private lateinit var apiService: APIService
    private lateinit var db: MovieRoomDatabase
    private lateinit var sharedPreference: MovieSharedPreference
    private lateinit var movieDaoImpl: MovieDaoImpl
    private val mapper = MovieModelMapperImpl()
    private lateinit var repositoryImpl: MovieRepositoryImpl
    private lateinit var moviesUseCase: MoviesUseCase
    private lateinit var viewModel: MovieViewModel


    @get:Rule
    var activityRule: ActivityTestRule<MovieActivity>? = ActivityTestRule(
        MovieActivity::class.java
    )
    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    lateinit var context: Context

    @Before
    @Throws(Exception::class)
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(
            context, MovieRoomDatabase::class.java
        ).build()

        movieDaoImpl = db.movieDao()

        val httpModule = HttpClientModule()
        val cache = httpModule.provideCache(context.applicationContext as Application)
        val httpClient = httpModule.provideOkhttpClient(cache)
        val retrofit = httpModule.provideRetrofit(httpClient)

        apiService = httpModule.provideApiService(retrofit)

        sharedPreference = MovieSharedPreference(
            context.getSharedPreferences(
                Constants.sharedPrefName,
                Context.MODE_PRIVATE
            )
        )

        repositoryImpl =
            MovieRepositoryImpl(apiService, movieDaoImpl, sharedPreference, mapper)

        moviesUseCase = MoviesUseCase(repositoryImpl)


        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)
        viewModel = MovieViewModel(moviesUseCase)
        repositoryImpl.getMoviesFromServer()
        viewModel.getData()
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        db.close()
        activityRule = null
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
    }


    @Test
    fun verifyLiveDataHasSomeValue() {
        viewModel.formattedMovieList.observeForTesting {
            Assert.assertNotNull(viewModel.formattedMovieList.value)
        }
    }

    @Test
    fun verifyLiveDataHasSuccessResponse() {
        viewModel.formattedMovieList.observeForTesting {
            Assert.assertTrue(viewModel.formattedMovieList.value!!.status == Status.SUCCESS)
        }
    }

    @Test
    fun verifyApiResponseAndDbSameSize() {

        viewModel.formattedMovieList.observeForTesting {
            Assert.assertTrue(viewModel.formattedMovieList.value!!.data.size == movieDaoImpl.getAllMovies().test().values()[0].size)
        }
    }

    @Test
    fun verifyApiResponseAndDbSameData() {

        viewModel.formattedMovieList.observeForTesting {
            Assert.assertTrue(viewModel.formattedMovieList.value!!.data.size == movieDaoImpl.getAllMovies().test().values()[0].size)
        }
    }

    //should be accessed from a common class
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

