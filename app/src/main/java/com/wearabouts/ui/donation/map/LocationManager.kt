package com.wearabouts.ui.donation.map


// import android.app.Activity
// import android.content.Context
// import android.location.Location

// Location 
import androidx.core.content.ContextCompat
import android.Manifest
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager

import androidx.compose.material3.Text


class LocationManager {

    fun request() {
        // val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission already granted
        } else {
            // Request permission
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
        }

        // if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        //     // Prompt the user to enable GPS
        //     Text("Location permission obtained, enable GPS please")
        // }

        override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
            when (requestCode) {
                REQUEST_CODE -> {
                    if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // Permission granted, proceed with location-related tasks
                        Text("Location permission obtained!")
                    } else {
                        // Permission denied, handle accordingly
                        Text("Location permission not obtained")
                    }
                }
            }
        }
    }
}