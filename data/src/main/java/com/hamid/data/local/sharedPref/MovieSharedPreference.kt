package com.hamid.data.local.sharedPref

import android.content.SharedPreferences
import com.hamid.domain.model.utils.Constants

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