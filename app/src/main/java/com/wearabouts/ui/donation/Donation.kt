package com.wearabouts.ui.donation

import androidx.compose.material3.Text

// Important libs
import com.wearabouts.R
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background

// Other classes
import com.wearabouts.ui.base.BaseContentPage

// Colors
import com.wearabouts.ui.theme.Primary
import com.wearabouts.ui.theme.Font

class Donation : BaseContentPage() {

    @Composable
    override fun Content() {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(Primary)
            ) {
                Text("Search here for donation places")
            }

        }
    }
}