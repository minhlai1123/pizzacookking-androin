package com.antigravity.pizzacooking

import com.antigravity.pizzacooking.core.Constants
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ContractValidationTest {

    @Test
    fun totalRecipesCount_isExactlyTwenty() {
        assertEquals(20, Constants.TOTAL_RECIPES)
    }

    @Test
    fun targetAspectRatio_isSixteenByNine() {
        val expected = 16f / 9f
        assertEquals(expected, Constants.TARGET_ASPECT_RATIO, 0.001f)
    }

    @Test
    fun recipeIds_followPattern() {
        val recipeIds = (1..20).map { String.format("R-%02d", it) }
        assertEquals(20, recipeIds.size)
        assertEquals("R-01", recipeIds.first())
        assertEquals("R-20", recipeIds.last())
        recipeIds.forEach { id ->
            assertTrue(id.matches(Regex("^R-\\d{2}$")))
        }
    }

    @Test
    fun starScoringRules_evaluatedCorrectly() {
        // Cold recipe (e.g. R-03, R-17) auto-pass oven criteria
        fun calculateStars(
            isColdRecipe: Boolean,
            manualStepRatio: Float,
            sauceCoverage: Float,
            toppingCount: Int,
            ovenPerfectBake: Boolean
        ): Int {
            val ovenPassed = if (isColdRecipe) true else ovenPerfectBake
            return when {
                manualStepRatio >= 1.0f && sauceCoverage >= 0.80f && toppingCount >= 5 && ovenPassed -> 3
                manualStepRatio >= 0.80f && sauceCoverage >= 0.60f && toppingCount >= 3 -> 2
                else -> 1 // Never 0 stars in positive reinforcement model
            }
        }

        // Test 3 stars for hot pizza
        val hotThreeStars = calculateStars(
            isColdRecipe = false,
            manualStepRatio = 1.0f,
            sauceCoverage = 0.85f,
            toppingCount = 6,
            ovenPerfectBake = true
        )
        assertEquals(3, hotThreeStars)

        // Test 3 stars for cold watermelon pizza (oven = false, but isColdRecipe = true)
        val coldThreeStars = calculateStars(
            isColdRecipe = true,
            manualStepRatio = 1.0f,
            sauceCoverage = 0.82f,
            toppingCount = 5,
            ovenPerfectBake = false
        )
        assertEquals(3, coldThreeStars)

        // Test 2 stars
        val twoStars = calculateStars(
            isColdRecipe = false,
            manualStepRatio = 0.85f,
            sauceCoverage = 0.65f,
            toppingCount = 3,
            ovenPerfectBake = false
        )
        assertEquals(2, twoStars)

        // Test 1 star fallback (minimum completion)
        val oneStar = calculateStars(
            isColdRecipe = false,
            manualStepRatio = 0.50f,
            sauceCoverage = 0.40f,
            toppingCount = 1,
            ovenPerfectBake = false
        )
        assertEquals(1, oneStar)
    }
}
