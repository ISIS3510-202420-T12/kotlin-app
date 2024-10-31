package com.wearabouts.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import com.wearabouts.R
import androidx.compose.runtime.Composable

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource

import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.navigation.NavController

// Colors
import com.wearabouts.ui.theme.IconColor
import com.wearabouts.ui.theme.Primary

// Borders
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape

// Website to convert SVG to Drawable: https://svg2vector.com/

@Composable
fun NavBar(navController: NavController, currentRoute: String, modifier: Modifier = Modifier) {

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(Color.White)
        )
    }

    Box(
        modifier = modifier
            .width(420.dp) // 350.dp
            .height(155.dp) // 125.dp
            .padding(16.dp)
            .padding(bottom = 25.dp)
            .border(border = BorderStroke(10.dp, Color.White), shape = CircleShape)
            .clip(RoundedCornerShape(60.dp))
            .background(Primary)
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.navigate("home") },
                modifier = Modifier
                    .size(56.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = "Home",
                    modifier = Modifier.size(24.dp),
                    tint = if (currentRoute == "home") Color.White else IconColor
                )
            }
            IconButton(
                onClick = { navController.navigate("tags") },
                modifier = Modifier
                    .size(56.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.tags),
                    contentDescription = "Tags",
                    modifier = Modifier.size(27.dp),
                    tint = if (currentRoute == "tags") Color.White else IconColor
                )
            }
            IconButton(
                onClick = { navController.navigate("donation") },
                modifier = Modifier
                    .size(56.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.donate),
                    contentDescription = "Donation",
                    modifier = Modifier.size(27.dp),
                    tint = if (currentRoute == "donation") Color.White else IconColor
                )
            }
            IconButton(
                onClick = { navController.navigate("favourites") },
                modifier = Modifier
                    .size(56.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.heart),
                    contentDescription = "Favourites",
                    modifier = Modifier.size(25.dp),
                    tint = if (currentRoute == "favourites") Color.White else IconColor
                )
            }

            Box(
                modifier = Modifier
                    .size(56.dp),
                contentAlignment = Alignment.Center
            ) {

                Box(
                    modifier = Modifier
                        .size(if (currentRoute == "profile") 44.dp else 43.dp)
                        .border(border = BorderStroke(5.dp, Color.White), shape = CircleShape)
                ) {}

                IconButton(
                    onClick = { navController.navigate("profile") },
                    modifier = Modifier
                        .size(56.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(if (currentRoute == "profile") 38.dp else 40.dp),
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
}