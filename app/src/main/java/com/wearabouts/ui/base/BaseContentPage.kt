package com.wearabouts.ui.base

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.wearabouts.ui.components.NavBar
import com.wearabouts.ui.components.Header
import androidx.compose.ui.graphics.Color

// Android management
import androidx.compose.ui.platform.LocalContext

// To highlight current page
import androidx.navigation.compose.currentBackStackEntryAsState

// Objects
import com.wearabouts.ui.login.SecureStorage

abstract class BaseContentPage {

    var navController: NavController? = null
    var loginInfo: Pair<String, String>? = null

    @Composable
    fun Template(navController: NavController) {
        this.navController = navController
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route ?: ""
        val adjustedRoute = if (currentRoute.contains("clothingdetail")) "clothingdetail" else currentRoute

        // Login info
        val context = LocalContext.current
        loginInfo = SecureStorage.getLoginInfo(context)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Header(navController)
            Content(modifier = Modifier)
            NavBar(navController, adjustedRoute, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }

    @Composable
    protected abstract fun Content(modifier: Modifier)

    fun navigate(route: String) {
        navController?.navigate(route)
    }

}