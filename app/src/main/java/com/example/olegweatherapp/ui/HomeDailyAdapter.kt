package com.example.olegweatherapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.olegweatherapp.R
import com.example.olegweatherapp.databinding.DailyViewItemBinding
import com.example.olegweatherapp.models.bycityname.ForecastByCity
import com.example.olegweatherapp.models.onecall.Daily


class HomeDailyAdapter : ListAdapter<Daily, HomeDailyAdapter.DailyViewHolder>(DiffCallback) {

    class DailyViewHolder(private var binding: DailyViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(daily: Daily) {
            binding.daily = daily

            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [ForecastByCity]
     * has been updated.
     * ru explanation of diffutil https://habr.com/ru/company/redmadrobot/blog/460673/
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Daily>() {
        override fun areItemsTheSame(oldItem: Daily, newItem: Daily): Boolean {
            //comparing items in memory
            return oldItem.dt == newItem.dt
        }

        override fun areContentsTheSame(oldItem: Daily, newItem: Daily): Boolean {
            //comparing based on content from JSON
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        val withDataBinding: DailyViewItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.daily_view_item,
            parent,
            false
        )
        return DailyViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        val daily = getItem(position)
        holder.bind(daily)
    }
}