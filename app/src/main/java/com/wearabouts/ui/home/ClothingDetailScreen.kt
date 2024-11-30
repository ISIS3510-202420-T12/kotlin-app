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

// Grids & lazy layouts
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells

// Colors
import androidx.compose.ui.graphics.Color
import com.wearabouts.ui.theme.IconColor
import com.wearabouts.ui.theme.Primary
import com.wearabouts.ui.theme.Font
import com.wearabouts.ui.theme.White
import com.wearabouts.ui.theme.Transparent
import com.wearabouts.ui.theme.Emerald

// Type
import com.wearabouts.ui.theme.Typography
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign

// Data model
import com.wearabouts.models.Clothe

//Imports for caching images
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.size.Size
import coil.disk.DiskCache
import coil.memory.MemoryCache

// Pop-ups
import android.widget.Toast

// Pager for carrousel
import androidx.compose.foundation.ExperimentalFoundationApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.pager.PagerState

class ClothingDetailScreen(
    private val homeViewModel: HomeViewModel,
    private val itemId: String
) : BaseContentPage() {

    @Composable
    fun goBack() {
        IconButton(
            onClick = {
                navigate("home")
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.home),
                contentDescription = "Back",
                tint = IconColor,
                modifier = Modifier.size(30.dp)
            )
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    override fun Content(modifier: Modifier) {
        val pagerState = rememberPagerState()

        val clothingItem: Clothe? = homeViewModel.getItemById(itemId)

        // Local state for favorite status
        var isFavorite by remember { mutableStateOf(homeViewModel.isFavorite(clothingItem!!)) }

        clothingItem?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top=35.dp, bottom=30.dp, start=30.dp, end=30.dp)
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(60.dp))

                if (clothingItem.images.isNotEmpty()) {
                    Box (  
                        modifier = Modifier
                            .width(220.dp)
                            .height(300.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        HorizontalPager(
                            count = clothingItem.images.size,
                            state = pagerState,
                            modifier = Modifier
                                .height(300.dp)
                                .width(220.dp)
                        ) { page ->
                            Image(
                                painter = rememberImagePainter(clothingItem.images[page]),
                                contentDescription = clothingItem.name,
                                modifier = Modifier
                                    .height(300.dp)
                                    .width(220.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(185.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                        // Title
                        Text(
                            text = clothingItem.name,
                            style = Typography.titleLarge,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        // Price
                        Text(
                            text = "$${clothingItem.price}",
                            style = Typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = Emerald
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        // Size
                        Text(
                            text = "Size: ${clothingItem.size}",
                            style = Typography.bodyLarge,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        // Labels
                        Text(
                            text = "Labels: ${clothingItem.labels.joinToString()}",
                            style = Typography.bodyLarge,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    
                }      

                Spacer(modifier = Modifier.height(15.dp))

                // Buttons
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
                            style = Typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = White
                        )
                    }
                }          
            }
        }
    }
}