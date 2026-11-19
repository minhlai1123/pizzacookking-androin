package com.antigravity.pizzacooking

import androidx.compose.ui.graphics.toArgb
import com.antigravity.pizzacooking.presentation.theme.BasilFreshGreen
import com.antigravity.pizzacooking.presentation.theme.CheeseYellow
import com.antigravity.pizzacooking.presentation.theme.CreamDoughWhite
import com.antigravity.pizzacooking.presentation.theme.GentleBlue
import com.antigravity.pizzacooking.presentation.theme.GoldenCrust
import com.antigravity.pizzacooking.presentation.theme.WarmKitchenWood
import com.antigravity.pizzacooking.presentation.theme.WarmTomatoRed
import org.junit.Assert.assertEquals
import org.junit.Test

class ThemeAndColorTest {

    @Test
    fun colorPaletteHexValues_matchGddSpecification() {
        assertEquals(0xFFE63946.toInt(), WarmTomatoRed.toArgb())
        assertEquals(0xFFF4A261.toInt(), GoldenCrust.toArgb())
        assertEquals(0xFFFFD166.toInt(), CheeseYellow.toArgb())
        assertEquals(0xFF2A9D8F.toInt(), BasilFreshGreen.toArgb())
        assertEquals(0xFF5C3D2E.toInt(), WarmKitchenWood.toArgb())
        assertEquals(0xFFFDF8F0.toInt(), CreamDoughWhite.toArgb())
        assertEquals(0xFF4EA8DE.toInt(), GentleBlue.toArgb())
    }
}
