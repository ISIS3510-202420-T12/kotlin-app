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
import com.wearabouts.ui.theme.Glorify
import com.wearabouts.ui.theme.Typography


class CartScreen {

    @Composable
    fun GoBack(navController: NavController?) {
        Box(
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
    fun Content(navController: NavController) {
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .height(28.dp)
                .background(Color.Black)
        )

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Carrito de Compras",
                style = Typography.titleLarge.copy(fontFamily = Glorify),
                color = Color.Black
            )

            Spacer(modifier = Modifier.width(16.dp))

        }

        GoBack(navController)

    }

}