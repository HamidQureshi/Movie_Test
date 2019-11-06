package com.hamid.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hamid.data.model.MovieResponse
import com.hamid.domain.model.repository.MovieDao
import io.reactivex.Flowable

@Dao
interface MovieDaoImpl : MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<MovieResponse>)

    @Query("SELECT * from movie_table ")
    fun getAllMovies(): Flowable<List<MovieResponse>>

    @Query("DELETE FROM movie_table")
    fun deleteAll()

    @Query("UPDATE movie_table SET favourite = :favourite WHERE movieId = :id")
    fun updateFavouriteMovie(id: Int, favourite: Boolean)

}