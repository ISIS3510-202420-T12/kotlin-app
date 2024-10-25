package com.wearabouts.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.colorResource
import com.wearabouts.ui.base.BaseContentPage
import com.wearabouts.R
import androidx.compose.material3.ButtonDefaults

// Location
import androidx.compose.ui.platform.LocalContext

class Home : BaseContentPage() {

    

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val homeViewModel: HomeViewModel = viewModel()
        val clothingItems by homeViewModel.clothingItems.collectAsState()

        homeViewModel.getLocation()

        Column(modifier = Modifier.fillMaxSize()) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CategoryButton("Shoes", R.drawable.shoes, colorResource(id = R.color.app_color2))
                CategoryButton("Bottoms", R.drawable.pants, colorResource(id = R.color.app_color2))
                CategoryButton("Tops", R.drawable.tshirt, colorResource(id = R.color.app_color2))
                CategoryButton("Jackets", R.drawable.scarf, colorResource(id = R.color.app_color2))
            }

            Text(
                text = "New Releases",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                contentPadding = PaddingValues(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(clothingItems) { item ->
                    ClothingItemCard(item)
                }
            }
        }
    }

    @Composable
    fun CategoryButton(text: String, iconResId: Int, backgroundColor: Color, modifier: Modifier = Modifier) {
        Button(
            onClick = { /* Handle category click */ },
            modifier = modifier
                .padding(horizontal = 2.dp),
            colors = ButtonDefaults.buttonColors(containerColor = backgroundColor) // Establece el color de fondo
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = text,
                    modifier = Modifier.size(24.dp) // Ajusta el tamaño del ícono
                )
                Text(text)
            }
        }
    }
}
