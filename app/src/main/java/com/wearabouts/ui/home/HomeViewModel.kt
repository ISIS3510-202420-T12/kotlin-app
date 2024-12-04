package com.wearabouts.ui.home

// Firestore
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase

// Data state
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Data model
import com.wearabouts.models.Clothe

// Location request
import com.wearabouts.ui.donationMap.map.LocationService
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.content.Context

// Pop-ups
import android.widget.Toast

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
    private val _favorites = MutableStateFlow<List<Clothe>>(emptyList())
    val favorites: StateFlow<List<Clothe>> = _favorites
    private val sharedPreferences: SharedPreferences = application.getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val FAVORITES_KEY = "favorite_clothing_items"

    // Firestore fetch
    private val _filteredClothingItems = MutableStateFlow<List<Clothe>>(emptyList())
    val filteredClothingItems: StateFlow<List<Clothe>> = _filteredClothingItems
    private val _clothingItems = MutableStateFlow<List<Clothe>>(emptyList())
    val clothingItems: StateFlow<List<Clothe>> = _clothingItems
    val _labels = MutableStateFlow<List<String>>(emptyList())
    val labels: StateFlow<List<String>> = _labels

    val TAG: String = "ClothingFetch"
    val FILTERTAG: String = "ClothingFilter"

    val db = Firebase.firestore


    //Cart items
    private val _cartItems = mutableStateListOf<Clothe>()
    val cartItems: List<Clothe> get() = _cartItems
    private val CART_ITEMS_KEY = "cart_items"



    init {
        fetchClothingItems()
        // Initialize local storage
        viewModelScope.launch {
            _favorites.value = getFavoriteClothingItems()
            _cartItems.addAll(fetchCartItemsFromStorage())
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
        db.collection("CleanClothes")
            .get()
            .addOnSuccessListener { result ->
                Log.d(TAG, "Success: fetched ${result.size()} documents")
                val items = result.mapNotNull { document ->
                    Log.d(TAG, "${document.id} => ${document.data}")
                    try {
                        document.toObject(Clothe::class.java)
                        var clothingItem = document.toObject(Clothe::class.java)
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
        val TAGHOME = "Home"
        Log.d(TAGHOME, "Filtering items by $category")
        val filteredItems = _clothingItems.value.filter { it.labels.contains(category) }
        Log.d(TAGHOME, "Filtered items: $filteredItems")
        try {
            _filteredClothingItems.value = filteredItems
        } catch (e: Exception) {
            Log.e(TAGHOME, "Error filtering items: ${e.message}")
        }

        try {
            logLabelFilter(category)
        } catch (e: Exception) {
            Log.e(FILTERTAG, "Error logging filter: ${e.message}")
        }
    }

    private fun logLabelFilter (label: String) {
        db.collection("Labels")
            .whereEqualTo("name", label)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // Create new document if it doesn't exist
                    val newLog = hashMapOf(
                        "name" to label,
                        "clicked" to 1,
                        "timesSold" to 0
                    )
                    db.collection("Labels").add(newLog)
                } else {
                    // Update existing document
                    for (document in documents) {
                        val currentClicked = document.getLong("clicked") ?: 0
                        db.collection("Labels").document(document.id)
                            .update("clicked", currentClicked + 1)
                    }
                }
                Toast.makeText(getApplication(), "Filtering by $label!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(getApplication(), "Couldn't filter by this category :(", Toast.LENGTH_SHORT).show()
                Log.w(FILTERTAG, "Error updating FilterLog", exception)
            }
    }

    fun resetFilter() {
        _filteredClothingItems.value = _clothingItems.value
    }

    fun getItemById(id: String): Clothe? {
        return _clothingItems.value.find { it.id == id }
    }

    // Cart functions
    fun addToCart(item: Clothe) {
        _cartItems.add(item)
        saveCartItems(_cartItems)
        Log.d(TAG, "Added item to cart: $item")
    }



    // ~ ~ ~ ~ Local storage ~ ~ ~ ~ //



    // Local storage functions for favourites

    private fun getFavoriteClothingItems(): List<Clothe> {
        val json = sharedPreferences.getString(FAVORITES_KEY, null)
        return if (json != null) {
            val type = object : TypeToken<List<Clothe>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    private fun saveFavorites(favorites: List<Clothe>) {
        val editor = sharedPreferences.edit()
        val json = gson.toJson(favorites)
        editor.putString(FAVORITES_KEY, json)
        editor.apply()

        // Update the StateFlow
        _favorites.value = favorites
    }

    private fun addFavorite(item: Clothe) {
        val currentFavorites = _favorites.value.toMutableList()
        currentFavorites.add(item)
        saveFavorites(currentFavorites)
    }

    private fun removeFavorite(item: Clothe) {
        val currentFavorites = _favorites.value.toMutableList()
        currentFavorites.removeAll { it.id == item.id }
        saveFavorites(currentFavorites)
    }

    fun isFavorite(clothingItem: Clothe): Boolean {
        return _favorites.value.any { it.id == clothingItem.id }
    }

    // High level functions
    fun toggleFav(clothingItem: Clothe) {
        if (isFavorite(clothingItem)) {
            removeFavorite(clothingItem)
        } else {
            addFavorite(clothingItem)
        }
    }

    fun buyItem(clothingItem: Clothe) {
        // For each label
        for (label in clothingItem.labels) {
            try {
                logLabelPurchase(label)
            } catch (e: Exception) {
                Log.e(TAG, "Error logging purchase: ${e.message}")
            }
        }
    }

    private fun logLabelPurchase (label: String) {
        db.collection("Labels")
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
                    db.collection("Labels").add(newLog)
                } else {
                    // Update existing document
                    for (document in documents) {
                        val currentTimesSold = document.getLong("timesSold") ?: 0
                        db.collection("Labels").document(document.id)
                            .update("timesSold", currentTimesSold + 1)
                    }
                }
                //Toast.makeText(getApplication(), "Purchase successful!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(getApplication(), "Purchase not successful :(", Toast.LENGTH_SHORT).show()
                Log.w(TAG, "Error updating Labels", exception)
            }
    }

    // Storage functions for cart items
    private fun saveCartItems(cartItems: List<Clothe>) {
        val editor = sharedPreferences.edit()
        val json = gson.toJson(cartItems)
        editor.putString(CART_ITEMS_KEY, json)
        editor.apply()
    }

    private fun fetchCartItemsFromStorage(): List<Clothe> {
        val json = sharedPreferences.getString(CART_ITEMS_KEY, null)
        return if (json != null) {
            val type = object : TypeToken<List<Clothe>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

}
