package com.wearabouts.ui.theme

import androidx.compose.ui.graphics.Color
import android.graphics.Color.parseColor

fun String.toColor() = Color(android.graphics.Color.parseColor(this))

val IconColor = "#225779".toColor()
val Primary = "#37A3A5".toColor()
val Font = "#FFFDE8".toColor()
val Transparent = "#00000000".toColor()
val BlackFont = "#000000".toColor()
val White = "#FFFFFF".toColor()
val Cream = "#FFFBF1".toColor()
val Emerald = "#50C878".toColor()