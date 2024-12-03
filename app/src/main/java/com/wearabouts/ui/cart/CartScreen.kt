// Archivo: app/src/main/java/com/wearabouts/ui/cart/CartScreen.kt
package com.wearabouts.ui.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.wearabouts.R
import com.wearabouts.models.Clothe
import com.wearabouts.ui.theme.Typography


class CartScreen() {

    @Composable
    fun goBack(navController: NavController?) {
        Box (
            modifier = Modifier
                .offset(x = 17.dp, y = 50.dp)
                .size(40.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(Color.Gray.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = {
                    navController?.popBackStack()
                },
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }

    @Composable
    fun Content(navController: NavController){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Carrito de Compras",
                style = Typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            goBack(navController)
        }
    }