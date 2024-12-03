package com.wearabouts.ui.profile

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
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale

// Borders
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape

// Colors
import androidx.compose.ui.graphics.Color
import com.wearabouts.ui.theme.Yellow
import com.wearabouts.ui.theme.Cream
import com.wearabouts.ui.theme.Emerald

// Type
import com.wearabouts.ui.theme.Typography
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.wearabouts.ui.theme.Glorify

// Data model
import com.wearabouts.models.User
import com.wearabouts.models.Clothe
import com.wearabouts.ui.user.UserViewModel
import com.wearabouts.ui.home.HomeViewModel
import com.wearabouts.storage.SupabaseViewModel
import com.wearabouts.BuildConfig

// Composables
import com.wearabouts.ui.base.BaseContentPage
import com.wearabouts.ui.home.ClothingItemCard

// Pop-ups
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

// Coil
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader

// Carrousel
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items

class Profile (userViewModel: UserViewModel, private val homeViewModel: HomeViewModel, private val supabaseViewModel: SupabaseViewModel) : BaseContentPage() {

    // The current user logged in is defined in BaseContentPage class already, with var name "user":User?

    private val TAG = "supabase"

    fun getUrl (id: Int): String {
        return "https://zoinycbesbrgjevnkomf.supabase.co/storage/v1/object/public/backgrounds/illustrations/image%20$id.png"
    }

    @Composable
    override fun Content(modifier: Modifier) {

        Log.d(TAG, "Supabase URL: ${BuildConfig.SUPABASE_URL}")
        Log.d(TAG, "Supabase Key: ${BuildConfig.SUPABASE_ANON_KEY}")

        val currentUser = user!!

        // DONT CALL IT AS IT DOESNT WORK FOR NOW
        // supabaseViewModel.fetchBackgrounds()
        // val backgrounds by supabaseViewModel.backgrounds.collectAsState()

        // Background
        val startBackgroundId = 1
        val endBackgroundId = 19

        val backgrounds = (startBackgroundId..endBackgroundId).map { getUrl(it) }

        // Current background will depend on the user background
        val defualtBackground = getUrl(18) // Default background
        val currentBackground = defualtBackground
        // ~ ~ ~ ~ ~ ~

        // Clothing items
        val clothingItems by homeViewModel.filteredClothingItems.collectAsState()

        // Get the clothes that the user sells, clothe.seller == user.email
        val userClothes = clothingItems.filter { it.seller == currentUser?.email }

        // Selected item to switch to detail view
        var selectedItem by remember { mutableStateOf<Clothe?>(null) }
        // ~ ~ ~ ~ ~ ~ ~ ~

        val context = LocalContext.current

        if (currentUser != null) {
            Column (
                modifier = modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                backgroundImage(currentBackground)
                Spacer(modifier = Modifier.height(55.dp))
                // Name
                Text(
                    text = currentUser.name,
                    style = Typography.titleLarge.copy(fontFamily = Glorify),
                    color = Color.Black,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Rating
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.star),
                        contentDescription = null,
                        tint = Yellow,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(7.dp))
                    Text(
                        text = "${currentUser.rating}",
                        style = Typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = Emerald,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                // Text indicating the user is a seller
                Text(
                    text = "Sells",
                    style = Typography.bodyLarge,
                    color = Color.Gray,
                    fontSize = 13.sp
                ) 

                // Items carrousel
                Spacer(modifier = Modifier.height(5.dp))
                
                // Horizontal pager of clothing item cards
                LazyRow(
                    modifier = Modifier
                        .width(320.dp)
                        .height(220.dp)
                        .background(Color.White)
                ) { 
                    items(userClothes) { item ->
                        // Card container
                        val cardHeight = 270.dp
                        val cardWidth = 100.dp
                        Box (
                            modifier = Modifier
                                .width(150.dp)
                                .height(265.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            ClothingItemCard(item = item, onClick = {
                                selectedItem = item
                            })
                        }
                    }
                }

                LaunchedEffect(selectedItem) {
                    selectedItem?.let {
                        navigate("clothingdetail/${it.id}")
                    }
                }
            }
            floatingIcon(currentUser.icon)
        }
    }

    @Composable
    fun backgroundImage (currentBackground: String) {
        val backgroundHeight = 340.dp
        // Background
        AsyncImage(
            model = currentBackground,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(backgroundHeight)
                .background(Color.White)
                .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)),
            contentScale = ContentScale.Crop
        )
    }

    @Composable
    fun floatingIcon (icon: String) {
        val iconSize = 100.dp
        val yOffset = 285.dp
        // User icon
        Row (
            modifier = Modifier
                .offset(y = yOffset)
                .fillMaxWidth()
                .height(iconSize),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            AsyncImage(
                model = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(iconSize)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(BorderStroke(5.dp, Color.White), CircleShape),
                contentScale = ContentScale.Crop
            )
        }   
    }

}