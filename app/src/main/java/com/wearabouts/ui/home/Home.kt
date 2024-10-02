package com.wearabouts.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import com.wearabouts.ui.base.BaseContentPage

class Home : BaseContentPage() {

    @Composable
    override fun Content() {
        Text(text = "Home Page")
    }
}