package com.wearabouts.ui.donation.view

// Composables
import com.wearabouts.ui.donation.view.DonationPlaceCard

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

// Colors
import com.wearabouts.ui.theme.IconColor
import com.wearabouts.ui.theme.Primary

class Slideshow {

    var donationPlaces: List<DonationPlace> = emptyList()

    // Create 5 donation places
    init {
        donationPlaces = listOf(
            DonationPlace(1, "Goodwill", "https://plus.unsplash.com/premium_photo-1661915661139-5b6a4e4a6fcc?q=80&w=1934&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", 37.7749, -122.4194, "Goodwill Industries International, Inc. is a nonprofit organization that provides job training, employment placement services, and other community-based programs for people who have barriers preventing them from otherwise obtaining a job."),
            DonationPlace(2, "Salvation Army", "https://images.unsplash.com/photo-1504615755583-2916b52192a3?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", 37.7749, -122.4194, "The Salvation Army is a Protestant Christian church and an international charitable organization. The organization reports a worldwide membership of over 1.7 million, consisting of soldiers, officers and adherents collectively known as Salvationists."),
            DonationPlace(3, "Habitat for Humanity", "https://images.unsplash.com/photo-1572120360610-d971b9d7767c?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", 37.7749, -122.4194, "Habitat for Humanity International, generally referred to as Habitat for Humanity or simply Habitat, is an international, non-governmental, and nonprofit organization, which was founded in 1976."),
            DonationPlace(4, "American Red Cross", "https://plus.unsplash.com/premium_photo-1661962841993-99a07c27c9f4?q=80&w=1931&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", 37.7749, -122.4194, "The American Red Cross (ARC), also known as The American National Red Cross, is a humanitarian organization that provides emergency assistance, disaster relief, and education inside the United States."),
            DonationPlace(5, "Feeding America", "https://plus.unsplash.com/premium_photo-1661908377130-772731de98f6?q=80&w=2012&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", 37.7749, -122.4194, "Feeding America is a United States–based nonprofit organization that is a nationwide network of more than 200 food banks that feed more than 46 million people through food pantries, soup kitchens, shelters, and other community-based agencies.")
        )
    }

    @Composable
    fun display() {
        LazyRow(
            modifier = Modifier
                .fillMaxSize()
                .background(Primary)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
            // flingBehavior = ScrollableDefaults.flingBehavior(),
            // userScrollEnabled = true
        ) {
            var length = donationPlaces.size
            items(length) { index ->
                DonationPlaceCard(donationPlaces[index])
            }
        }
    }
}