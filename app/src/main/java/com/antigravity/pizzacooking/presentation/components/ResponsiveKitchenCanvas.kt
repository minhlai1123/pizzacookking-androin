package com.antigravity.pizzacooking.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.antigravity.pizzacooking.presentation.theme.WarmKitchenWood

/**
 * Edge-to-edge landscape canvas with seamless background extension.
 * Eliminates artificial letterboxing while preserving child-friendly touch ergonomics.
 */
@Composable
fun ResponsiveKitchenCanvas(
    modifier: Modifier = Modifier,
    ambientBackgroundColor: Color = WarmKitchenWood,
    content: @Composable (widthDp: Float, heightDp: Float) -> Unit
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(ambientBackgroundColor),
        contentAlignment = Alignment.Center
    ) {
        val screenWidth = maxWidth.value
        val screenHeight = maxHeight.value

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            content(screenWidth, screenHeight)
        }
    }
}
