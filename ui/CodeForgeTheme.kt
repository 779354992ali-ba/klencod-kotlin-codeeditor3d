package com.example.codeeditor3d.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary = Color(0xFF4F8BFF),
    onPrimary = Color.White,
    secondary = Color(0xFF8B5CF6),
    background = Color(0xFF0B0D14),
    surface = Color(0xFF141824),
    onSurface = Color(0xFFE6E9F0),
    error = Color(0xFFFF5A6D)
)

@Composable
fun CodeForgeTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = DarkColors, content = content)
}
