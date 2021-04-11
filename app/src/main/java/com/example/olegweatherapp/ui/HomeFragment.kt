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
import com.example.olegweatherapp.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

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
//    lazy {
//        val activity = requireNotNull(this.activity) {
//            "You can only access the viewModel after onActivityCreated()"
//        }
//        ViewModelProvider(this, HomeViewModelFactory(activity.application))
//                .get(HomeViewModel::class.java)
//    }

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
            hourlyList.adapter = HourlyAdapter()
            dailyList.adapter = DailyAdapter()

            //refresh only on explicit user action
            swipeRefreshHome.setOnRefreshListener {
                //update current location
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