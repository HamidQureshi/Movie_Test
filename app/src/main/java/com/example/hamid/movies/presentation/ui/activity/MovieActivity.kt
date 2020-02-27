package com.example.hamid.movies.presentation.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hamid.movies.R
import com.example.hamid.movies.presentation.ui.adaptar.MovieListAdapter
import com.example.hamid.movies.presentation.ui.viewmodel.MovieViewModel
import com.hamid.domain.model.model.MovieViewState
import io.uniflow.androidx.flow.onStates
import kotlinx.android.synthetic.main.activity_movie.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MovieActivity : AppCompatActivity() {

    //    val viewModel =  get<MovieViewModel>()
    //by lazy
//    val viewModel by inject<MovieViewModel>()
    //by viewModel
    val viewModel by viewModel<MovieViewModel>()

    private lateinit var itemListAdapter: MovieListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_movie)

        itemListAdapter =
            MovieListAdapter(viewModel)

        val mLayoutManager = LinearLayoutManager(this)
        rv_list.layoutManager = mLayoutManager
        rv_list.itemAnimator = DefaultItemAnimator()
        rv_list.adapter = itemListAdapter

        rv_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if (mLayoutManager.findLastVisibleItemPosition() == mLayoutManager.itemCount - 1) {
                    if (pb_nextPageLoading.visibility == View.GONE) {
                        pb_nextPageLoading.visibility = View.VISIBLE
                        viewModel.getMoviesFromServer()
                    }
                }

                super.onScrolled(recyclerView, dx, dy)
            }
        })

        viewModel.getData()

        // Observe incoming states
        onStates(viewModel) { state ->
            when (state) {
                // react on WeatherState update
                is MovieViewState.Init -> progress_bar.visibility = View.VISIBLE
                is MovieViewState.Success -> {

                    if (pb_nextPageLoading.visibility == View.VISIBLE) {
                        pb_nextPageLoading.visibility = View.GONE
                    }
                    progress_bar.visibility = View.GONE
                    itemListAdapter.setAdapterList(state.movieList)
                }
                is MovieViewState.Failed -> progress_bar.visibility = View.VISIBLE
            }
        }

    }

}
