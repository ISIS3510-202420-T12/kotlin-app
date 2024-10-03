package com.wearabouts.ui.donation

import androidx.compose.material3.Text

// Important libs
import com.wearabouts.R
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background

// To round borders
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip

// Other classes
import com.wearabouts.ui.base.BaseContentPage

// MapBox
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.MapViewportState
import com.mapbox.geojson.Point

// Colors
import com.wearabouts.ui.theme.Primary
import com.wearabouts.ui.theme.Font

class Donation : BaseContentPage() {

    @Composable
    override fun Content() {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
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
            Box(
                
            ) {
                MapboxMap(
                    Modifier.fillMaxSize(),
                        mapViewportState = MapViewportState().apply {
                            setCameraOptions {
                                zoom(2.0)
                                center(Point.fromLngLat(-98.0, 39.5))
                                pitch(0.0)
                                bearing(0.0)
                            }
                    },
                )
            }
        }
    }
}