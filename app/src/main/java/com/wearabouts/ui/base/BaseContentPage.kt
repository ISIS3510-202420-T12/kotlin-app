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

// To highlight current page
import androidx.navigation.compose.currentBackStackEntryAsState

abstract class BaseContentPage {

    var navController: NavController? = null

    @Composable
    fun Template(navController: NavController) {
        this.navController = navController
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route ?: ""
        val adjustedRoute = if (currentRoute.contains("clothingdetail")) "clothingdetail" else currentRoute

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Header()
            Content()
            NavBar(navController, adjustedRoute, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }

    @Composable
    protected abstract fun Content()

    fun navigate(route: String) {
        navController?.navigate(route)
    }

}