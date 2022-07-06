package com.example.android.imdbfinalapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.imdbfinalapp.R
import com.example.android.imdbfinalapp.databinding.HomePageItemBinding
import com.example.android.imdbfinalapp.models.Movie
import com.example.android.imdbfinalapp.util.Util


class MovieListAdapter(
    private val context: Context,
    private val listenerItem: OnItemClickListener,
    private val favoriteListener: OnFavoriteClickListener
    ): ListAdapter<Movie,MovieListAdapter.HomePageViewHolder>(DiffCallback) {
    companion object DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePageViewHolder {
        val binding: HomePageItemBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.home_page_item,parent, false)

        return HomePageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomePageViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }


    inner class HomePageViewHolder(var binding: HomePageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(movie: Movie) {
            movie.poster_path?.let { Util.loadImage(context, it, binding.imdbPoster) }
            binding.title.text = movie.title
            if (movie.release_date != null) {
                binding.year.text = movie.release_date!!.substringBefore("-", "")
            } else {
                binding.year.text = "-"
            }
            if (movie.favorite) {
                binding.favoriteBtn.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.red_favorite)
                )
            } else {
                binding.favoriteBtn.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.white_favorite)
                )
            }
            binding.movieItem.setOnClickListener {
                val position = this@HomePageViewHolder.adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listenerItem.onClick(getItem(position))
                }
            }

            binding.favoriteBtn.setOnClickListener {
                val position = this@HomePageViewHolder.adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    favoriteListener.onFavoriteClick(getItem(position))
                    notifyDataSetChanged()
                }
            }

//            binding.bookMark.setOnClickListener { v ->
//                val position = this@HomePageViewHolder.adapterPosition
//                if (position != RecyclerView.NO_POSITION) {
//                    listener?.onBookMarkClicked(results[position], position, this@HomePageAdapters)
//                }
//            }
            binding.executePendingBindings()
        }
    }

    class OnItemClickListener(val clickListener: (movie: Movie) -> Unit) {
        fun onClick(movie: Movie) = clickListener(movie)
    }

    class OnFavoriteClickListener(val favoriteClickListener: (movie: Movie) -> Unit){
        fun onFavoriteClick(movie: Movie) = favoriteClickListener(movie)
    }
//    interface OnHomePageItemClickListener {
//        fun onItemClick(movie: Movie)
//        //fun onBookMarkClicked(result: Result?, position: Int, listener: BookMarkListener?)
//    }
}
