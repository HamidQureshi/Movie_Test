package com.example.hamid.movies.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.hamid.movies.data.local.db.MovieDao
import com.example.hamid.movies.data.local.db.MovieRoomDatabase
import com.example.hamid.movies.utils.helper.MockResponse
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DBTest {

    private lateinit var db: MovieRoomDatabase
    private lateinit var movieDao: MovieDao
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val movieList = MockResponse.movieResponseList

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), MovieRoomDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        movieDao = db.movieDao()
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        db.close()
    }


    @Test
    fun getDataWhenNoDataInserted() {
        val list = movieDao.getAllMovies()
            .test()
            .values()[0]

        Assert.assertEquals(0, list.size)
    }

    @Test
    fun insertAndGetData() {
        movieDao.insertAll(movieList)

        movieDao.getAllMovies()
            .test()
            .assertValue { it.size == movieList.size && it == movieList }
    }

    @Test
    fun deleteAndGetData() {
        movieDao.insertAll(movieList)

        movieDao.deleteAll()
        val list = movieDao.getAllMovies()
            .test()
            .values()[0]

        Assert.assertEquals(0, list.size)

    }

    @Test
    fun updateFavouriteMovie() {
        movieDao.insertAll(movieList)

        movieDao.updateFavouriteMovie(movieList[0].movieId, true)

        movieDao.getAllMovies()
            .test()
            .assertValue { it[0].favourite }

    }

}


