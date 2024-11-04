package com.wearabouts.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wearabouts.R
import com.wearabouts.ui.base.BaseContentPage

class Home : BaseContentPage() {

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val homeViewModel: HomeViewModel = viewModel()
        val clothingItems by homeViewModel.filteredClothingItems.collectAsState()

        homeViewModel.getLocation()

        Column(modifier = Modifier.fillMaxSize()) {
            CustomToolbar()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CategoryButton("Shoes", R.drawable.shoes, { homeViewModel.filterItems("Shoes") })
                CategoryButton("Bottoms", R.drawable.pants, { homeViewModel.filterItems("Bottoms") })
                CategoryButton("Tops", R.drawable.tshirt, { homeViewModel.filterItems("Tops") })
                CategoryButton("Jackets", R.drawable.ic_jacket, { homeViewModel.filterItems("Jackets") })
            }

            //Text(
            //    text = "New Releases",
            //    style = MaterialTheme.typography.headlineSmall,
            //    modifier = Modifier.padding(16.dp)
            //)

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
    fun CustomToolbar() {


        Row(
            modifier = Modifier.padding(top = 40.dp)

        ){}
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(colorResource(id = R.color.app_color2))
                .padding(top = 16.dp), // Asegúrate de añadir un padding superior
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Handle menu icon click */ }) {
                Image(
                    painter = painterResource(id = R.drawable.menu),
                    contentDescription = "Menu",
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { /* Handle notification icon click */ }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_notification),
                    contentDescription = "Notifications",
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "WearAbouts",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = { /* Handle search icon click */ }) {
                Image(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "Search",
                    modifier = Modifier.size(20.dp)
                )
            }

            IconButton(onClick = { /* Handle cart icon click */ }) {
                Image(
                    painter = painterResource(id = R.drawable.shopping_bag),
                    contentDescription = "Shopping Cart",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }

    @Composable
    fun CategoryButton(

        text: String,
        iconResId: Int,
        onClick: () -> Unit,
        modifier: Modifier = Modifier
    ) {

        Button(
            onClick = {
                println("Category button '$text' clicked")
                onClick()
            },
            modifier = modifier
                .padding(horizontal = 2.dp)
                .height(120.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = text,
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = text,
                    color = Color.Black,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }

}