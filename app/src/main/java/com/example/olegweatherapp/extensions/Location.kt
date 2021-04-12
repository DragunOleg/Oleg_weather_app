package com.example.olegweatherapp.extensions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import timber.log.Timber

/**
 * move location to shared pref
 * TODO() call current new location instead of fused location
 * https://codelabs.developers.google.com/codelabs/while-in-use-location#1
 * it is currently not UPDATING loc, just checking update from others
 */
fun moveLocationToPref(activity: Activity) {

        //check if no permissions granted
        if (ActivityCompat.checkSelfPermission
                (activity.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            //ask user for permission
            ActivityCompat.requestPermissions(
                activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
            //permission granted, pass lat&lon
        } else {
            if (activity.applicationContext != null) {
                val fusedLocationClient =
                    LocationServices.getFusedLocationProviderClient(activity.applicationContext)
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val sharedPref =
                            activity.getSharedPreferences("settings", Context.MODE_PRIVATE)
                        Timber.d("forecast: putFloat lat ${location.latitude.toFloat()} " +
                                "putFloat lon ${location.longitude.toFloat()}")
                        with(sharedPref.edit()) {
                            putFloat("latitude", location.latitude.toFloat())
                            putFloat("longitude", location.longitude.toFloat())
                            apply()
                        }
                    }
                }
            }
        }
}