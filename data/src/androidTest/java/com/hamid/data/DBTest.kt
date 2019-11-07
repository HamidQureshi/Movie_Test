package com.hamid.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.hamid.data.local.db.MovieDaoImpl
import com.hamid.data.local.db.MovieRoomDatabase
import com.hamid.data.utils.helper.MockRepoResponse
import org.junit.*

class DBTest {

    private lateinit var db: MovieRoomDatabase
    private lateinit var movieDaoImpl: MovieDaoImpl
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val movieList = MockRepoResponse.movieResponseList

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), MovieRoomDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        movieDaoImpl = db.movieDao()
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        db.close()
    }


    @Test
    fun getDataWhenNoDataInserted() {
        val list = movieDaoImpl.getAllMovies()
            .test()
            .values()[0]

        Assert.assertEquals(0, list.size)
    }

    @Test
    fun insertAndGetData() {
        movieDaoImpl.insertAll(movieList)

        movieDaoImpl.getAllMovies()
            .test()
            .assertValue { it.size == movieList.size && it == movieList }
    }

    @Test
    fun deleteAndGetData() {
        movieDaoImpl.insertAll(movieList)

        movieDaoImpl.deleteAll()
        val list = movieDaoImpl.getAllMovies()
            .test()
            .values()[0]

        Assert.assertEquals(0, list.size)

    }

    @Test
    fun updateFavouriteMovie() {
        movieDaoImpl.insertAll(movieList)

        movieDaoImpl.updateFavouriteMovie(movieList[0].movieId, true)

        movieDaoImpl.getAllMovies()
            .test()
            .assertValue { it[0].favourite }

    }

}


