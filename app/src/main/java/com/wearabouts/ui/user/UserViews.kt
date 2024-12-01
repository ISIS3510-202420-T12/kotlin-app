package com.wearabouts.ui.user

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
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Colors
import com.wearabouts.ui.theme.Yellow

// Models & project
import com.wearabouts.models.User

@Composable
fun MiniUserView (user: User?) {

    if (user != null) {

        Box (
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            contentAlignment = Alignment.Center
        ) {
            Spacer(modifier = Modifier.height(70.dp))
            Row (
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(75.dp))
                // Load image from URL
                user.icon?.let { iconUrl ->
                    AsyncImage(
                        model = iconUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(BorderStroke(0.5.dp, Color.Gray), CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(25.dp))

                Column {
                    // Vendor
                    Text(
                        text = "Vendor",
                        style = Typography.bodyLarge,
                        color = Color.Gray,
                        fontSize = 12.sp
                    ) 
                    Spacer(modifier = Modifier.height(3.dp))
                    // Name
                    Text(
                        text = user.name,
                        style = Typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.Black,
                        fontSize = 14.sp
                    )

                    // Rating
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.star),
                            contentDescription = null,
                            tint = Yellow,
                            modifier = Modifier.size(25.dp)
                        )
                        Spacer(modifier = Modifier.width(7.dp))
                        Text(
                            text = "${user.rating}",
                            style = Typography.bodyLarge,
                            color = Color.Black,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}