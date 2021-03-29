package com.example.olegweatherapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.olegweatherapp.R
import com.example.olegweatherapp.databinding.FragmentHomeBinding
import com.example.olegweatherapp.extensions.moveLocationToPref
import com.example.olegweatherapp.viewmodels.HomeViewModel
import com.example.olegweatherapp.viewmodels.factories.HomeViewModelFactory
import timber.log.Timber

/**
 * Show last available forecast
 */
class HomeFragment : Fragment() {

    /**
     * One way to delay creation of the viewModel until an appropriate lifecycle method is to use
     * lazy. This requires that viewModel not be referenced before onActivityCreated, which we
     * do in this Fragment.
     */
    private val viewModel: HomeViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, HomeViewModelFactory(activity.application))
            .get(HomeViewModel::class.java)
    }

    /**
     * Called immediately after onCreateView() has returned, and fragment's
     * view hierarchy has been created. It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )
        // Set the lifecycleOwner so DataBinding can observe LiveData
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.hourlyList.adapter = HourlyAdapter()
        binding.dailyList.adapter = DailyAdapter()


        viewModel.eventNetworkError.observe(viewLifecycleOwner, { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })

        val sharedPref = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)
        val scale = sharedPref.getInt("scale", 1)
        viewModel.refreshDataFromRepository(getLocationFromPref(), scale)


        return binding.root
    }

    private fun getLocationFromPref(): Pair<Double, Double> {
        Timber.d("forecast: update location")
        moveLocationToPref(activity)
        val sharedPref = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)
        if (sharedPref != null && sharedPref.contains("latitude") && sharedPref.contains("longitude")) {
            val lat = sharedPref.getFloat("latitude", (40.462212).toFloat()).toDouble()
            val lon = sharedPref.getFloat("longitude", (-2.96039).toFloat()).toDouble()
            return Pair(lat, lon)
        } else return Pair(40.462212, -2.96039)
    }

    /**
     * Method for displaying a Toast error message for network errors.
     */
    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }
}