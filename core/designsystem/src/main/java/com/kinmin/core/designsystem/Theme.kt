package com.kinmin.core.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Palette taken from the Kinmin mockups.
val KinminGreen = Color(0xFF1F6E5E)
val KinminGreenDark = Color(0xFF14513F)
val KinminOrange = Color(0xFFD2562F)
val KinminCream = Color(0xFFFFF6E0)
val KinminGrey = Color(0xFF6B6B6B)

private val LightColors = lightColorScheme(
    primary = KinminGreen,
    onPrimary = Color.White,
    secondary = KinminOrange,
    onSecondary = Color.White,
    surfaceVariant = KinminCream,
    background = Color.White
)

@Composable
fun KinminTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = LightColors, content = content)
}
