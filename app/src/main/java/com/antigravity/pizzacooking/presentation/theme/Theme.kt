package com.antigravity.pizzacooking.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = WarmTomatoRed,
    secondary = GoldenCrust,
    tertiary = BasilFreshGreen,
    background = CreamDoughWhite,
    surface = CreamDoughWhite,
    onPrimary = CreamDoughWhite,
    onSecondary = DarkCharcoal,
    onTertiary = CreamDoughWhite,
    onBackground = DarkCharcoal,
    onSurface = DarkCharcoal
)

@Composable
fun PizzaCookingWorldTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
