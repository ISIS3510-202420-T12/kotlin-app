package com.wearabouts.ui.donation.view

// Composables
import com.wearabouts.ui.donation.view.DonationPlaceCard
import com.wearabouts.ui.donation.map.MapManager

// Data model
import com.wearabouts.models.DonationPlace

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
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

// Colors
import com.wearabouts.ui.theme.IconColor
import com.wearabouts.ui.theme.Primary
import com.wearabouts.ui.theme.Transparent
import com.wearabouts.ui.theme.White

// Debugging
import android.util.Log

class Slideshow {

    var donationPlaces: List<DonationPlace> = emptyList()

    // Create 5 donation places
    init {
        donationPlaces = listOf(
            DonationPlace(1, "Goodwill", "https://plus.unsplash.com/premium_photo-1661915661139-5b6a4e4a6fcc?q=80&w=1934&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", 4.700365, -73.100032),
            DonationPlace(2, "Salvation Army", "https://images.unsplash.com/photo-1504615755583-2916b52192a3?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", 4.543865, -72.095532),
            DonationPlace(3, "Habitat for Humanity", "https://images.unsplash.com/photo-1572120360610-d971b9d7767c?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", 4.948365, -71.102032),
            DonationPlace(4, "American Red Cross", "https://plus.unsplash.com/premium_photo-1661962841993-99a07c27c9f4?q=80&w=1931&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", 4.646865, -75.097532),
            DonationPlace(5, "Feeding America", "https://plus.unsplash.com/premium_photo-1661908377130-772731de98f6?q=80&w=2012&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", 4.949365, -76.099532)
        )
    }

    @Composable
    fun display(mapManager: MapManager) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Transparent),
            contentAlignment = Alignment.BottomCenter
        ) {

            // Donation places slideshow
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Transparent)
                    .padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                var length = donationPlaces.size
                items(length) { index ->
                    val donationPlace = donationPlaces[index]
                    DonationPlaceCard(donationPlace) {
                        Log.d("DonationPlace", "Clicked on ${donationPlace.name}")
                        // On click, move the map to the donation place's location
                        try {
                            mapManager.moveCameraToLocation(donationPlace.longitude, donationPlace.latitude)
                        } catch (e: Exception) {
                            Log.e("DonationPlace", "Error moving camera to location: ${e.message}")
                        }
                    }
                }
            }
        }
    }
}