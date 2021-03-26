package com.example.olegweatherapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getLocation()

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_favorites, R.id.navigation_settings))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    /**
     * move location to shared pref
     */
    fun getLocation(){

        //check if no permissions granted
        if (this.applicationContext.let {
                    ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) }
                != PackageManager.PERMISSION_GRANTED
                || this.applicationContext.let {
                    ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_BACKGROUND_LOCATION) }
                != PackageManager.PERMISSION_GRANTED) {
            //ask user for permission
            this.let {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    ActivityCompat.requestPermissions(
                            it, arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION), 1)
                } else {
                    ActivityCompat.requestPermissions(
                            it, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
                }
            }
        } else {
            //permission granted, pass lat&lon
            if(this.applicationContext != null) {
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.applicationContext)
                fusedLocationClient.lastLocation
                        .addOnSuccessListener { location: Location? ->
                            if (location != null) {
                                val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
                                if (sharedPref != null) {
                                    with (sharedPref.edit()) {
                                        putFloat("latitude", location.latitude.toFloat())
                                        putFloat("longtitude", location.longitude.toFloat())
                                        apply()
                                    }
                                }
                            }
                        }
            }
        }
    }
}