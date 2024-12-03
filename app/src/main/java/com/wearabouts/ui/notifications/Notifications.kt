package com.wearabouts.ui.notifications

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

// Android management
import androidx.compose.ui.platform.LocalContext

// Classes
import com.wearabouts.ui.login.SecureStorage

@Composable
fun Notifications () {

    // Login info
    val context = LocalContext.current
    var loginInfo = SecureStorage.getLoginInfo(context)
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Login Info:",
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Username: ${loginInfo?.first ?: "Not logged in"}",
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Password: ${loginInfo?.second ?: "Not logged in"}",
            color = Color.Black
        )
    }

}