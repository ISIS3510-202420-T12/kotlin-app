package com.wearabouts.ui.donation.map

import android.Manifest
import android.app.Activity
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

private const val LOCATION_PERMISSION_REQUEST_CODE = 1

class LocationManager {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    fun getUserLocation(context: Context, onLocationReceived: (Location?) -> Unit) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions if not granted
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
    
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            onLocationReceived(location)
        }
    }

}