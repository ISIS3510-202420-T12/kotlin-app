package com.wearabouts.ui.donation.map

// Pop-ups
import android.widget.Toast

// Debugging
import android.util.Log

// Styles & layout
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Brush
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
import com.wearabouts.R
import androidx.compose.ui.res.painterResource

// Models
import com.wearabouts.models.DonationPlace

// Map logic
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.attribution.generated.AttributionSettings
import com.mapbox.maps.plugin.compass.generated.CompassSettings
import com.mapbox.maps.plugin.gestures.generated.GesturesSettings
import com.mapbox.maps.plugin.scalebar.generated.ScaleBarSettings
import com.mapbox.maps.plugin.compass.compass
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.scalebar.scalebar
import kotlinx.coroutines.delay

// User location pin
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.viewport.data.FollowPuckViewportStateOptions

// Custom pins
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import androidx.core.content.res.ResourcesCompat
import android.content.res.Resources
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import android.graphics.Bitmap
// import com.mapbox.maps.extension.compose.annotation.rememberIconImage // Requires MapBox >=11.7.0

// Colors
import com.wearabouts.ui.theme.Primary
import com.wearabouts.ui.theme.Font

class MapManager() {
    var mapViewportState: MapViewportState? = null

    @OptIn(MapboxExperimental::class)
    @Composable
    fun AddPointer(point:Point) {
        val context = LocalContext.current
        val drawable = ResourcesCompat.getDrawable(
            context.resources,
            R.drawable.location_pin,
            null
        )
        val bitmap = drawable!!.toBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        PointAnnotation(
            iconImageBitmap = bitmap,
            iconSize = 0.5,
            point = point,
            onClick = {
                //Toast.makeText(context, "Clicked on Circle Annotation: $it", Toast.LENGTH_LONG).show()
                true
            }
        )
    }

    fun moveCameraToLocation(long: Double, lat: Double) {
        mapViewportState?.flyTo(
                cameraOptions = cameraOptions {
                    center(Point.fromLngLat(long, lat))
                    zoom(15.0)
                },
                animationOptions = MapAnimationOptions.mapAnimationOptions { duration(5000) },
        )
    }

    @OptIn(MapboxExperimental::class)
    @Composable
    fun showMap(long: Double, lat: Double, mapManager: MapManager, donationPlaces: List<DonationPlace>) {
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
            mutableStateOf(CompassSettings { enabled = true })
        }

        val scaleBarSetting: ScaleBarSettings by remember {
            mutableStateOf(ScaleBarSettings { enabled = false })
        }

        // Provide the mapViewportState to MapManager
        mapManager.mapViewportState = mapViewportState

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            MapboxMap(
                modifier = Modifier
                    .height(600.dp)
                    .fillMaxWidth(),
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
            ) {

                var locationPoint = Point.fromLngLat(long, lat) 
                // Donation places pins
                donationPlaces.forEach { donationPlace ->
                    val point = Point.fromLngLat(donationPlace.longitude, donationPlace.latitude)
                    AddPointer(point)
                }

                MapEffect(Unit) { mapView ->
                    mapView.location.updateSettings {
                      locationPuck = createDefault2DPuck(withBearing = true)
                      enabled = true
                      puckBearing = PuckBearing.COURSE
                      puckBearingEnabled = true
                    }
                    mapViewportState.transitionToFollowPuckState(
                        followPuckViewportStateOptions = FollowPuckViewportStateOptions.Builder().build()
                    )
                }
            }

            // Top fade effect
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.White, Color.Transparent)
                        )
                    )
            )

            // Bottom fade effect
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .offset(y = 550.dp) // Adjust the offset to match the height of the map
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.White)
                        )
                    )
            )

        }
    }
}