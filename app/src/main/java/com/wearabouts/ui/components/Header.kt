package com.wearabouts.ui.components

// Material
import androidx.compose.material3.Text

// Layout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*

// Styles
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

// Borders
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape

// For icons
import com.wearabouts.R
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

// Type
import com.wearabouts.ui.theme.Typography
import com.wearabouts.ui.theme.Glorify

// Colors
import com.wearabouts.ui.theme.IconColor
import com.wearabouts.ui.theme.Primary

@Composable
fun Header(modifier: Modifier = Modifier) {
    val iconSizes = 20.dp
    val inconTint = Color.Black

    // Box to make notifications area of the phone visible
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .height(28.dp)
            .background(Color.Black)
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 60.dp, start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        // Bell icon
        Icon(
            painter = painterResource(id = R.drawable.bell),
            contentDescription = "Bell Icon",
            tint = inconTint,
            modifier = Modifier.size(iconSizes)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Title
        Text(
            text = "WearAbouts",
            style = Typography.titleLarge.copy(fontFamily = Glorify),
            color = Color.Black
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Cart icon
        Icon(
            painter = painterResource(id = R.drawable.menu),
            contentDescription = "Cart Icon",
            tint = inconTint,
            modifier = Modifier.size(iconSizes)
        )
    }
}