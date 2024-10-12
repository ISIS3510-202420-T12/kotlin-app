package com.wearabouts.ui.donation

import com.wearabouts.ui.base.BaseContentPage
import com.wearabouts.ui.donation.map.LocationManager

import androidx.appcompat.app.AppCompatActivity

import androidx.compose.material3.Text

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background

import androidx.compose.runtime.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import android.location.Location

class Donation : BaseContentPage() {

    @Composable
    override fun Content() {

        val context = LocalContext.current
        val locationManager = LocationManager()
        var userLocation by remember { mutableStateOf<Location?>(null) }

        // Call the getUserLocation function
        LaunchedEffect(Unit) {
            locationManager.getUserLocation(context) { locationResult ->
                userLocation = locationResult
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top=16.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (userLocation != null) {
                Text("Location obtained")
            } else {
                Text("Obtaining location...")
            }
        }
    
    }
}