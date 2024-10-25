package com.wearabouts.ui.donation.view

// Pop-ups
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.lang.Thread

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
        var hasLocationPermission by remember { mutableStateOf(false) }
        var isLocationEnabled = locationService.isLocationEnabled(context)

        // Permission launcher
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            hasLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            locationService.getUserLocation(context, onSuccess = { location -> }, onFailure = {})

        }

        // Check and request permission if not granted
        LaunchedEffect(Unit) {
            val fineLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            val coarseLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)

            if (fineLocationPermission != PackageManager.PERMISSION_GRANTED && coarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
                // Request permission for precise location
                Toast.makeText(context, "Location permission is needed", Toast.LENGTH_LONG).show()
                
                // Create an AlertDialog
                AlertDialog.Builder(context)
                    .setMessage("We need the location permission (either precise or approximate) to display a map with nearby donation places")
                    .setPositiveButton("OK") { _, _ ->
                        navigate("home")
                    }
                    .show()
                Log.d("Donation", "Permission ")
            } else if (!isLocationEnabled) {
                Log.d("Donation", "Location not enabled according to locationservice")
                // Request permission to turn onlocation
                // Show a Toast message
                Toast.makeText(context, "We need the location to be activated", Toast.LENGTH_LONG).show()
                
                // Create an AlertDialog
                AlertDialog.Builder(context)
                    .setMessage("We need the location to be activated")
                    .setPositiveButton("OK") { _, _ ->
                        navigate("home")
                    }
                    .show()
            } else {
                Log.d("Donation", "Permission already granted")
                // Permission is already given
                hasLocationPermission = true
            }
        }

        // Fetch location when permission is granted
        LaunchedEffect(hasLocationPermission) {
            Log.d("Donation", "LaunchedEffect(haslocationpermission): Checking location permission")
            isLocationEnabled = locationService.isLocationEnabled(context)
            if (hasLocationPermission && isLocationEnabled) {
                Log.d("Donation", "LaunchedEffect(haslocationpermission): Location permission granted and its turned on, getting location only")
                // Check if location settings are enabled
                locationService.getLocationOnly(
                    context,
                    onSuccess = { location ->
                        Log.d("Donation", "LaunchedEffect(haslocationpermission): Location received, location = $location")
                        userLocation = location
                    },
                    onFailure = {
                        Log.d("Donation", "LaunchedEffect(haslocationpermission): Location not received")
                        // Show a Toast message
                        Toast.makeText(context, "Location not received", Toast.LENGTH_LONG).show()
                    }
                )
                // navigate("home")
                // Thread.sleep(1000)
                // navigate("donation")
            } else {
                Log.d("Donation", "LaunchedEffect(haslocationpermission): Location permission denied or location not enabled")
                // Show a Toast message
                Toast.makeText(context, "Location permission is needed", Toast.LENGTH_LONG).show()
                
                // Create an AlertDialog
                AlertDialog.Builder(context)
                    .setMessage("We need the location permission (either precise or approximate) to display a map with nearby donation places")
                    .setPositiveButton("OK") { _, _ ->
                        navigate("home")
                    }
                    .show()
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
                Text("Obtaining location...")
            }
        }
    }
}