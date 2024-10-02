package com.wearabouts.ui.donation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import com.wearabouts.ui.base.BaseContentPage

class Donation : BaseContentPage() {

    @Composable
    override fun Content() {
        Text(text = "Donations Page")
    }
}