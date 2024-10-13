package com.wearabouts.ui.donation

import com.wearabouts.ui.base.BaseContentPage
import com.wearabouts.ui.donation.map.LocationManager

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

class Donation : BaseContentPage() {

    @Composable
    override fun Content() {

        val context = LocalContext.current
        val locationManager = LocationManager()
        var userLocation by remember { mutableStateOf<Location?>(null) }
        var locationStatus by remember { mutableStateOf("Obtaining location...") }
        val coroutineScope = rememberCoroutineScope()

        // Track permission state
        var hasLocationPermission by remember {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            )
        }

        // Permission launcher
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            hasLocationPermission = isGranted
        }

        // Request permission if not granted
        LaunchedEffect(Unit) {
            if (!hasLocationPermission) {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }

        // Re-trigger location fetching when permission state changes
        LaunchedEffect(hasLocationPermission) {
            if (hasLocationPermission) {
                locationManager.getUserLocation(
                    context,
                    onSuccess = { location ->
                        userLocation = location
                        locationStatus = "Location: lat -> ${location?.latitude} | long -> ${location?.longitude}"
                    },
                    onFailure = {
                        locationStatus = "Location permission denied"
                    }
                )
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
}