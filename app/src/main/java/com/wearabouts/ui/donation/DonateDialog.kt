package com.wearabouts.ui.donation

// Composables
import androidx.compose.runtime.Composable

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
import androidx.compose.material3.Button
import androidx.compose.material3.AlertDialog

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
fun DonateDialog(
    campaign: Campaing,
    onDismiss: () -> Unit,
    onDonate: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Donate to ${campaign.name}", style = Typography.titleLarge, color = White)
        },
        text = {
            Text(text = "Are you sure you want to donate to ${campaign.name}?", color = White)
        },
        confirmButton = {
            Button(onClick = onDonate) {
                Text("Donate")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}