package com.wearabouts.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

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

// Website to convert SVG to Drawable: https://svg2vector.com/

@Composable
fun NavBar(navController: NavController, modifier: Modifier = Modifier) {

    Box(
        modifier = modifier
            .width(350.dp)
            .height(125.dp)
            .padding(16.dp)
            .padding(bottom = 25.dp)
            .clip(RoundedCornerShape(60.dp))
            .background(Primary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
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
                    tint = IconColor
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
                    tint = IconColor
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
                    tint = IconColor
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
                    tint = IconColor
                )
            }
            IconButton(
                onClick = { navController.navigate("profile") },
                modifier = Modifier
                    .size(56.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Profile Icon",
                    modifier = Modifier.size(40.dp),
                    tint = Color.Unspecified
                )
            }
        }
    }
}