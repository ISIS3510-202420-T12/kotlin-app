package com.wearabouts.ui.donation

// Components
import com.wearabouts.ui.base.BaseContentPage
import com.wearabouts.ui.donation.map.LocationManager
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

// Compose
import androidx.compose.runtime.*

// Map
// 
// import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.LocalContext
import android.location.Location
// import kotlinx.coroutines.launch
// import androidx.core.content.ContextCompat
// import android.Manifest
// import android.content.pm.PackageManager
// import androidx.activity.compose.rememberLauncherForActivityResult
// import androidx.activity.result.contract.ActivityResultContracts
// import android.content.Context

// Colors
import com.wearabouts.ui.theme.Primary
import com.wearabouts.ui.theme.Font

class Donation : BaseContentPage() {

    fun ContentView(userLocation: Location) {
        val mapManager = MapManager()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top=16.dp),
            contentAlignment = Alignment.Center,
        ) {
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
                    Text(text = "Search here for donation places", color = Font)
                }

                mapManager.showMap(userLocation!!.longitude, userLocation!!.latitude)

            }
        }
    }

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val locationManager = LocationManager()

        locationManager.request()
    }

}