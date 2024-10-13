package com.wearabouts.ui.donation

import com.wearabouts.ui.base.BaseContentPage
import com.wearabouts.ui.donation.map.LocationManager
import com.wearabouts.ui.donation.map.MapManager

import androidx.appcompat.app.AppCompatActivity

import androidx.compose.material3.Text

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background

import androidx.compose.runtime.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import android.location.Location
import kotlinx.coroutines.launch
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.content.Context

class Donation : BaseContentPage() {

    @Composable
    override fun Content() {

        val context = LocalContext.current
        val locationManager = LocationManager()
        val mapManager = MapManager()
        var userLocation by remember { mutableStateOf<Location?>(null) }
        var locationStatus by remember { mutableStateOf("Obtaining location...") }
        var hasLocationPermission by remember { mutableStateOf(false) }

        // Permission launcher
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            hasLocationPermission = isGranted
        }

        // Check and request permission if not granted
        LaunchedEffect(Unit) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            } else {
                hasLocationPermission = true
            }
        }

        // Fetch location when permission is granted
        LaunchedEffect(hasLocationPermission) {
            if (hasLocationPermission) {
                fetchLocation(locationManager, context, onSuccess = { location ->
                    userLocation = location
                    locationStatus = "Location: lat -> ${location?.latitude} | long -> ${location?.longitude}"
                    // mapManager.showMap(location?.latitude ?: 0.0, location?.longitude ?: 0.0)
                }, onFailure = {
                    locationStatus = "Failed to obtain location"
                })
            } else {
                locationStatus = "Location permission denied"
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top=16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(locationStatus)
        }
    
    }

    private fun fetchLocation(
        locationManager: LocationManager,
        context: Context,
        onSuccess: (Location?) -> Unit,
        onFailure: () -> Unit
    ) {
        locationManager.getUserLocation(
            context,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }

}