package com.wearabouts.ui.donation

// Composables
import androidx.compose.runtime.*
import com.wearabouts.ui.base.BaseContentPage
import com.wearabouts.ui.donation.CampaingCard

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
import androidx.compose.ui.platform.LocalContext

// Type
import com.wearabouts.ui.theme.Typography

// Model
import com.wearabouts.models.Campaing

// Network
import com.wearabouts.utils.NetworkUtils
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import kotlinx.coroutines.delay

class Donation : BaseContentPage() {

    @Composable
    override fun Content(modifier: Modifier) {
        val LOG_TAG = "CampaignFetch"

        // Initialize the ViewModel and collect donationPlaces
        val donationViewModel: DonationViewModel = viewModel()
        val campaings by donationViewModel.campaings.collectAsState()

        // State for selected campaign
        var selectedCampaign by remember { mutableStateOf<Campaing?>(null) }
        val context = LocalContext.current

        // Connectivity check
        var isConnected by remember { mutableStateOf(NetworkUtils.isInternetAvailable(context)) }

        // Effect to update connectivity status
        LaunchedEffect(Unit) {
            while(true) {
                isConnected = NetworkUtils.isInternetAvailable(context)
                delay(5000)
            }
        }

        // Style vars
        val navbarWidth = 320.dp

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Text block of impact
            Column (
                modifier = Modifier
                    .width(navbarWidth)
                    .clip(RoundedCornerShape(25.dp))
                    .background(Primary),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Clothes donated text
                Row (
                    modifier = Modifier
                        .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 0.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // First part
                    Text (
                        text = "You have donated",
                        style = Typography.titleMedium,
                        color = Font
                    )
                    // Highlighted number
                    Box (
                        modifier = Modifier
                            .size(50.dp)
                            .padding(10.dp)
                            .clip(RoundedCornerShape(60.dp))
                            .background(Font),
                        contentAlignment = Alignment.Center
                    ) {
                        Text (
                            text = "56",
                            style = Typography.titleMedium,
                        )
                    }
                    // Second part
                    Text (
                        text = "clothes.",
                        style = Typography.titleMedium,
                        color = Font
                    )
                }
                // People helped text
                Row (
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // First part
                    Text (
                        text = "And you've helped",
                        style = Typography.titleMedium,
                        color = Font
                    )
                    // Highlighted number
                    Box (
                        modifier = Modifier
                            .size(50.dp)
                            .padding(10.dp)
                            .clip(RoundedCornerShape(60.dp))
                            .background(Font),
                        contentAlignment = Alignment.Center
                    ) {
                        Text (
                            text = "120",
                            style = Typography.titleMedium,
                        )
                    }
                    // Second part
                    Text (
                        text = "people.",
                        style = Typography.titleMedium,
                        color = Font
                    )
                }
            }

            // Search bar and map view button
            Row (
                modifier = Modifier
                    .width(navbarWidth)
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val localHeight = 50.dp
                // Search bar
                Row (
                    modifier = Modifier
                        .height(localHeight)
                        .width(navbarWidth - 85.dp)
                        .clip(RoundedCornerShape(1000.dp))
                        .background(Primary),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Search icon
                    Box (
                        modifier = Modifier.padding(start = 30.dp)
                    ) {
                        Icon (
                            painter = painterResource(id = R.drawable.search),
                            contentDescription = "Search",
                            tint = Font,
                            modifier = Modifier
                                .size(20.dp)
                        )
                    }
                    // Search bar text
                    Text (
                        text = "Search here for NGOs",
                        style = Typography.titleMedium,
                        color = Font,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                // Map view button, onClick = navigate("donationMap")
                IconButton (
                    onClick = { navigate("donationMap") },
                    modifier = Modifier
                        .size(localHeight)
                        //.padding(10.dp)
                        .clip(RoundedCornerShape(10000.dp))
                        .background(Primary)
                ) {
                    // Map icon
                    Icon (
                        painter = painterResource(id = R.drawable.donation_map),
                        contentDescription = "Map view",
                        tint = Font,
                        modifier = Modifier.size(30.dp)
                    )
                }   
            }

            // Campaings cards
            Log.d(LOG_TAG, "Campaings: $campaings")
            if (!campaings.isEmpty()) {
                Log.d(LOG_TAG, "Campaings is not null, loading lazy column")
            }
            
            if (!isConnected) {
                // Mensaje de no conexión
                Column(
                    modifier = Modifier
                        .width(navbarWidth)
                        .padding(top = 20.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .background(Primary)
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.WifiOff,
                        contentDescription = "No internet connection",
                        tint = Font,
                        modifier = Modifier
                            .size(50.dp)
                            .padding(bottom = 10.dp)
                    )
                    
                    Text(
                        text = "No Internet Connection",
                        style = Typography.titleLarge,
                        color = Font,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Text(
                        text = "Please check your internet connection and try again to view available campaigns",
                        style = Typography.bodyMedium,
                        color = Font,
                        textAlign = TextAlign.Center
                    )
                }
            } else if (!campaings.isEmpty()) {
                // Tu código existente para mostrar campañas
                val sortedCampaigns = campaings.sortedBy { 
                    if (it.goal > 0) it.progress / it.goal else 0.0 
                }
                
                LazyColumn(
                    modifier = Modifier
                        .width(navbarWidth)
                        .padding(top = 20.dp)
                ) {
                    items(sortedCampaigns.size) { index ->
                        CampaingCard(sortedCampaigns[index], onClick = { selectedCampaign = sortedCampaigns[index] })
                    }
                }
            }
            

            // Show DonateDialog if a campaign is selected
            selectedCampaign?.let { campaign ->
                DonateDialog(
                    campaign = campaign,
                    onDismiss = { selectedCampaign = null },
                    onDonate = {
                        donationViewModel.donateToCampaign(campaign, context)
                        selectedCampaign = null
                    }
                )
            }
        }
    }
}