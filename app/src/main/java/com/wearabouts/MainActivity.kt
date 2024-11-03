package com.wearabouts

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.wearabouts.ui.theme.WearAboutsTheme

import com.wearabouts.ui.login.Login
import com.wearabouts.ui.login.Register
import com.wearabouts.ui.home.Home
import com.wearabouts.ui.donation.view.Donation

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wearabouts.ui.home.ClothingDetailScreen

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WearAboutsTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") { Login(navController) }
                        composable("donation") { Donation().Template(navController) }
                        composable("home") { Home().Template(navController) }
                        composable("clothingDetail"){
                            //ClothingDetailScreen()
                        }
                        composable("register") { Register(navController) }

                        // Unimplemented
                        composable("tags") { Home().Template(navController) }
                        composable("favourites") { Home().Template(navController) }
                        composable("profile") { Home().Template(navController) }
                    }
                }
            }
        }
    }
}