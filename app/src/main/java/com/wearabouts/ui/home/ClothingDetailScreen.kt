package com.wearabouts.ui.home

// Composable
import android.widget.Toast
import androidx.compose.runtime.*
import com.wearabouts.ui.user.MiniUserView

// ViewModels
//import com.wearabouts.ui.user.UserViewModel

// Debugging
//import android.util.Log

// Material
import androidx.compose.material3.Text
//import androidx.compose.material3.MaterialTheme

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
//import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.clickable

// Grids & lazy layouts
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.lazy.grid.GridCells

// Colors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.wearabouts.ui.theme.Primary
import com.wearabouts.ui.theme.White
import com.wearabouts.ui.theme.Emerald

// Type
import com.wearabouts.ui.theme.Typography
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign

// Data models
import com.wearabouts.models.Clothe
import com.wearabouts.models.User

// Image caching
//import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
//import coil.request.ImageRequest
//import coil.size.Size
//import coil.disk.DiskCache
//import coil.memory.MemoryCache

// Pop-ups
//import android.widget.Toast

// Pager for carrousel
//import androidx.compose.foundation.ExperimentalFoundationApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
//import com.google.accompanist.pager.PagerState

// Page navigation
import androidx.navigation.NavController

class ClothingDetailScreen (
    private val homeViewModel: HomeViewModel,
    private val itemId: String,
    private val users: List<User>
) {

    @Composable
    fun GoBack(navController: NavController?) {
        Box (
            modifier = Modifier
                .offset(x = 17.dp, y = 50.dp)
                .size(40.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(Color.Gray.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = {
                    navController?.popBackStack()
                },
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun Content(navController: NavController) {
        val pagerState = rememberPagerState()

        val clothingItem: Clothe? = homeViewModel.getItemById(itemId)
        val user: User? = users.find { it.email == clothingItem?.seller }

        // Local state for favorite status
        var isFavorite by remember { mutableStateOf(homeViewModel.isFavorite(clothingItem!!)) }

        // Container
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Box to make notifications area of the phone visible
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .background(Color.Black)
            )

            Spacer(modifier = Modifier.height(15.dp))

            // Content
            Box (
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                clothingItem?.let {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom=30.dp, start=30.dp, end=30.dp)
                            .background(Color.White),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Spacer(modifier = Modifier.height(40.dp))

                        if (clothingItem.images.isNotEmpty()) {
                            Box (  
                                modifier = Modifier
                                    .width(260.dp)
                                    .height(340.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                HorizontalPager(
                                    count = clothingItem.images.size,
                                    state = pagerState,
                                    modifier = Modifier.fillMaxSize()
                                ) { page ->
                                        Image(
                                            painter = rememberImagePainter(clothingItem.images[page]),
                                            contentDescription = clothingItem.name,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.BottomEnd)
                                                .padding(8.dp)
                                                .background(Color.Black, shape = RoundedCornerShape(8.dp))
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = "${page + 1}/${clothingItem.images.size}",
                                                style = Typography.bodyLarge,
                                                color = Color.White,
                                                fontSize = 10.sp
                                            )
                                        }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        // Column of item data
                        Box (
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp)
                        ) {
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                item {
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
                                        color = Color.Black,
                                        fontSize = 13.sp
                                    )

                                    Spacer(modifier = Modifier.height(20.dp))

                                    // Labels
                                    Text(
                                        text = clothingItem.labels.joinToString(separator = "     "),
                                        style = Typography.bodyLarge,
                                        color = Color.Gray,
                                        textAlign = TextAlign.Center,
                                        fontSize = 12.sp
                                    )

                                    // Mini user view of seller
                                    MiniUserView(user)
                                }
                            }   
                        }   

                        Spacer(modifier = Modifier.height(15.dp))

                        // Buttons
                        Row (
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.width(20.dp))
                            // Fav button
                            FavButton({
                                isFavorite = !isFavorite
                                homeViewModel.toggleFav(clothingItem)
                            }, isFavorite)
                            Spacer(modifier = Modifier.width(40.dp))
                            // Buy button with text
                            BuyButton(clothingItem)
                        }          
                    }
                }
            }
        }

        GoBack(navController)
    }

    @Composable
    fun BuyButton (clothingItem: Clothe) {

        val context = LocalContext.current

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Primary)
                .clickable {

                    homeViewModel.addToCart(clothingItem)
                    //homeViewModel.buyItem(clothingItem)
                    Toast.makeText(context, "Añadido al carrito con éxito", Toast.LENGTH_SHORT).show()

                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Add to bag",
                style = Typography.bodyLarge,
                color = White
            )
        }
    }

    @Composable
    fun FavButton (onClickFunction: () -> Unit, isFavorite: Boolean) {
        IconButton(
            onClick = { onClickFunction() }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.heart),
                contentDescription = "Add to cart",
                tint = if (isFavorite) Color.Red else Color.Gray,
                modifier = Modifier.size(30.dp)
            )
        }
    }

}