package com.antigravity.pizzacooking

import com.antigravity.pizzacooking.core.Constants
import org.junit.Assert.assertEquals
import org.junit.Test

class ResponsiveCanvasTest {

    private fun calculateCanvasDimensions(screenWidth: Float, screenHeight: Float): Pair<Float, Float> {
        val currentRatio = screenWidth / screenHeight
        val targetRatio = Constants.TARGET_ASPECT_RATIO

        return if (currentRatio > targetRatio) {
            // Wider phone (e.g. 19.5:9): Fit by height
            val canvasHeight = screenHeight
            val canvasWidth = screenHeight * targetRatio
            Pair(canvasWidth, canvasHeight)
        } else {
            // Squarer tablet (e.g. 4:3): Fit by width
            val canvasWidth = screenWidth
            val canvasHeight = screenWidth / targetRatio
            Pair(canvasWidth, canvasHeight)
        }
    }

    @Test
    fun standardSixteenByNine_matchesExactly() {
        val (width, height) = calculateCanvasDimensions(1920f, 1080f)
        assertEquals(1920f, width, 0.01f)
        assertEquals(1080f, height, 0.01f)
        assertEquals(Constants.TARGET_ASPECT_RATIO, width / height, 0.001f)
    }

    @Test
    fun ultrawidePhone_fitsByHeight_aspectRatioPreserved() {
        // iPhone 15 Pro / Galaxy S24: 19.5:9 -> e.g. 2340 x 1080
        val (width, height) = calculateCanvasDimensions(2340f, 1080f)
        assertEquals(1080f, height, 0.01f)
        assertEquals(1080f * (16f / 9f), width, 0.01f)
        assertEquals(Constants.TARGET_ASPECT_RATIO, width / height, 0.001f)
    }

    @Test
    fun tabletFourByThree_fitsByWidth_aspectRatioPreserved() {
        // iPad 4:3 -> e.g. 2048 x 1536
        val (width, height) = calculateCanvasDimensions(2048f, 1536f)
        assertEquals(2048f, width, 0.01f)
        assertEquals(2048f / (16f / 9f), height, 0.01f)
        assertEquals(Constants.TARGET_ASPECT_RATIO, width / height, 0.001f)
    }
}
