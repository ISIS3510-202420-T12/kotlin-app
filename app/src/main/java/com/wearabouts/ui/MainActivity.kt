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

// Pages
import com.wearabouts.ui.login.Login
import com.wearabouts.ui.login.Register
import com.wearabouts.ui.login.LoginViewModel
import com.wearabouts.ui.home.Home
import com.wearabouts.ui.donationMap.DonationMap
import com.wearabouts.ui.donation.Donation
import com.wearabouts.ui.home.ClothingDetailScreen
import com.wearabouts.ui.notifications.Notifications
import com.wearabouts.ui.profile.Profile

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
import com.wearabouts.ui.user.UserViewModel
import com.wearabouts.storage.SupabaseViewModel

//Imports for caching images
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.ImageLoader

// Data fetch
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

class MainActivity : FragmentActivity() {

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var loginViewModel: LoginViewModel

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var userViewModel: UserViewModel

    private lateinit var supabaseViewModel: SupabaseViewModel

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

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        setupBiometricPrompt()

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        supabaseViewModel = ViewModelProvider(this)[SupabaseViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            WearAboutsTheme {
                val navController = rememberNavController()
                val users by userViewModel.users.collectAsState(initial = emptyList())
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") { 
                            Login(
                                navController = navController,
                                biometricPrompt = biometricPrompt,
                                promptInfo = promptInfo,
                                viewModel = loginViewModel
                            ) 
                        }
                        composable("donation") { Donation().Template(navController, users) }
                        composable("donationMap") { DonationMap().Template(navController, users) }
                        composable("home") { Home(homeViewModel).Template(navController, users) }
                        
                        composable("clothingdetail/{itemId}") { backStackEntry ->
                            val itemId = backStackEntry.arguments?.getString("itemId")
                            if (itemId != null) {
                                ClothingDetailScreen(homeViewModel, itemId, users).Content(navController)
                            } else {
                                // Handle the null case, maybe show an error or navigate back
                            }
                        }

                        composable("register") { Register(navController) }
                        composable("notifications") { Notifications() }
                        composable("profile") { 
                            Profile(
                                userViewModel = userViewModel,
                                homeViewModel = homeViewModel,
                                supabaseViewModel = supabaseViewModel
                            ).Template(navController, users)
                        }

                        // Unimplemented
                        composable("favourites") { Home(homeViewModel).Template(navController, users) }
                        composable("buybag") { Home(homeViewModel).Template(navController, users) }
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
                    loginViewModel.onBiometricSuccess(this@MainActivity)
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
