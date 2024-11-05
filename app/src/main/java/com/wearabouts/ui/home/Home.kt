package com.wearabouts.ui.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wearabouts.R

// Data model
import com.wearabouts.models.ClothingItem

// Composables
import com.wearabouts.ui.base.BaseContentPage

// Pop-ups
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader

class Home(private val homeViewModel: HomeViewModel) : BaseContentPage() {

    @Composable
    override fun Content() {

        VerifyCache()

        val clothingItems by homeViewModel.filteredClothingItems.collectAsState()

        // Detail of card selected
        var selected by remember { mutableStateOf<ClothingItem?>(null) }
        val context = LocalContext.current

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
                    ClothingItemCard(item, onClick = {
                        selected = item
                    })
                }
            }

            // Detail of card selected
            LaunchedEffect(selected) {
                selected?.let {
                    navigate("clothingdetail/${it.id}")
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

    @OptIn(ExperimentalCoilApi::class)
    @Composable
    fun VerifyCache() {
        val context = LocalContext.current
        val imageLoader = context.imageLoader

        LaunchedEffect(Unit) {
            // Estadísticas del caché
            val memorySize = imageLoader.memoryCache?.size ?: 0
            val memoryMaxSize = imageLoader.memoryCache?.maxSize ?: 0
            val diskSize = imageLoader.diskCache?.size ?: 0
            val diskMaxSize = imageLoader.diskCache?.maxSize ?: 0

            Log.d(TAG, "=== ESTADÍSTICAS DEL CACHÉ ===")
            Log.d(TAG, "Memoria: $memorySize / $memoryMaxSize bytes")
            Log.d(TAG, "Disco: $diskSize / $diskMaxSize bytes")
            Log.d(TAG, "============================")
        }
    }

}