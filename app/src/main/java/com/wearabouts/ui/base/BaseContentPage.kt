package com.wearabouts.ui.base

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.wearabouts.ui.components.NavBar
import com.wearabouts.ui.components.Header

abstract class BaseContentPage {

    @Composable
    fun Template(navController: NavController) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Header()
                Content()
            }
            NavBar(navController, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }

    @Composable
    protected abstract fun Content()
}