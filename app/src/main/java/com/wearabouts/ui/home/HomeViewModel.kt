package com.wearabouts.ui.home

// Data state
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Data model
import com.wearabouts.models.ClothingItem

// Location request
import com.wearabouts.ui.donation.map.LocationService
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
import androidx.compose.runtime.Composable

// Pop-ups
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class HomeViewModel : ViewModel() {

    private val _clothingItems = MutableStateFlow<List<ClothingItem>>(emptyList())
    val clothingItems: StateFlow<List<ClothingItem>> = _clothingItems

    init {
        fetchClothingItems()
    }

    @Composable
    fun getLocation() {
        val context = LocalContext.current
        val locationService = LocationService()
        var hasLocationPermission by remember { mutableStateOf(false) }

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

            if (fineLocationPermission != PackageManager.PERMISSION_GRANTED && coarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
                // Request permission for precise location
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
                locationService.getUserLocation(context, onSuccess = { location -> }, onFailure = {})
                // Create Toast
                Toast.makeText(context, "Location permission is required to show nearby donation places", Toast.LENGTH_LONG).show()
                hasLocationPermission = false
            } else {
                // Permission is already given
                hasLocationPermission = true
            }
        }

        // Fetch location when permission is granted
        LaunchedEffect(hasLocationPermission) {
            val fineLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            val coarseLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)

            if (hasLocationPermission) {
                // Check if location settings are enabled
                locationService.requestLocationSettings(context, onSuccess = {}, onFailure = {})
            } else if (fineLocationPermission == PackageManager.PERMISSION_GRANTED || coarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
                locationService.requestLocationSettings(context, onSuccess = {}, onFailure = {})
            }
        }
    }

    private fun fetchClothingItems() {
        // Simulate fetching data from a repository
        viewModelScope.launch {
            val items = listOf(
                ClothingItem(1, "T-Shirt", "https://www.therange.co.uk/media/2/5/1654518853_12_1005.jpg", 19.99),
                ClothingItem(2, "Jeans", "https://img1.exportersindia.com/product_images/bc-full/2019/1/5450192/mens-funny-look-jeans-1547451409-4644396.jpeg", 49.99),
                ClothingItem(3, "Jacket", "https://m.media-amazon.com/images/I/71zaJkhWPCL._AC_UY1000_.jpg", 89.99),
                ClothingItem(4, "Skirt", "https://m.media-amazon.com/images/I/71ZJF42-4UL._AC_SX569_.jpg", 69.99),
                ClothingItem(5, "Duck Shoes", "https://i.pinimg.com/originals/77/83/62/778362991f15bcc6211a3cd3e9e41533.jpg", 69.99),
                ClothingItem(6, "Space Pants", "https://canary.contestimg.wish.com/api/webimage/5e981c690ca0dc55df360cfd-2-large.jpg", 69.99),
                ClothingItem(7, "Skirt", "https://m.media-amazon.com/images/I/71ZJF42-4UL._AC_SX569_.jpg", 69.99)



            )
            _clothingItems.value = items
        }
    }
}