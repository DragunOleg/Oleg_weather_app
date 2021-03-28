package com.example.olegweatherapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.olegweatherapp.Injection
import com.example.olegweatherapp.R
import com.example.olegweatherapp.databinding.FragmentSettingsBinding
import com.example.olegweatherapp.setupRecurringWork
import com.example.olegweatherapp.viewmodels.SettingsViewModel
import timber.log.Timber

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        settingsViewModel = ViewModelProvider(this, Injection.provideSettingsViewModelFactory())
            .get(SettingsViewModel::class.java)
        val binding : FragmentSettingsBinding = DataBindingUtil.inflate(
               inflater, R.layout.fragment_settings, container, false)

        setupSlider(binding)

//        val textView: TextView = root.findViewById(R.id.text_settings)
//        settingsViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return binding.root
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