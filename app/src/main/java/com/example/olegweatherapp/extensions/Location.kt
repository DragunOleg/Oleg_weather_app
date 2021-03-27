package com.example.olegweatherapp.extensions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices

/**
 * move location to shared pref
 */
fun moveLocationToPref (activity: Activity?){

    if (activity != null) {
        //check if no permissions granted
        if (ActivityCompat.checkSelfPermission
                (activity.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            //ask user for permission
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(
                        activity, arrayOf(
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION), 1)
            //in lower version fine location give background loc also
            } else {
                ActivityCompat.requestPermissions(
                        activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        //permission granted, pass lat&lon
        } else {
            if(activity.applicationContext != null) {
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity.applicationContext)
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
                        if (sharedPref != null) {
                            with (sharedPref.edit()) {
                                putFloat("latitude", location.latitude.toFloat())
                                putFloat("longitude", location.longitude.toFloat())
                                apply()
                            }
                        }
                    }
                }
            }
        }
    }
}

fun getLocationWithoutActivity(context: Context) : Pair<Double, Double> {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            //no permission = update with dummy location
        } else {
            //permission granted, pass lat&lon
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            val loc = fusedLocationClient.lastLocation.result
            return Pair(loc.latitude, loc.longitude)
        }
    //dummy return
    return Pair(40.462212, -2.96039)
}