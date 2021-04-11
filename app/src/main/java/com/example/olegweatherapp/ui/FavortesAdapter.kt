package com.example.olegweatherapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.olegweatherapp.R
import com.example.olegweatherapp.databinding.FavoritesViewItemBinding
import com.example.olegweatherapp.models.bycityname.ForecastByCity
import kotlin.math.roundToInt

/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * data, including computing diffs between lists.
 * Tricky item click listener to delete explained here
 * https://developer.android.com/codelabs/kotlin-android-training-interacting-with-items#3
 */
class FavortesAdapter(val clickListener: ForecastListener) :
    ListAdapter<ForecastByCity, FavortesAdapter.ForecastViewHolder>(DiffCallback) {

    /**
     * The PictureViewHolder constructor takes the binding variable from the associated
     * FavoritesViewItem, which nicely gives it access to the full [ForecastByCity] information.
     */
    class ForecastViewHolder(private var binding: FavoritesViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(forecastByCity: ForecastByCity, clickListener: ForecastListener) {
            binding.forecastByCity = forecastByCity
            binding.temperature.text = forecastByCity.main.temp.roundToInt().toString() + "°"
            binding.feelsLikeValue.text =
                forecastByCity.main.feelsLike.roundToInt().toString() + "°"
            binding.clickListener = clickListener

            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [ForecastByCity]
     * has been updated.
     * ru explanation of diffutil https://habr.com/ru/company/redmadrobot/blog/460673/
     */
    companion object DiffCallback : DiffUtil.ItemCallback<ForecastByCity>() {
        override fun areItemsTheSame(oldItem: ForecastByCity, newItem: ForecastByCity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ForecastByCity, newItem: ForecastByCity): Boolean {
            return oldItem == newItem
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     * This method needs to return a new PictureViewHolder, created by inflating the ImageItemViewBinding
     * and using LayoutInflater from our parent ViewGroup context
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val withDataBinding: FavoritesViewItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.favorites_view_item,
            parent,
            false
        )
        return ForecastViewHolder(withDataBinding)
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val forecastByCity = getItem(position)
        holder.bind(forecastByCity, clickListener)
    }
}

class ForecastListener(val clickListener: (cityName: String) -> Unit) {
    fun onClick(forecastCity: ForecastByCity) = clickListener(forecastCity.name)
}