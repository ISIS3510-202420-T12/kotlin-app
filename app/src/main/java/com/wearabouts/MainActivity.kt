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
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

import com.wearabouts.ui.login.Login
import com.wearabouts.ui.login.Register
import com.wearabouts.ui.login.LoginViewModel
import com.wearabouts.ui.home.Home
import com.wearabouts.ui.donationMap.DonationMap
import com.wearabouts.ui.donation.Donation
import com.wearabouts.ui.home.ClothingDetailScreen

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Biometrics
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.compose.ui.platform.LocalContext

// ViewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wearabouts.ui.home.HomeViewModel
import androidx.activity.viewModels

//Imports for caching images
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.ImageLoader


class MainActivity : FragmentActivity() {

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    // private lateinit var homeViewModel: HomeViewModel
    private lateinit var loginViewModel: LoginViewModel

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Configurar Coil para que use un caché
        val imageLoader = ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(this.cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.02)
                    .build()
            }
            .crossfade(true)
            .build()

        // Establecer el ImageLoader como predeterminado
        coil.Coil.setImageLoader(imageLoader)

        // homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        setupBiometricPrompt()

        enableEdgeToEdge()
        setContent {
            WearAboutsTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    NavHost(navController = navController, startDestination = "home") {
                        composable("login") { 
                            Login(
                                navController = navController,
                                biometricPrompt = biometricPrompt,
                                promptInfo = promptInfo,
                                viewModel = loginViewModel
                            ) 
                        }
                        composable("donation") { Donation().Template(navController) }
                        composable("donationMap") { DonationMap().Template(navController) }
                        composable("home") { Home(homeViewModel).Template(navController) }
                        
                        composable("clothingdetail/{itemId}") { backStackEntry ->
                            val itemId = backStackEntry.arguments?.getString("itemId")
                            if (itemId != null) {
                                ClothingDetailScreen(homeViewModel, itemId).Template(navController)
                            } else {
                                // Handle the null case, maybe show an error or navigate back
                            }
                        }

                        composable("register") { Register(navController) }

                        // Unimplemented
                        composable("tags") { Home(homeViewModel).Template(navController) }
                        composable("favourites") { Home(homeViewModel).Template(navController) }
                        composable("profile") { Home(homeViewModel).Template(navController) }
                    }
                }
            }
        }
    }

    private fun setupBiometricPrompt() {
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric authentication")
            .setSubtitle("Use your fingerprint to login")
            .setNegativeButtonText("Cancel")
            .build()

        biometricPrompt = BiometricPrompt(
            this,
            ContextCompat.getMainExecutor(this),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    loginViewModel.onBiometricSuccess()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    loginViewModel.onBiometricError(errString.toString())
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    loginViewModel.onBiometricError("Failed to authenticate")
                }
            }
        )
    }
}
