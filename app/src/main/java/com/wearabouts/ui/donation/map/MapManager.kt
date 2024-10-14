package com.wearabouts.ui.donation.map

import androidx.compose.material3.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.mapbox.geojson.Point
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.attribution.generated.AttributionSettings
import com.mapbox.maps.plugin.compass.generated.CompassSettings
import com.mapbox.maps.plugin.gestures.generated.GesturesSettings
import com.mapbox.maps.plugin.logo.generated.LogoSettings
import com.mapbox.maps.plugin.scalebar.generated.ScaleBarSettings
import kotlinx.coroutines.delay

// Colors
import com.wearabouts.ui.theme.Primary
import com.wearabouts.ui.theme.Font

class MapManager {

    @OptIn(MapboxExperimental::class)
    @Composable
    fun showMap(long: Double, lat: Double) {
        // 77.594566, 12.971599

        val mapViewportState = rememberMapViewportState {
            setCameraOptions {
                center(Point.fromLngLat(long, lat))
                zoom(1.0)
                pitch(0.0)
            }
        }

        LaunchedEffect(Unit) {
            delay(200)
            mapViewportState.flyTo(
                cameraOptions = cameraOptions {
                    center(Point.fromLngLat(long, lat))
                    zoom(10.0)
                },
                animationOptions = MapAnimationOptions.mapAnimationOptions { duration(5000) },
            )
        }
    
        val mapBoxUiSettings: GesturesSettings by remember {
            mutableStateOf(GesturesSettings {
                rotateEnabled = true
                pinchToZoomEnabled = true
                pitchEnabled = true
            })
        }
    
        val compassSettings: CompassSettings by remember {
            mutableStateOf(CompassSettings { enabled = false })
        }
    
        val scaleBarSetting: ScaleBarSettings by remember {
            mutableStateOf(ScaleBarSettings { enabled = false })
        }

        Box() {
            MapboxMap(
                modifier = Modifier
                    .height(400.dp)
                    .width(330.dp),
                mapInitOptionsFactory = { context ->
                    MapInitOptions(
                        context = context,
                        styleUri = "mapbox://styles/mapbox/standard-satellite",
                        // Styles: "mapbox://styles/mapbox/dark-v11", "mapbox://styles/mapbox/standard", "mapbox://styles/mapbox/standard-satellite"
                    )
                },
                mapViewportState = mapViewportState,
                compassSettings = compassSettings,
                scaleBarSettings = scaleBarSetting,
                gesturesSettings = mapBoxUiSettings,
                attributionSettings = AttributionSettings {
                    enabled = false
                },
            )
        }
    }
}