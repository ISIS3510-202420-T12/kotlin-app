package com.wearabouts.ui.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.viewmodel.compose.viewModel
import android.content.Context

// Styles & resources
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.ui.draw.clip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import com.wearabouts.R
import androidx.compose.ui.res.painterResource

// Borders
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape

// Colors
import androidx.compose.ui.graphics.Color
import com.wearabouts.ui.theme.IconColor
import com.wearabouts.ui.theme.Primary
import com.wearabouts.ui.theme.Font
import com.wearabouts.ui.theme.White
import com.wearabouts.ui.theme.Transparent
import com.wearabouts.ui.theme.Cream

// Type
import com.wearabouts.ui.theme.Typography

// Data model
import com.wearabouts.models.Clothe

// Composables
import com.wearabouts.ui.base.BaseContentPage

// Pop-ups
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

// Coil
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader

class Home(private val homeViewModel: HomeViewModel) : BaseContentPage() {

    private val TAG = "Home"

    @Composable
    override fun Content(modifier: Modifier) {

        VerifyCache()

        val clothingItems by homeViewModel.filteredClothingItems.collectAsState()
        val labels by homeViewModel.labels.collectAsState()

        // Detail of card selected
        var selected by remember { mutableStateOf<Clothe?>(null) }
        val context = LocalContext.current

        val TAG = "Home"

        homeViewModel.getLocation()

        Column(
            modifier = modifier.fillMaxSize()
        ) {

            Spacer(modifier = Modifier.height(120.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CategoryButton("Shoes", R.drawable.home_shoes, { homeViewModel.filterItems("shoes") })
                CategoryButton("Bottoms", R.drawable.home_shorts, { homeViewModel.filterItems("bottoms") })
                CategoryButton("Tops", R.drawable.home_tops, { homeViewModel.filterItems("tops") })
                CategoryButton("Jackets", R.drawable.home_coat, { homeViewModel.filterItems("jackets") })
            }

            Spacer(modifier = Modifier.height(20.dp))

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
                items(clothingItems) { item ->
                    ClothingItemCard(item, onClick = {
                        selected = item
                    })
                }
                item {
                    Spacer(modifier = Modifier.padding(bottom = 450.dp))
                }
            }

            // Detail of card selected
            LaunchedEffect(selected) {
                selected?.let {
                    navigate("clothingdetail/${it.id}")
                }
            }
        }

        filterButton(labels, context)

    }

    @Composable
    fun CategoryButton(
        text: String,
        iconResId: Int,
        onClickFun: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        Column (
            modifier = Modifier
                .size(80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box (
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(IconColor),
                contentAlignment = Alignment.Center
            ) {
                IconButton (
                    onClick = { 
                        try {
                            onClickFun()
                            Log.d(TAG, "Items filtered")
                        }
                        catch (e: Exception) {
                            Log.e(TAG, "Error filtering items: ${e.message}")
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Icon(
                        painter = painterResource(id = iconResId),
                        contentDescription = text,
                        modifier = Modifier.size(30.dp),
                        tint = Cream
                    )
                }
            }
            
            Text(
                text = text,
                style = Typography.titleSmall,
                color = Color.Black,
                modifier = Modifier
            )
        }
    }

    @Composable
    fun filterButton(
        labels: List<String>,
        context: Context,
        modifier: Modifier = Modifier
    ) {
        Box (
            modifier = Modifier
                .offset(x = 310.dp, y = 650.dp)
                .size(50.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(IconColor),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = {
                    // Show filters
                    val labelsArray = arrayOf("Disable") + labels.toTypedArray()
                    AlertDialog.Builder(context)
                        .setTitle("Filter by")
                        .setItems(labelsArray) { dialog, which ->
                            if (which == 0) {
                                homeViewModel.resetFilter()
                                return@setItems
                            }
                            homeViewModel.filterItems(labels[which])
                        }
                        .setNegativeButton("Cancel") { dialog, which ->
                            dialog.dismiss()
                        }
                        .show()
                },
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.filter),
                    contentDescription = "Filter labels",
                    modifier = Modifier.size(15.dp),
                    tint = Cream
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