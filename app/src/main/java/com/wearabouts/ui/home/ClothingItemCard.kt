package com.wearabouts.ui.home
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import com.wearabouts.models.ClothingItem
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter

@Composable
fun ClothingItemCard(item: ClothingItem, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        if (item.imageUrls.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(item.imageUrls[0]),
                contentDescription = item.name,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = item.name)
        Text(text = "$${item.price}")
    }
}