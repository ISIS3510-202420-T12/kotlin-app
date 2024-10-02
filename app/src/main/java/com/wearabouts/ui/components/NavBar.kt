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
import com.wearabouts.ui.theme.NavbarBgColor

// Website to convert SVG to Drawable: https://svg2vector.com/

@Composable
fun NavBar(navController: NavController, modifier: Modifier = Modifier) {

    BottomAppBar(
        modifier = modifier
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(NavbarBgColor)
                .clip(RoundedCornerShape(30.dp))
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.navigate("Home") },
                    modifier = Modifier
                        .size(60.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.home),
                        contentDescription = "Home",
                        modifier = Modifier.size(30.dp),
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
                        modifier = Modifier.size(24.dp),
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
                        modifier = Modifier.size(24.dp),
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
                        modifier = Modifier.size(24.dp),
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
                        modifier = Modifier.size(24.dp),
                        tint = IconColor
                    )
                }
            }
        }
        
    }
}