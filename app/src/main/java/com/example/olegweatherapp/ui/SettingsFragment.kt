package com.example.olegweatherapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.olegweatherapp.R
import com.example.olegweatherapp.databinding.FragmentSettingsBinding
import com.example.olegweatherapp.setupRecurringWork
import timber.log.Timber

class SettingsFragment : Fragment() {


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val binding: FragmentSettingsBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_settings, container, false)

        setupSlider(binding)

        setupRadioGroup(binding)

        return binding.root
    }

    private fun setupRadioGroup(binding: FragmentSettingsBinding) {
        val sharedPref = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)
        //1 = metric, 2 = standard, 3 = imperial
        val prefScale = sharedPref.getInt("scale", 1)
        when (prefScale) {
            1 -> binding.radioMetric.isChecked = true
            2 -> binding.radioStandard.isChecked = true
            3 -> binding.radioImperial.isChecked = true
        }
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding.radioMetric.id -> {
                    Toast.makeText(this.context, "changed to metric", Toast.LENGTH_SHORT).show()
                    sharedPref.edit().putInt("scale", 1).apply()
                }
                binding.radioStandard.id -> {
                    Toast.makeText(this.context, "changed to standard", Toast.LENGTH_SHORT).show()
                    sharedPref.edit().putInt("scale", 2).apply()
                }
                binding.radioImperial.id -> {
                    Toast.makeText(this.context, "changed to imperial", Toast.LENGTH_SHORT).show()
                    sharedPref.edit().putInt("scale", 3).apply()
                }
            }
        }
    }

    private fun setupSlider(binding: FragmentSettingsBinding) {
        val sharedPref = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)
        val prefPeriod = sharedPref.getInt("updatePeriod", 60).toFloat() / 60
        Timber.d("forecast pref period is $prefPeriod")
        binding.slider.value = prefPeriod
        binding.slider.addOnChangeListener { slider, value, fromUser ->
            // Responds to when slider's value is changed
            val result = (value * 4).toInt() * 15
            sharedPref.edit().putInt("updatePeriod", result).apply()
            Timber.d("forecast: put update $result")
            setupRecurringWork(result)
        }
    }
}