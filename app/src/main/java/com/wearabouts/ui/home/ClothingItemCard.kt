package com.wearabouts.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

import coil.compose.rememberImagePainter

import com.wearabouts.models.ClothingItem

import androidx.compose.runtime.Composable

import androidx.compose.material3.Text
import coil.compose.rememberAsyncImagePainter

@Composable
fun ClothingItemCard(item: ClothingItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(item.imageUrl),
            contentDescription = item.name,
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = item.name)
        Text(text = "$${item.price}")
    }
}