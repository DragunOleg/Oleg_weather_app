package com.example.olegweatherapp.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.olegweatherapp.R
import com.example.olegweatherapp.databinding.FragmentFavoritesBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Show favorite cities from db, add/delete cities
 */
@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    /**
     * One way to delay creation of the viewModel until an appropriate lifecycle method is to use
     * lazy. This requires that viewModel not be referenced before onActivityCreated, which we
     * do in this Fragment.
     */
    private val viewModel: FavoritesViewModel by viewModels()

    private var binding: FragmentFavoritesBinding? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val fragmentBinding: FragmentFavoritesBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_favorites,
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
            viewModel = this@FavoritesFragment.viewModel
            swipeRefreshFavorites.setOnRefreshListener {
                this@FavoritesFragment.viewModel.refreshDataFromRepository()
                swipeRefreshFavorites.isRefreshing = false
            }

            cityList.adapter = FavortesAdapter(ForecastListener { cityName ->
                Toast.makeText(context, "$cityName gone", Toast.LENGTH_SHORT).show()
                this@FavoritesFragment.viewModel.deleteCity(cityName)
            })

            setButtonsBehavior(binding!!)
        }

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

    private fun setButtonsBehavior(binding: FragmentFavoritesBinding) {
        //fab behavior
        with(binding) {
            fab.setOnClickListener {
                button.visibility = View.VISIBLE
                textLayout.visibility = View.VISIBLE
                view?.let { activity?.showKeyboard(it) }
                textInput.requestFocus()
            }
        }

        //add city button behavior
        with(binding) {
            button.setOnClickListener {
                if (!textInput.text.isNullOrBlank()) {
                    viewModel?.addCity(textInput.text.toString())
                }
                textLayout.visibility = View.GONE
                textInput.text?.clear()
                button.visibility = View.GONE
                view?.let { activity?.hideKeyboard(it) }
            }
        }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun Context.showKeyboard(view: View) {
        val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInputFromWindow(view.windowToken, 0, 0)
    }

}