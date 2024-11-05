package com.wearabouts.ui.donation

// Log
import android.util.Log

// Pop-ups
import android.widget.Toast
import android.content.Context

// Firestore
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase

// View model
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

// Model
import com.wearabouts.models.Campaing

// Data state & coroutines
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch

class DonationViewModel : ViewModel() {

    private val _campaings = MutableStateFlow<List<Campaing>>(emptyList())
    val campaings: StateFlow<List<Campaing>> = _campaings
    val TAG: String = "CampaignFetch"

    val db = Firebase.firestore

    init {
        fetchCampaings()
    }

    private suspend fun fetchCampaingsSuspend(): List<Campaing> {
        val result = db.collection("Campaigns").get().await()
        return result.map { document ->
            Log.d(TAG, "${document.id} => ${document.data}")
            document.toObject(Campaing::class.java)
        }
    }

    private fun fetchCampaings() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Starting fetching of campaings")
                val campaings = fetchCampaingsSuspend()
                Log.d(TAG, "Success: fetched ${campaings.size} campaigns")
                _campaings.value = campaings
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching campaings", e)
            }
        }
    }

    fun donateToCampaign(campaing: Campaing, context: Context) {
        Log.d(TAG, "Donating to campaign ${campaing.name}")

        // Query to find the document with the matching campaign name
        db.collection("Campaigns")
            .whereEqualTo("Name", campaing.name)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Log.w(TAG, "No matching campaign found for ${campaing.name}")
                    Toast.makeText(context, "Campaign not found.", Toast.LENGTH_SHORT).show()
                } else {
                    val document = documents.first()
                    val newProgress = (campaing.progress + 5000).toDouble()
                    db.collection("Campaigns")
                        .document(document.id)
                        .update("Reached", newProgress)
                        .addOnSuccessListener {
                            Log.d(TAG, "DocumentSnapshot successfully updated!")
                            Toast.makeText(context, "Donation successful!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error updating document", e)
                            Toast.makeText(context, "Donation failed", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error querying campaigns.", exception)
                Toast.makeText(context, "Donation failed.", Toast.LENGTH_SHORT).show()
            }
    }

}