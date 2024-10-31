package com.wearabouts.ui.donation

// Composables
import androidx.compose.runtime.Composable
import com.wearabouts.ui.base.BaseContentPage

// Debugging
import android.util.Log

// Material
import androidx.compose.material3.Text

// Styles & resources
import com.wearabouts.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.lazy.LazyColumn

// Colors
import androidx.compose.ui.graphics.Color
import com.wearabouts.ui.theme.IconColor
import com.wearabouts.ui.theme.Primary
import com.wearabouts.ui.theme.Font
import com.wearabouts.ui.theme.White
import com.wearabouts.ui.theme.Transparent

// View model
import androidx.lifecycle.viewmodel.compose.viewModel

// Data fetch
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

// Type
import com.wearabouts.ui.theme.Typography

class Donation : BaseContentPage() {

    @Composable
    override fun Content() {
        // Style vars
        val maxWidth = 420.dp

    //     Column (
    //         modifier = Modifier
    //             .width(maxWidth)
    //             .fillMaxHeight()
    //     ) {
    //         // Text block of impact
    //         Column (

    //         ) {
    //             // Clothes donated text
    //             Row (

    //             ) {
    //                 // First part
    //                 Text (

    //                 )
    //                 // Highlighted number
    //                 Box (

    //                 ) {
    //                     Text (

    //                     )
    //                 }
    //                 // Second part
    //                 Text (

    //                 )
    //             }
    //             // People helped text
    //             Row (

    //             ) {
    //                 // First part
    //                 Text (

    //                 )
    //                 // Highlighted number
    //                 Box (

    //                 ) {
    //                     Text (

    //                     )
    //                 }
    //                 // Second part
    //                 Text (

    //                 )
    //             }
    //         }

    //         // Search bar and map view button
    //         Row (

    //         ) {
    //             // Search bar
    //             Row (

    //             ) {
    //                 // Search icon
    //                 Icon (

    //                 )
    //                 // Search bar
    //                 Box (

    //                 ) {
    //                     // Search bar text
    //                     Text (

    //                     )
    //                 }
    //             }
    //             // Map view button
    //             IconButton (

    //             ) {
    //                 // Map icon, onClick = navigate("donationMap")
    //                 Icon (

    //                 )
    //             }   
    //         }

    //         // Filter buttons
    //         Row (

    //         ) {
    //             // Study icon
    //             IconButton (

    //             ) {
    //                 Icon (

    //                 )
    //             }
    //             // Medic icon
    //             IconButton (

    //             ) {
    //                 Icon (

    //                 )
    //             }
    //             // Human icon
    //             IconButton (

    //             ) {
    //                 Icon (

    //                 )
    //             }
    //             // Animals icon
    //             IconButton (

    //             ) {
    //                 Icon (

    //                 )
    //             }
    //         }

    //         // "Featured" text and See more button
    //         Row (

    //         ) {
    //             // Featured text
    //             Text (

    //             )
    //             // See more "button" (is a text for now)
    //             Text (

    //             )
    //         }

    //         // Campaings cards
    //         Column (

    //         ) {

    //         }

    //     }
    }
}