package com.wearabouts.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import com.wearabouts.R
import androidx.compose.ui.graphics.Color

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.navigation.NavController

// Website to convert SVG to Drawable: https://svg2vector.com/

@Composable
fun NavBar(navController: NavController, modifier: Modifier = Modifier) {
    BottomAppBar {
        IconButton(
            onClick = { navController.navigate("home") },
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.home),
                contentDescription = "Home",
                modifier = Modifier.size(24.dp),
                tint = Color.Black
            )
        }
        IconButton(
            onClick = { navController.navigate("donation") },
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.donate),
                contentDescription = "Donation",
                modifier = Modifier.size(24.dp),
                tint = Color.Black
            )
        }
    }
}