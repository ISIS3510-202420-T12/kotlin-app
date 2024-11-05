package com.wearabouts.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext

// Pop-ups
//import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter

// ViewModels

// Data model
import com.wearabouts.models.ClothingItem

//Imports for caching images
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import coil.disk.DiskCache
import coil.memory.MemoryCache

@Composable
fun ClothingDetailScreen(homeViewModel: HomeViewModel, itemId: String) {

    val context = LocalContext.current
    val clothingItem: ClothingItem? = homeViewModel.getItemById(itemId)

    clothingItem?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {


            if (clothingItem.imageUrls.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(clothingItem.imageUrls[0])
                            .size(Size.ORIGINAL)
                            .memoryCacheKey(clothingItem.imageUrls[0])
                            .diskCacheKey(clothingItem.imageUrls[0])
                            .crossfade(true)
                            .build()
                    ),
                    contentDescription = clothingItem.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = clothingItem.name,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = clothingItem.description,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}