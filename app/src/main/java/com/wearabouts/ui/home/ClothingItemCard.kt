package com.wearabouts.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import com.wearabouts.models.ClothingItem
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter

//Imports for caching images
//import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
//import coil.disk.DiskCache
//import coil.memory.MemoryCache

// Borders
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip

@Composable
fun ClothingItemCard(item: ClothingItem, onClick: () -> Unit) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        if (item.imageUrls.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context)
                        .data(item.imageUrls[0])
                        .size(Size.ORIGINAL)
                        .memoryCacheKey(item.imageUrls[0])
                        .diskCacheKey(item.imageUrls[0])
                        .crossfade(true)
                        .build()
                ),
                contentDescription = item.name,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(15.dp)),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = item.name)
        Text(text = "$${item.price}")
    }
}