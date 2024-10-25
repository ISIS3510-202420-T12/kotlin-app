package com.wearabouts.ui.donation

// Debugging
import android.util.Log

// Components
import com.wearabouts.ui.base.BaseContentPage
import com.wearabouts.ui.donation.map.LocationService
import com.wearabouts.ui.donation.map.MapManager

// Material
import androidx.compose.material3.Text

// Styles
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape

// Map
import androidx.compose.runtime.*
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.LocalContext
import android.location.Location
import kotlinx.coroutines.launch
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.content.Context
import android.app.Activity

// Colors
import com.wearabouts.ui.theme.Primary
import com.wearabouts.ui.theme.Font

class Donation : BaseContentPage() {

    private fun fetchLocation(
        locationService: LocationService,
        context: Context,
        onSuccess: (Location?) -> Unit,
        onFailure: () -> Unit
    ) {
        locationService.getUserLocation(
            context,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }

    @Composable
    override fun Content() {

        val context = LocalContext.current
        val locationService = LocationService()
        val mapManager = MapManager()
        
        var userLocation by remember { mutableStateOf<Location?>(null) }
        var locationStatus by remember { mutableStateOf("Obtaining location...") }
        var hasLocationPermission by remember { mutableStateOf(false) }
        var isLocationEnabled by remember { mutableStateOf(false) }

        // Permission launcher
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            hasLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        }

        // Check and request permission if not granted
        LaunchedEffect(Unit) {
            val fineLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            val coarseLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)

            if (fineLocationPermission != PackageManager.PERMISSION_GRANTED) {
                // Request permission for precise location
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            } else {
                // Permission is already given
                hasLocationPermission = true
            }
        }

        // Fetch location when permission is granted
        LaunchedEffect(hasLocationPermission) {
            if (hasLocationPermission) {
                // Check if location settings are enabled
                locationService.requestLocationSettings(context, onSuccess = {
                    isLocationEnabled = true
                }, onFailure = {
                    locationStatus = "Failed to request the user to enable location"
                })
                isLocationEnabled = locationService.isLocationEnabled(context)
            } else {
                locationStatus = "Location permission denied"
            }
        }

        // Fetch location when location settings are enabled
        LaunchedEffect(isLocationEnabled) {
            if (isLocationEnabled) {
                fetchLocation(locationService, context, onSuccess = { location ->
                    userLocation = location
                    locationStatus = "Location: long -> ${userLocation!!.longitude} | lat -> ${userLocation!!.latitude}"
                }, onFailure = {
                    locationStatus = "Failed to obtain location"
                })
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top=16.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (userLocation != null) {

                Column(

                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 50.dp, bottom = 50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {

                    Box(
                        modifier = Modifier
                            .width(330.dp)
                            .height(80.dp)
                            .padding(16.dp)
                            .clip(RoundedCornerShape(60.dp))
                            .background(Primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Search here for donation places",
                            color = Font
                        )
                    }

                    mapManager.showMap(userLocation!!.longitude, userLocation!!.latitude)

                }

            } else {
                Text(locationStatus)
            }
        }
    }
}