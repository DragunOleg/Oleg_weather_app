package com.example.olegweatherapp.ui

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

        //data binding
        binding.viewModel = viewModel

        //binding adapters would connect adapter with data from ViewModel
        binding.hourlyList.adapter = HourlyAdapter()
        binding.dailyList.adapter = DailyAdapter()

        //refresh only on explicit user action
        binding.swipeRefreshHome.setOnRefreshListener {
            //update current location
            moveLocationToPref(requireActivity())
            //refresh data
            viewModel.refreshDataFromRepository()
            //end of refresh animation
            binding.swipeRefreshHome.isRefreshing = false
        }

        //any error would trigger onNetworkError
        viewModel.eventNetworkError.observe(viewLifecycleOwner, { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.d("forecast: onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("forecast: onDestroy")
    }

    /**
     * Method for displaying a Toast error message for network errors.
     */
    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show()
            viewModel.onNetworkErrorShown()
        }
    }
}