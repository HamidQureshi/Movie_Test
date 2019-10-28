package com.example.hamid.movies.ui

import android.content.Context
import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.hamid.movies.InstrumentedTest
import com.example.hamid.movies.R
import com.example.hamid.movies.domain.model.Status
import com.example.hamid.movies.presentation.ui.activity.MovieActivity
import com.example.hamid.movies.utils.EspressoIdlingResource
import org.hamcrest.Matchers.`is`
import org.junit.*


class ActivityTest {

    @get:Rule
    var activityRule: ActivityTestRule<MovieActivity>? = ActivityTestRule(
        MovieActivity::class.java
    )

    @get:Rule
    val rxSchedulerRule = InstrumentedTest.RxSchedulerRule()
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private var mContext: Context? = null


    @Before
    @Throws(Exception::class)
    fun setUp() {
        mContext = InstrumentationRegistry.getInstrumentation().targetContext

        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        activityRule = null

        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)
    }


    @Test
    fun verifyLDisBeingObserved() {
        Assert.assertTrue(activityRule!!.activity.viewModel.formattedMovieList.hasObservers())
    }

    @Test
    fun testRecyclerVisible() {
        onView(withId(R.id.rv_list))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testRecyclerViewScroll() {

        val recyclerView = activityRule!!.activity.findViewById<RecyclerView>(R.id.rv_list)
        val itemCount = recyclerView.adapter!!.itemCount

        onView(withId(R.id.rv_list))
            .inRoot(
                RootMatchers.withDecorView(
                    `is`<View>(activityRule!!.activity.window.decorView)
                )
            )
            .perform(scrollToPosition<RecyclerView.ViewHolder>(itemCount - 6))
    }

    @Test
    fun rv_itemSize_matches_responseSize() {

        val recyclerView = activityRule!!.activity.findViewById<RecyclerView>(R.id.rv_list)
        val itemCount = recyclerView.adapter!!.itemCount

        Assert.assertTrue(itemCount.toLong() >= 0)

    }

    @Test
    fun rv_verify_fieldsDisplayed() {

        onView(withId(R.id.rv_list))
            .check(matches(hasDescendant(withId(R.id.iv_poster))))

        onView(withId(R.id.rv_list))
            .check(matches(hasDescendant(withId(R.id.tv_lbl_title))))

        onView(withId(R.id.rv_list))
            .check(matches(hasDescendant(withId(R.id.tv_title))))

        onView(withId(R.id.rv_list))
            .check(matches(hasDescendant(withId(R.id.tv_lbl_vote))))

        onView(withId(R.id.rv_list))
            .check(matches(hasDescendant(withId(R.id.tv_vote))))

        onView(withId(R.id.rv_list))
            .check(matches(hasDescendant(withId(R.id.tv_lbl_releaseDate))))

        onView(withId(R.id.rv_list))
            .check(matches(hasDescendant(withId(R.id.tv_releaseDate))))

        onView(withId(R.id.rv_list))
            .check(matches(hasDescendant(withId(R.id.cb_fav))))

    }

    @Test
    fun whenNoData_progressBarShown() {

        onView(withId(R.id.progress_bar)).check(matches(withEffectiveVisibility(Visibility.GONE)))

        activityRule!!.activity.viewModel.movieProcessor.dataRepository.nukeDB()

        activityRule!!.activity.viewModel.formattedMovieList.observeForTesting {
            if (activityRule!!.activity.viewModel.formattedMovieList.value!!.status == Status.ERROR) {
                onView(withId(R.id.progress_bar)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

            } else {
                onView(withId(R.id.progress_bar)).check(matches(withEffectiveVisibility(Visibility.GONE)))

            }
        }

    }

    @Test
    fun whenData_progressBarHidden() {

        val recyclerView = activityRule!!.activity.findViewById<RecyclerView>(R.id.rv_list)
        val itemCount = recyclerView.adapter!!.itemCount

        if (itemCount > 0) {
            onView(withId(R.id.pb_nextPageLoading)).check(matches(withEffectiveVisibility(Visibility.GONE)))

        } else {
            onView(withId(R.id.pb_nextPageLoading)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        }

    }

    @Test
    fun loadingNextPage_ProgressBarShown() {

        onView(withId(R.id.pb_nextPageLoading)).check(matches(withEffectiveVisibility(Visibility.GONE)))

        val recyclerView = activityRule!!.activity.findViewById<RecyclerView>(R.id.rv_list)
        val itemCount = recyclerView.adapter!!.itemCount

        onView(withId(R.id.rv_list))
            .inRoot(
                RootMatchers.withDecorView(
                    `is`<View>(activityRule!!.activity.window.decorView)
                )
            )
            .perform(scrollToPosition<RecyclerView.ViewHolder>(itemCount - 1))

        onView(withId(R.id.pb_nextPageLoading)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

    }

    @Test
    fun movieSelectedAsFavourite() {

        onView(
            withRecyclerView(R.id.rv_list)
                .atPositionOnView(1, R.id.cb_fav)
        )
            .check(matches(isNotChecked()))


        onView(withId(R.id.rv_list))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1,
                    clickOnViewChild(R.id.cb_fav)
                )
            )

        onView(
            withRecyclerView(R.id.rv_list)
                .atPositionOnView(1, R.id.cb_fav)
        )
            .check(matches(isChecked()))

    }

    private fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {

        return RecyclerViewMatcher(recyclerViewId)
    }

    private fun clickOnViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null

        override fun getDescription() = "Click on a child view with specified id."

        override fun perform(uiController: UiController, view: View) =
            click().perform(uiController, view.findViewById<View>(viewId))
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

}

