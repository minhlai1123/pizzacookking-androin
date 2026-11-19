package com.antigravity.pizzacooking.domain.scoring

import com.antigravity.pizzacooking.domain.creative.CreativeCookingSession
import com.antigravity.pizzacooking.domain.statemachine.CookingSession

data class ScoreResult(
    val stars: Int,
    val feedbackMessage: String,
    val manualCompletionRate: Float,
    val sauceCoverage: Float,
    val toppingsCount: Int,
    val isOvenPassed: Boolean
)

object ScoringEngine {

    fun calculateRecipeScore(session: CookingSession): ScoreResult {
        val totalSteps = session.totalSteps
        val manualSteps = session.manualStepsCompleted
        val skippedSteps = session.skippedStepsCount
        val manualRate = if (totalSteps > 0) manualSteps.toFloat() / totalSteps.toFloat() else 1.0f

        val sauceCoverage = session.sauceCoverage
        val toppingsCount = session.placedToppings.size

        // For no-bake recipes (R-03, R-17), baking is auto-passed
        val isOvenPassed = if (session.recipe.isNoBake) true else session.isBaked

        val minToppingsRequired = session.recipe.completionRules.minToppings

        // 3 Stars criteria:
        // No skips (100% manual), sauce >= 80%, toppings >= minRequired, oven pass
        val gets3Stars = (skippedSteps == 0) &&
                (sauceCoverage >= 0.80f) &&
                (toppingsCount >= minToppingsRequired) &&
                isOvenPassed

        // 2 Stars criteria:
        // At most 1 skip (or manualRate >= 0.80f), sauce >= 60%, toppings >= minToppingsRequired / 2 (or >= 3)
        val gets2Stars = (skippedSteps <= 1 || manualRate >= 0.80f) &&
                (sauceCoverage >= 0.60f) &&
                (toppingsCount >= 3.coerceAtMost(minToppingsRequired))

        val stars = when {
            gets3Stars -> 3
            gets2Stars -> 2
            else -> 1 // Never 0 stars! Every child deserves positive reward!
        }

        val feedback = when (stars) {
            3 -> "Master Chef! Perfect delicious pizza!"
            2 -> "Great Job! Looks yummy and smells wonderful!"
            else -> "Super Fun! You made a wonderful pizza!"
        }

        return ScoreResult(
            stars = stars,
            feedbackMessage = feedback,
            manualCompletionRate = manualRate,
            sauceCoverage = sauceCoverage,
            toppingsCount = toppingsCount,
            isOvenPassed = isOvenPassed
        )
    }

    fun calculateCreativeScore(session: CreativeCookingSession): ScoreResult {
        // Creative mode always awards 3 stars to encourage boundless imagination
        return ScoreResult(
            stars = 3,
            feedbackMessage = "Incredible Creation! You are a brilliant pizza artist!",
            manualCompletionRate = 1.0f,
            sauceCoverage = 1.0f,
            toppingsCount = session.placedToppings.size,
            isOvenPassed = true
        )
    }
}
