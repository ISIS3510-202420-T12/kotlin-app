package com.wearabouts.ui.donation.map

// Debugging
import android.util.Log

// Android management
import android.Manifest
import android.app.Activity
import android.content.Context
import android.location.LocationManager
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import android.content.IntentSender

// Google play services for permission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

// To request turn on location
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes

import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import android.os.Looper

class LocationService {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val LOCATION_SETTINGS_REQUEST_CODE = 2
    }

    fun getLocationOnly(context: Context, onSuccess: (Location?) -> Unit, onFailure: () -> Unit) {
        Log.d("Donation", "getLocationOnly: creating location request")

        // Create location request
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 0
            fastestInterval = 0
            numUpdates = 1
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            Log.d("Donation", "getLocationOnly: onSuccess result = $location")
            onSuccess(location)
        }.addOnFailureListener {
            // Request location updates if lastLocation is null
            fusedLocationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    fusedLocationClient.removeLocationUpdates(this)
                    val newLocation = locationResult.lastLocation
                    Log.d("Donation", "getLocationOnly: onSuccess result from updates = $newLocation")
                    onSuccess(newLocation)
                }
            }, Looper.getMainLooper())
        }
    }

    fun getUserLocation(
        context: Context,
        onSuccess: (Location?) -> Unit,
        onFailure: () -> Unit
    ) {
        // Check and request location permissions
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            onFailure()
            return
        }

        // Check and request to enable location services
        requestLocationSettings(context, onSuccess = {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                onSuccess(location)
            }.addOnFailureListener {
                onFailure()
            }
        }, onFailure = {
            onFailure()
        })
    }

    fun requestLocationSettings(
        context: Context,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(context)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener { response ->
            val states = response.locationSettingsStates
            if (states.isLocationPresent) {
                onSuccess()
            } else {
                onFailure()
            }
        }.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(context as Activity, LOCATION_SETTINGS_REQUEST_CODE)
                } catch (sendEx: IntentSender.SendIntentException) {
                    onFailure()
                }
            } else {
                onFailure()
            }
        }
    }

    fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || 
               locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

}