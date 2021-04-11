package com.example.olegweatherapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.olegweatherapp.R
import com.example.olegweatherapp.databinding.HourlyViewItemBinding
import com.example.olegweatherapp.models.bycityname.ForecastByCity
import com.example.olegweatherapp.models.onecall.Hourly

class HourlyAdapter : ListAdapter<Hourly, HourlyAdapter.HourlyViewHolder>(DiffCallback) {

    class HourlyViewHolder(private var binding: HourlyViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(hourly: Hourly) {
            binding.hourly = hourly

            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [ForecastByCity]
     * has been updated.
     * ru explanation of diffutil https://habr.com/ru/company/redmadrobot/blog/460673/
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Hourly>() {
        override fun areItemsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
            //comparing items in memory
            return oldItem.dt == newItem.dt
        }

        override fun areContentsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
            //comparing based on content from JSON
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val withDataBinding: HourlyViewItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.hourly_view_item,
            parent,
            false
        )
        return HourlyViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val hourly = getItem(position)
        holder.bind(hourly)
    }
}