package com.wearabouts.ui.base

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.wearabouts.ui.components.NavBar
import com.wearabouts.ui.components.Header

// To highlight current page
import androidx.navigation.compose.currentBackStackEntryAsState

abstract class BaseContentPage {

    @Composable
    fun Template(navController: NavController) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route ?: ""

        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Header()
                Content()
            }
            NavBar(navController, currentRoute, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }

    @Composable
    protected abstract fun Content()
}