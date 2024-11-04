package com.wearabouts.ui.donation

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
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

// Colors
import androidx.compose.ui.graphics.Color
import com.wearabouts.ui.theme.IconColor
import com.wearabouts.ui.theme.Primary
import com.wearabouts.ui.theme.Font
import com.wearabouts.ui.theme.White
import com.wearabouts.ui.theme.Transparent

// Type
import com.wearabouts.ui.theme.Typography

// Model
import com.wearabouts.models.Campaing

@Composable
fun CampaingCard (campaing: Campaing, onClick: () -> Unit) {

    // Style vars
    val navbarWidth = 320.dp
    val cardHeight = 270.dp
    val imgHeight = 160.dp

    Card(
        colors = CardDefaults.cardColors(
            // White container color
            containerColor = Color.White,
        ),
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp)
            .width(navbarWidth)
            .height(cardHeight)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp)
    ) {
        Image(
            painter = rememberImagePainter(campaing.imageUrl),
            contentDescription = campaing.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(imgHeight)
                .fillMaxWidth()
        )
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(Primary),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Campaign name
            Text(
                text = campaing.name,
                style = Typography.titleMedium,
                color = Font,
                modifier = Modifier
                    .padding(5.dp)
                    .width(navbarWidth - 50.dp)
            )
            // Progress bar
            var percentage = (campaing.progress.toFloat() / campaing.goal.toFloat()) * 100
            Box(
                modifier = Modifier
                    .width(navbarWidth - 50.dp)
                    .height(8.dp)
                    .background(Font)
                    //.clip(RoundedCornerShape(4.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width((navbarWidth - 50.dp) * (percentage / 100f))
                        .background(IconColor)
                        //.clip(RoundedCornerShape(4.dp))
                )
            }
            // Value of progress and goal; and percentage in number
            Row (
                modifier = Modifier
                    .padding(5.dp)
                    .width(navbarWidth - 50.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Progress
                Text(
                    text = "${campaing.progress.toInt()} / ${campaing.goal.toInt()}",
                    style = Typography.titleSmall,
                    color = Font
                )
                // Percentage
                Text(
                    text = "${percentage.toInt()}%",
                    style = Typography.titleSmall,
                    color = Font
                )
            }
        }
    }   
}