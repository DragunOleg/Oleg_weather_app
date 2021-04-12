package com.example.olegweatherapp.ui

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.olegweatherapp.R
import com.example.olegweatherapp.models.bycityname.ForecastByCity
import com.example.olegweatherapp.models.onecall.Daily
import com.example.olegweatherapp.models.onecall.Hourly

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.ic_broken_image)
            .into(imgView)
    }
}

@BindingAdapter("favoritesListData")
fun bindFavoritesRecyclerView(recyclerView: RecyclerView, data: List<ForecastByCity>?) {
    val adapter = recyclerView.adapter as FavortesAdapter
    adapter.submitList(data)
}

@BindingAdapter("hourlyListData")
fun bindHourlyRecyclerView(recyclerView: RecyclerView, data: List<Hourly>?) {
    val adapter = recyclerView.adapter as HomeHourlyAdapter
    adapter.submitList(data)

}

@BindingAdapter("dailyListData")
fun bindDailyRecyclerView(recyclerView: RecyclerView, data: List<Daily>?) {
    val adapter = recyclerView.adapter as HomeDailyAdapter
    adapter.submitList(data)
}



