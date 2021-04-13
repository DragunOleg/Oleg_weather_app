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
import com.google.android.material.slider.Slider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class SettingsFragment : Fragment() {


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val binding: FragmentSettingsBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_settings, container, false
        )

        setupSlider(binding)

        setupButtonsGroup(binding)

        return binding.root
    }

    private fun setupButtonsGroup(binding: FragmentSettingsBinding) {
        val sharedPref = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)
        //1 = metric, 2 = standard, 3 = imperial
        val prefScale = sharedPref.getInt("scale", 1)
        when (prefScale) {
            1 -> binding.buttonGroup.check(binding.buttonMetric.id)
            2 -> binding.buttonGroup.check(binding.buttonStandard.id)
            3 -> binding.buttonGroup.check(binding.buttonImperial.id)
        }

        fun callNetworkUpdate() {
            val prefPeriod = sharedPref.getInt("updatePeriod", 60)
            CoroutineScope(Dispatchers.IO).launch {
                setupRecurringWork(prefPeriod, requireContext())
            }
        }

        binding.buttonGroup.addOnButtonCheckedListener { _, checkedId, _ ->
            when (checkedId) {
                binding.buttonMetric.id -> {
                    // without if statement previous button also chang state and toggle action
                    if (binding.buttonMetric.isChecked) {
                        Toast.makeText(this.context, "changed to metric", Toast.LENGTH_SHORT).show()
                        sharedPref.edit().putInt("scale", 1).apply()
                        callNetworkUpdate()
                    }
                }
                binding.buttonStandard.id -> {
                    if (binding.buttonStandard.isChecked) {
                        Toast.makeText(this.context, "changed to standard", Toast.LENGTH_SHORT)
                                .show()
                        sharedPref.edit().putInt("scale", 2).apply()
                        callNetworkUpdate()
                    }
                }
                binding.buttonImperial.id -> {
                    if (binding.buttonImperial.isChecked) {
                        Toast.makeText(this.context, "changed to imperial", Toast.LENGTH_SHORT)
                                .show()
                        sharedPref.edit().putInt("scale", 3).apply()
                        callNetworkUpdate()
                    }
                }
            }
        }
    }

    private fun setupSlider(binding: FragmentSettingsBinding) {
        val sharedPref = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)
        val prefPeriod = sharedPref.getInt("updatePeriod", 60).toFloat() / 60
        Timber.d("forecast pref period is $prefPeriod")
        binding.slider.value = prefPeriod
        binding.slider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {}

            override fun onStopTrackingTouch(slider: Slider) {
                val result = (slider.value * 4).toInt() * 15
                sharedPref.edit().putInt("updatePeriod", result).apply()
                Timber.d("forecast: put update $result")
                CoroutineScope(Dispatchers.IO).launch {
                    setupRecurringWork(result, requireContext())
                }
                Toast.makeText(
                        requireContext(),
                        "will update every ${slider.value} hours",
                        Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}