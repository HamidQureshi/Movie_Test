package com.example.hamid.movies.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.hamid.movies.data.model.MovieResponse

@Database(entities = [MovieResponse::class], version = 1)
abstract class MovieRoomDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

}
