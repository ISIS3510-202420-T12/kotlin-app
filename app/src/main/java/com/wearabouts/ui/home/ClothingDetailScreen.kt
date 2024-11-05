package com.wearabouts.ui.home

// Composables
import androidx.compose.runtime.*
import com.wearabouts.ui.base.BaseContentPage
import com.wearabouts.ui.donation.CampaingCard

// Debugging
import android.util.Log

// Material
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme

// Styles & resources
import com.wearabouts.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.clickable

// Colors
import androidx.compose.ui.graphics.Color
import com.wearabouts.ui.theme.IconColor
import com.wearabouts.ui.theme.Primary
import com.wearabouts.ui.theme.Font
import com.wearabouts.ui.theme.White
import com.wearabouts.ui.theme.Transparent

// Type
import androidx.compose.ui.text.style.TextAlign
import com.wearabouts.ui.theme.Typography

// Data model
import com.wearabouts.models.ClothingItem

//Imports for caching images
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.size.Size
import coil.disk.DiskCache
import coil.memory.MemoryCache

// Pop-ups
import android.widget.Toast

class ClothingDetailScreen(
    private val homeViewModel: HomeViewModel,
    private val itemId: String
) : BaseContentPage() {

    @Composable
    override fun Content() {

        val clothingItem: ClothingItem? = homeViewModel.getItemById(itemId)

        // Local state for favorite status
        var isFavorite by remember { mutableStateOf(homeViewModel.isFavorite(clothingItem!!)) }

        clothingItem?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top=100.dp, bottom=30.dp, start=30.dp, end=30.dp)
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (clothingItem.imageUrls.isNotEmpty()) {
                    Box (  
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = rememberImagePainter(clothingItem.imageUrls[0]),
                            contentDescription = clothingItem.name,
                            modifier = Modifier
                                .height(300.dp)
                                .fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Spacer(modifier = Modifier.height(50.dp))
                Text(
                    text = clothingItem.name,
                    style = Typography.titleLarge,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    text = clothingItem.description,
                    style = Typography.titleMedium,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(50.dp))
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Spacer(modifier = Modifier.width(20.dp))
                    // Fav button
                    IconButton(
                        onClick = {
                            // Update both local state and ViewModel
                            isFavorite = !isFavorite
                            homeViewModel.toggleFav(clothingItem)
                        } 
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.heart),
                            contentDescription = "Add to cart",
                            tint = if (isFavorite) Color.Red else Color.Gray,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(40.dp))
                    // Buy button with text
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Primary)
                            .clickable {
                                homeViewModel.buyItem(clothingItem)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Buy",
                            style = Typography.titleMedium,
                            color = White
                        )
                    }
                }
            }
        }
    }
}