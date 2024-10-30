package com.wearabouts.ui.donation

// Log
import android.util.Log

// Firestore
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase

// View model
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

// Model
import com.wearabouts.models.DonationPlace

// Data state
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DonationViewModel : ViewModel() {
    private val _donationPlaces = MutableStateFlow<List<DonationPlace>>(emptyList())
    val donationPlaces: StateFlow<List<DonationPlace>> = _donationPlaces
    val TAG: String = "DonationFetch"

    val db = Firebase.firestore

    init {
        fetchDonationPlaces()
    }

    private fun fetchDonationPlaces() {
        Log.d(TAG, "Starting fetching of donation places")
        db.collection("DonationPlaces")
            .get()
            .addOnSuccessListener { result ->
                Log.d(TAG, "Success: fetched ${result.size()} documents")
                val places = result.map { document ->
                    Log.d(TAG, "${document.id} => ${document.data}")
                    document.toObject(DonationPlace::class.java)
                }
                _donationPlaces.value = places
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
            .addOnCompleteListener {
                Log.d(TAG, "Finished fetching of donation places")
            }
    }
}