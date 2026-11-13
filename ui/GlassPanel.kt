package com.example.codeeditor3d.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun GlassPanel(
    modifier: Modifier = Modifier,
    tiltX: Float = 3f,
    glow: Color = Color(0xFF4F8BFF),
    content: @Composable BoxScope.() -> Unit
) {
    val density = androidx.compose.ui.platform.LocalDensity.current
    Box(
        modifier
            .graphicsLayer {
                rotationX = tiltX
                cameraDistance = with(density) { 40.dp.toPx() }
                shadowElevation = with(density) { 16.dp.toPx() }
                shape = RoundedCornerShape(22.dp)
                clip = false
            }
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF20263A),
                        Color(0xFF12151F),
                        Color(0xFF0C0F17)
                    ),
                    start = Offset.Zero,
                    end = Offset.Infinite
                )
            )
            .border(
                1.dp,
                Brush.linearGradient(
                    listOf(glow.copy(alpha = .55f), Color.Transparent, glow.copy(alpha = .15f))
                ),
                RoundedCornerShape(22.dp)
            )
            .padding(12.dp),
        content = content
    )
}

@Composable
fun AppBackground(content: @Composable BoxScope.() -> Unit) {
    Box(
        Modifier.background(
            Brush.radialGradient(
                colors = listOf(Color(0xFF1A2240), Color(0xFF0B0D14)),
                center = Offset(300f, 200f),
                radius = 1400f
            )
        ),
        content = content
    )
}
