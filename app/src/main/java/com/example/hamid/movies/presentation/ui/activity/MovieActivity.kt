package com.example.hamid.movies.presentation.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hamid.movies.R
import com.example.hamid.movies.domain.model.Status
import com.example.hamid.movies.presentation.factory.ViewModelFactory
import com.example.hamid.movies.presentation.ui.adaptar.MovieListAdapter
import com.example.hamid.movies.presentation.ui.viewmodel.MovieViewModel
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_movie.*
import javax.inject.Inject


class MovieActivity : AppCompatActivity() {

    lateinit var viewModel: MovieViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var itemListAdapter: MovieListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_movie)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieViewModel::class.java)

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

        viewModel.formattedMovieList.observe(this, Observer { movies ->

            if (pb_nextPageLoading.visibility == View.VISIBLE) {
                pb_nextPageLoading.visibility = View.GONE
            }

            if (movies.status == Status.SUCCESS) {
                progress_bar.visibility = View.GONE
                itemListAdapter.setAdapterList(movies!!.data)
            } else {
                progress_bar.visibility = View.VISIBLE
            }

        })

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.compositeDisposable.clear()
    }
}
