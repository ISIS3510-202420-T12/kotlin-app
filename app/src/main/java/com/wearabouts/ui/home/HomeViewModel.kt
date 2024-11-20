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

// Local storage
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.app.Application
import androidx.lifecycle.AndroidViewModel

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    // Local storage fetch
    private val _favorites = MutableStateFlow<List<ClothingItem>>(emptyList())
    val favorites: StateFlow<List<ClothingItem>> = _favorites
    private val sharedPreferences: SharedPreferences = application.getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val FAVORITES_KEY = "favorite_clothing_items"

    // Firestore fetch
    private val _filteredClothingItems = MutableStateFlow<List<ClothingItem>>(emptyList())
    val filteredClothingItems: StateFlow<List<ClothingItem>> = _filteredClothingItems
    private val _clothingItems = MutableStateFlow<List<ClothingItem>>(emptyList())
    val clothingItems: StateFlow<List<ClothingItem>> = _clothingItems
    val _labels = MutableStateFlow<List<String>>(emptyList())
    val labels: StateFlow<List<String>> = _labels

    val TAG: String = "ClothingFetch"
    val FILTERTAG: String = "ClothingFilter"

    val db = Firebase.firestore

    init {
        fetchClothingItems()
        // Initialize local storage
        viewModelScope.launch {
            _favorites.value = getFavoriteClothingItems()
        }
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
                _labels.value = items.flatMap { it.labels }.distinct()
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
            .addOnCompleteListener {
                Log.d(TAG, "Finished fetching of campaings")
            }
    }

    fun filterItems(category: String) {
        _filteredClothingItems.value = _clothingItems.value.filter { it.labels.contains(category) }
        db.collection("FilterLog")
            .whereEqualTo("name", category)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // Create new document if it doesn't exist
                    val newLog = hashMapOf(
                        "name" to category,
                        "clicked" to 1,
                        "timesSold" to 0
                    )
                    db.collection("FilterLog").add(newLog)
                } else {
                    // Update existing document
                    for (document in documents) {
                        val currentClicked = document.getLong("clicked") ?: 0
                        db.collection("FilterLog").document(document.id)
                            .update("clicked", currentClicked + 1)
                    }
                }
                Toast.makeText(getApplication(), "Filtering by $category!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(getApplication(), "Couldn't filter by this category :(", Toast.LENGTH_SHORT).show()
                Log.w(FILTERTAG, "Error updating FilterLog", exception)
            }
    }

    fun resetFilter() {
        _filteredClothingItems.value = _clothingItems.value
    }

    fun getItemById(id: String): ClothingItem? {
        return _clothingItems.value.find { it.id == id }
    }



    // ~ ~ ~ ~ Local storage ~ ~ ~ ~ //



    // Local storage functions for favourites

    fun getFavoriteClothingItems(): List<ClothingItem> {
        val json = sharedPreferences.getString(FAVORITES_KEY, null)
        return if (json != null) {
            val type = object : TypeToken<List<ClothingItem>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    private fun saveFavorites(favorites: List<ClothingItem>) {
        val editor = sharedPreferences.edit()
        val json = gson.toJson(favorites)
        editor.putString(FAVORITES_KEY, json)
        editor.apply()

        // Update the StateFlow
        _favorites.value = favorites
    }

    fun addFavorite(item: ClothingItem) {
        val currentFavorites = _favorites.value.toMutableList()
        currentFavorites.add(item)
        saveFavorites(currentFavorites)
    }

    fun removeFavorite(item: ClothingItem) {
        val currentFavorites = _favorites.value.toMutableList()
        currentFavorites.removeAll { it.id == item.id }
        saveFavorites(currentFavorites)
    }

    fun isFavorite(clothingItem: ClothingItem): Boolean {
        return _favorites.value.any { it.id == clothingItem.id }
    }

    // High level functions
    fun toggleFav(clothingItem: ClothingItem) {
        if (isFavorite(clothingItem)) {
            removeFavorite(clothingItem)
        } else {
            addFavorite(clothingItem)
        }
    }

    fun buyItem(clothingItem: ClothingItem) {
        // For each label
        for (label in clothingItem.labels) {
            db.collection("FilterLog")
                .whereEqualTo("name", label)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        // Create new document if it doesn't exist
                        val newLog = hashMapOf(
                            "name" to label,
                            "clicked" to 0,
                            "timesSold" to 1
                        )
                        db.collection("FilterLog").add(newLog)
                    } else {
                        // Update existing document
                        for (document in documents) {
                            val currentTimesSold = document.getLong("timesSold") ?: 0
                            db.collection("FilterLog").document(document.id)
                                .update("timesSold", currentTimesSold + 1)
                        }
                    }
                    Toast.makeText(getApplication(), "Purchase successful!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(getApplication(), "Purchase not successful :(", Toast.LENGTH_SHORT).show()
                    Log.w(TAG, "Error updating FilterLog", exception)
                }
        }
    }
}
