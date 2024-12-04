package com.wearabouts.ui.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.wearabouts.R
import com.wearabouts.models.Clothe
import com.wearabouts.ui.home.ClothingItemCard
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
    fun Content(navController: NavController, cartItems: List<Clothe>) {


        var selected by remember { mutableStateOf<Clothe?>(null) }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Spacer(modifier = Modifier.height(55.dp))


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Carrito",
                    style = Typography.titleLarge.copy(fontFamily = Glorify),
                    color = Color.Black
                )
            }


            Spacer(modifier = Modifier.height(40.dp))



            if (cartItems.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "El carrito está vacío",
                        style = Typography.bodyLarge,
                        color = Color.Gray
                    )
                }
            } else {

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(15.dp)),
                    contentPadding = PaddingValues(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(cartItems) { item ->
                        ClothingItemCard(item, onClick = {
                            selected = item
                        })
                    }
                    item {
                        Spacer(modifier = Modifier.padding(bottom = 450.dp))
                    }
                }
            }

            // Detail of card selected
            LaunchedEffect(selected) {
                selected?.let {
                    navController.navigate("clothingdetail/${it.id}")
                }
            }
        }

        GoBack(navController)

    }

}