package com.wearabouts.ui.home

// Firestore
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase

// Data state
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Data model
import com.wearabouts.models.ClothingItem

// Location request
import com.wearabouts.ui.donationMap.map.LocationService
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import android.location.Location
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.content.Context
import android.app.Activity

// Pop-ups
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

// Log
import android.util.Log

class HomeViewModel : ViewModel() {

    private val _filteredClothingItems = MutableStateFlow<List<ClothingItem>>(emptyList())
    val filteredClothingItems: StateFlow<List<ClothingItem>> = _filteredClothingItems

    // Firestore fetch
    private val _clothingItems = MutableStateFlow<List<ClothingItem>>(emptyList())
    val clothingItems: StateFlow<List<ClothingItem>> = _clothingItems
    val TAG: String = "ClothingFetch"

    val db = Firebase.firestore

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
        Log.d(TAG, "Starting fetching of clothing items")
        db.collection("Clothes")
            .get()
            .addOnSuccessListener { result ->
                Log.d(TAG, "Success: fetched ${result.size()} documents")
                val items = result.mapNotNull { document ->
                    Log.d(TAG, "${document.id} => ${document.data}")
                    try {
                        document.toObject(ClothingItem::class.java)
                        var clothingItem = document.toObject(ClothingItem::class.java)
                        Log.d(TAG, "Clothing item: $clothingItem")
                        clothingItem.id = document.id
                        Log.d(TAG, "Clothing item with id: $clothingItem")
                        clothingItem
                    } catch (e: Exception) {
                        Log.e(TAG, "Error parsing document", e)
                        null
                    }
                }
                _clothingItems.value = items
                _filteredClothingItems.value = items
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
            .addOnCompleteListener {
                Log.d(TAG, "Finished fetching of campaings")
            }
    }

    fun filterItems(category: String) {
        //_filteredClothingItems.value = _clothingItems.value.filter { it.category == category }
        _filteredClothingItems.value = _clothingItems.value
    }

    fun resetFilter() {
        _filteredClothingItems.value = _clothingItems.value
    }

    fun getItemById(id: String): ClothingItem? {
        return _clothingItems.value.find { it.id == id }
    }
}
