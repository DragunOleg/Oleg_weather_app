package com.example.olegweatherapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.olegweatherapp.R
import com.example.olegweatherapp.databinding.FragmentHomeBinding
import com.example.olegweatherapp.extensions.moveLocationToPref
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

/**
 * Show last available forecast
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {

    /**
     * One way to delay creation of the viewModel until an appropriate lifecycle method is to use
     * lazy. This requires that viewModel not be referenced before onActivityCreated, which we
     * do in this Fragment.
     */
    private val viewModel: HomeViewModel by viewModels()

    private var binding: FragmentHomeBinding? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val fragmentBinding: FragmentHomeBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_home,
                container,
                false
        )

        binding = fragmentBinding

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            // Set the lifecycleOwner so DataBinding can observe LiveData
            lifecycleOwner = viewLifecycleOwner
            //data binding
            viewModel = this@HomeFragment.viewModel

            //binding adapters would connect adapter with data from ViewModel
            hourlyList.adapter = HomeHourlyAdapter()
            dailyList.adapter = HomeDailyAdapter()

            //refresh only on explicit user action
            swipeRefreshHome.setOnRefreshListener {
                //update current location
                Timber.d("forecast: move loc to pref")
                moveLocationToPref(requireActivity())
                //refresh data
                this@HomeFragment.viewModel.refreshDataFromRepository()
                //end of refresh animation
                swipeRefreshHome.isRefreshing = false
            }
        }

        //any error would trigger onNetworkError
        viewModel.eventNetworkError.observe(viewLifecycleOwner, { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
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