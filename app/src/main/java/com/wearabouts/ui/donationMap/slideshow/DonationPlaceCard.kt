package com.wearabouts.ui.donationMap.view

// Card & Material
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.foundation.clickable

// Styles
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
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
fun DonationPlaceCard(donationPlace: DonationPlace, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            // White container color
            containerColor = Color.White,
        ),
        modifier = Modifier
            .padding(8.dp)
            .width(150.dp)
            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp)
    ) {
        Image(
            painter = rememberImagePainter(donationPlace.imageUrl),
            contentDescription = donationPlace.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
        )
        Box (
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = donationPlace.name,
                style = Typography.titleSmall,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
            )
        }
    }
}