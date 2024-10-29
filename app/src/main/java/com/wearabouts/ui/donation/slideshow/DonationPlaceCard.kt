package com.wearabouts.ui.donation.view

// Card & Material
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

// Styles
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

// Type
import com.wearabouts.ui.theme.Typography

// Model
import com.wearabouts.models.DonationPlace

@Composable
fun DonationPlaceCard(donationPlace: DonationPlace) {
    Card(
        colors = CardDefaults.cardColors(
            // White container color
            containerColor = Color.White,
        ),
        modifier = Modifier
            .padding(8.dp)
            .width(200.dp)
            .height(300.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = rememberImagePainter(donationPlace.imageUrl),
                contentDescription = donationPlace.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = donationPlace.name,
                style = Typography.titleMedium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = donationPlace.description,
                style = Typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}