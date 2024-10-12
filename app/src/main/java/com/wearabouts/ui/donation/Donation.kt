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
                coroutineScope.launch {
                    userLocation = locationManager.getUserLocation(context)
                }
            }
        }

        // Check for permission and request if not granted
        LaunchedEffect(Unit) {
            if (!hasLocationPermission) {
                // Request permission (this is a simplified example, you might need to handle the permission request result)
                // In a real app, you should use ActivityResultContracts.RequestPermission
                // to handle the permission request and result properly.
                // For simplicity, we assume the permission is granted after the request.
                // You should replace this with proper permission handling logic.
                hasLocationPermission = true // Simulate permission granted
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top=16.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (userLocation != null) {
                Text("Location: lat -> ${userLocation!!.latitude} | long -> ${userLocation!!.longitude}")
            } else {
                Text("Obtaining location...")
            }
        }
    
    }
}