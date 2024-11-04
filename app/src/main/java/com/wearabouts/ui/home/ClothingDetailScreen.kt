package com.wearabouts.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberImagePainter
import androidx.compose.ui.unit.dp

// Pop-ups
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.compose.ui.platform.LocalContext

// ViewModels
import com.wearabouts.ui.home.HomeViewModel

// Data model
import com.wearabouts.models.ClothingItem

@Composable
fun ClothingDetailScreen(homeViewModel: HomeViewModel, itemId: String) {
    // Context
    val context = LocalContext.current
    var clothingItem: ClothingItem? = homeViewModel.getItemById(itemId)

    // Toast.makeText(context, "ClothingDetailScreen ${clothingItem?.name}", Toast.LENGTH_SHORT).show()

    clothingItem?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Image(
            //     painter = rememberImagePainter(clothingItem.imageUrl),
            //     contentDescription = clothingItem.name,
            //     modifier = Modifier
            //         .fillMaxWidth()
            //         .height(300.dp),
            //     contentScale = ContentScale.Crop
            // )
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