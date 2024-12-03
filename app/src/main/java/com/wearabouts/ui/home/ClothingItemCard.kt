package com.wearabouts.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import com.wearabouts.models.Clothe
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

// Type
import com.wearabouts.ui.theme.Typography
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Colors
import androidx.compose.ui.graphics.Color
import com.wearabouts.ui.theme.IconColor
import com.wearabouts.ui.theme.Primary
import com.wearabouts.ui.theme.Font
import com.wearabouts.ui.theme.White
import com.wearabouts.ui.theme.Transparent
import com.wearabouts.ui.theme.Emerald

@Composable
fun ClothingItemCard(item: Clothe, onClick: () -> Unit) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        if (item.images.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context)
                        .data(item.images[0])
                        .size(Size.ORIGINAL)
                        .memoryCacheKey(item.images[0])
                        .diskCacheKey(item.images[0])
                        .crossfade(true)
                        .build()
                ),
                contentDescription = item.name,
                modifier = Modifier
                    .height(230.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(15.dp)),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = item.name,
            style = Typography.bodyLarge,
            color = Color.Black,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 4.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "$${item.price}",
            style = Typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = Emerald,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}