package com.hamid.data.utils

import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource
import com.hamid.domain.model.utils.Constants

object EspressoIdlingResource {

    private val mCountingIdlingResource = CountingIdlingResource(Constants.idlingResourceName)

    val idlingResource: IdlingResource
        get() = mCountingIdlingResource

    fun increment() {
        mCountingIdlingResource.increment()
    }

    fun decrement() {
        mCountingIdlingResource.decrement()
    }
}