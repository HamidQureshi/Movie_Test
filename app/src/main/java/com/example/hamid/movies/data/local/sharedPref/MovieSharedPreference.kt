package com.example.hamid.movies.data.local.sharedPref

import android.content.SharedPreferences
import com.example.hamid.movies.utils.Constants

class MovieSharedPreference
constructor(private val sharedPreferences: SharedPreferences) {

    fun incrementPageNumber() {
        with(sharedPreferences.edit()) {
            putInt(Constants.pageNumberKey, getPageNumber() + 1)
            commit()
        }
    }

    fun getPageNumber(): Int {
        return sharedPreferences.getInt(Constants.pageNumberKey, 1)
    }

}