package com.example.hamid.movies.presentation.ui.adaptar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.hamid.movies.R
import com.example.hamid.movies.domain.model.MovieFormatted
import com.example.hamid.movies.presentation.ui.viewmodel.MovieViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_card.view.*

class MovieListAdapter(
    private val viewModel: MovieViewModel
) : RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {

    private var movieList: List<MovieFormatted>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_card, parent, false)

        return ViewHolder(
            view,
            object :
                ViewHolder.IClickListener {

                override fun onClick(caller: View, position: Int) {

                    val movie = movieList!![position]
                    val isChecked = (caller as AppCompatCheckBox).isChecked

                    viewModel.updateFavouriteMovie(movie.id, isChecked)
                }

            })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movieList!![position]
        holder.tvTitle.text = movie.title
        holder.tvVote.text = movie.voteAverage
        holder.tvReleaseDate.text = movie.releaseDate
        Picasso.get()
            .load(movie.posterPath)
            .into(holder.ivPoster)

        holder.cbFavourite.isChecked = movie.favourite

    }

    override fun getItemCount(): Int {
        return if (movieList != null)
            movieList!!.size
        else
            0
    }

    fun setAdapterList(movieList: List<MovieFormatted>) {
        this.movieList = movieList
        notifyDataSetChanged()
    }

    class ViewHolder(view: View, private val clickListener: IClickListener) :
        RecyclerView.ViewHolder(view),
        View.OnClickListener {

        var ivPoster: ImageView = view.iv_poster
        var tvTitle: TextView = view.tv_title
        var tvVote: TextView = view.tv_vote
        var tvReleaseDate: TextView = view.tv_releaseDate
        var cbFavourite: CheckBox = view.cb_fav

        init {
            cbFavourite.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            clickListener.onClick(view, layoutPosition)
        }

        interface IClickListener {
            fun onClick(caller: View, position: Int)
        }

    }

}
