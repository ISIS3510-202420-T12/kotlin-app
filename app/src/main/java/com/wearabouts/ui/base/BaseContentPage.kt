package com.wearabouts.ui.base

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.navigation.NavController

import com.wearabouts.ui.components.NavBar
import com.wearabouts.ui.components.Header

abstract class BaseContentPage {

    @Composable
    fun Template(navController: NavController) {
        Column(modifier = Modifier.fillMaxSize()) {
            Header()
            Content()
            NavBar(navController)
        }
    }

    @Composable
    protected abstract fun Content()
}