package com.example.android.imdbfinalapp.domain.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

object Util {

    //companion object {
        fun loadImage(context: Context, url: String, imageView: ImageView) {
            Glide.with(context)
                .load(Constants.IMAGE_BASE_URL + url)
                .error(android.R.drawable.stat_notify_error)
                .into(imageView)
        }

        fun getGenresFromIds(ids: List<Int>): ArrayList<String> {
            val map = HashMap<Int, String>()
            map[28] = "Action"
            map[12] = "Adventure"
            map[16] = "Animation"
            map[35] = "Comedy"
            map[80] = "Crime"
            map[99] = "Documentary"
            map[18] = "Drama"
            map[10751] = "Family"
            map[14] = "Fantasy"
            map[36] = "History"
            map[27] = "Horror"
            map[10402] = "Music"
            map[9648] = "Mystery"
            map[10749] = "Romance"
            map[878] = "Science Fiction"
            map[10770] = "TV Movie"
            map[53] = "Thriller"
            map[10752] = "War"
            map[37] = "Western"
            val genres = ArrayList<String>()
            for (i in ids) {
                val item = map[i]
                if (item != null) {
                    genres.add(item)
                }
            }
            return genres
        }
 //   }

}