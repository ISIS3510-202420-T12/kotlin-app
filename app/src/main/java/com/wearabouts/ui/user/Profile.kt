package com.wearabouts.ui.profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.viewmodel.compose.viewModel
import android.content.Context

// Styles & resources
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.ui.draw.clip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import com.wearabouts.R
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale

// Borders
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape

// Colors
import androidx.compose.ui.graphics.Color
import com.wearabouts.ui.theme.Yellow
import com.wearabouts.ui.theme.Cream

// Type
import com.wearabouts.ui.theme.Typography
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Data model
import com.wearabouts.models.User
import com.wearabouts.ui.user.UserViewModel

// Composables
import com.wearabouts.ui.base.BaseContentPage

// Pop-ups
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

// Coil
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader

class Profile (userViewModel: UserViewModel) : BaseContentPage() {

    // The current user logged in is defined in BaseContentPage class already, with var name "user":User?

    private val TAG = "Profile"

    @Composable
    override fun Content(modifier: Modifier) {

        val context = LocalContext.current

        if (user == null) {
            Log.d(TAG, "User is null")
            Toast.makeText(context, "User is null", Toast.LENGTH_SHORT).show()
            return
        }

        val currentUser = user!!
        if (currentUser != null) {
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
                    currentUser.icon?.let { iconUrl ->
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
                            text = currentUser.name,
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
                                text = "${currentUser.rating}",
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
}