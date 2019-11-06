package com.hamid.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hamid.data.model.MovieResponse

@Database(entities = [MovieResponse::class], version = 1)
abstract class MovieRoomDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDaoImpl

}
