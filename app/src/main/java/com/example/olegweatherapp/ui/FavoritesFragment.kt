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
import androidx.lifecycle.ViewModelProvider
import com.example.olegweatherapp.R
import com.example.olegweatherapp.databinding.FragmentFavoritesBinding
import com.example.olegweatherapp.viewmodels.FavoritesViewModel
import com.example.olegweatherapp.viewmodels.factories.FavoritesViewModelFactory

/**
 * Show favorite cities from db, add/delete cities
 */
class FavoritesFragment : Fragment() {

    /**
     * One way to delay creation of the viewModel until an appropriate lifecycle method is to use
     * lazy. This requires that viewModel not be referenced before onActivityCreated, which we
     * do in this Fragment.
     */
    private val viewModel: FavoritesViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, FavoritesViewModelFactory(activity.application))
                .get(FavoritesViewModel::class.java)
    }

    /**
     * Called immediately after onCreateView() has returned, and fragment's
     * view hierarchy has been created. It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val textView: TextView = view.findViewById(R.id.text_favorites)
//        viewModel.citiesList.observe(viewLifecycleOwner, {cities ->
//            cities.apply {
//                if(cities.isNotEmpty()) {
//                    textView.text = cities.toString()
//                    }
//                else {
//                    textView.text = "waiting for network"
//                }
//            }
//        })
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val binding: FragmentFavoritesBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_favorites,
                container,
                false)
        // Set the lifecycleOwner so DataBinding can observe LiveData
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.eventNetworkError.observe(viewLifecycleOwner, { isNetworkError ->
            if(isNetworkError) onNetworkError()
        })

        binding.cityList.adapter = FavortesAdapter(ForecastListener { cityName ->
            Toast.makeText(context, "$cityName gone", Toast.LENGTH_LONG).show()
            viewModel.deleteCity(cityName)
        })
        setButtonsBehavior(binding)

        return binding.root
    }

    /**
     * Method for displaying a Toast error message for network errors.
     */
    private fun onNetworkError() {
        if(!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
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
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun Context.showKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInputFromWindow(view.windowToken,0,0)
    }

}