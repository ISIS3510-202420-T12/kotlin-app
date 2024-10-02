package com.wearabouts.ui.theme

import androidx.compose.ui.graphics.Color
import android.graphics.Color.parseColor

fun String.toColor() = Color(android.graphics.Color.parseColor(this))

val IconColor = "#225779".toColor()
val NavbarBgColor = "#37A3A5".toColor()