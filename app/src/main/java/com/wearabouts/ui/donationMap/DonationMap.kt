package com.wearabouts.ui.donationMap

// Pop-ups
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.lang.Thread

// View model
import androidx.lifecycle.viewmodel.compose.viewModel

// Data fetch
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

// To ask user to change a setting
import android.content.Intent
import android.provider.Settings
import android.net.Uri

// Debugging
import android.util.Log

// Composables
import com.wearabouts.ui.base.BaseContentPage
import com.wearabouts.ui.donationMap.map.LocationService
import com.wearabouts.ui.donationMap.map.MapManager
import com.wearabouts.ui.donationMap.slideshow.Slideshow

// Material
import androidx.compose.material3.Text

// Styles & resources
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import com.wearabouts.R
import androidx.compose.ui.res.painterResource

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
import androidx.compose.ui.graphics.Color
import com.wearabouts.ui.theme.IconColor
import com.wearabouts.ui.theme.Primary
import com.wearabouts.ui.theme.Font
import com.wearabouts.ui.theme.White
import com.wearabouts.ui.theme.Transparent

class DonationMap() : BaseContentPage() {

    @Composable
    override fun Content(modifier: Modifier) {

        val TAG = "donation"

        // Initialize the ViewModel and collect donationPlaces
        val donationViewModel: DonationMapViewModel = viewModel()
        val donationPlaces by donationViewModel.donationPlaces.collectAsState()

        val context = LocalContext.current
        val locationService = LocationService()
        val mapManager = MapManager()
        val slideshow = Slideshow()
        
        var userLocation by remember { mutableStateOf<Location?>(null) }
        var hasLocationPermission by remember { mutableStateOf(false) }
        var isLocationEnabled = locationService.isLocationEnabled(context)

        // Check and request permission if not granted
        LaunchedEffect(Unit) {
            val fineLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            val coarseLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)

            if (fineLocationPermission != PackageManager.PERMISSION_GRANTED && coarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
                // Request permission for precise location
                Log.d(TAG, "Needs permission permission, taking user to home! ")
                // Create an AlertDialog
                AlertDialog.Builder(context)
                    .setMessage("We need the location permission (either precise or approximate) to display a map with nearby donation places")
                    .setPositiveButton("Go to settings") { dialog, _ ->
                        // Navigate to app settings
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        dialog.dismiss()
                        navigate("home")
                        context.startActivity(intent)
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                        navigate("home")
                    }
                    .show()
            } else if (!isLocationEnabled) {
                // Request permission to turn onlocation
                Log.d(TAG, "Location not enabled according to locationservice, taking user to home!")
                Toast.makeText(context, "We need the location to be activated", Toast.LENGTH_LONG).show()
                navigate("home")
            } else {
                Log.d(TAG, "Permission already granted!")
                // Permission is already given
                hasLocationPermission = true
            }
        }

        // Fetch location when permission is granted
        LaunchedEffect(hasLocationPermission) {
            Log.d(TAG, "LaunchedEffect(haslocationpermission): Checking location permission")
            isLocationEnabled = locationService.isLocationEnabled(context)

            if (hasLocationPermission) {
                Log.d(TAG, "LaunchedEffect(haslocationpermission): Location permission granted and its turned on, getting location only")
                // Check if location settings are enabled
                locationService.getLocationOnly(
                    context,
                    onSuccess = { location ->
                        userLocation = location
                    },
                    onFailure = {
                        // Show a Toast message
                        Toast.makeText(context, "Location not received", Toast.LENGTH_LONG).show()
                    }
                )
            }
        }

        // Sort donation places based on distance
        val sortedDonationPlaces by remember(userLocation) {
            derivedStateOf {
                userLocation?.let { userLoc ->
                    donationPlaces.sortedBy {
                        mapManager.calculateDistance(
                            userLoc.latitude,
                            userLoc.longitude,
                            it.latitude,
                            it.longitude
                        )
                    }
                } ?: donationPlaces
            }
        }

        // Get closest donation place
        val closestDonationPlace = sortedDonationPlaces.firstOrNull()

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

                    // Show map centered on closest donation place
                    closestDonationPlace?.let {
                        Log.d(TAG, "Showing map centered on closest donation place")
                        //mapManager.showMap(it.longitude, it.latitude, mapManager, sortedDonationPlaces)
                    }

                    //mapManager.showMap(userLocation!!.longitude, userLocation!!.latitude, mapManager, sortedDonationPlaces)
                }

                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(550.dp),
                    contentAlignment = Alignment.Center
                ) {
                    slideshow.display(mapManager, sortedDonationPlaces)
                }

                // Go back to current location button
                var goBackToCurrentLocationHeight = 220.dp
                if (donationPlaces.isEmpty()) {
                    goBackToCurrentLocationHeight = 500.dp
                }
                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Transparent)
                        .height(goBackToCurrentLocationHeight),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 20.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Box (
                            modifier = Modifier
                                .size(45.dp)
                                .clip(RoundedCornerShape(45.dp))
                                .background(White),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                onClick = {
                                    Log.d(TAG, "Clicked on current location button")
                                    try {
                                        mapManager.moveCameraToLocation(userLocation!!.longitude, userLocation!!.latitude)
                                    } catch (e: Exception) {
                                        Log.d(TAG, "Error in moving camera to current location: ${e.message}")
                                        Toast.makeText(context, "Error in moving camera to current location", Toast.LENGTH_LONG).show()
                                    }
                                },
                                modifier = Modifier
                                    .size(45.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.location),
                                    contentDescription = "Go back to current location",
                                    modifier = Modifier.size(20.dp),
                                    tint = Color.Black
                                )

                                // Fly to closest donation place
                                //mapManager.moveCameraToLocation(sortedDonationPlaces[0].longitude, sortedDonationPlaces[0].latitude)
                            }
                        }
                    }
                }

            } else {
                Text("Obtaining location...")
            }
        }
    }
}