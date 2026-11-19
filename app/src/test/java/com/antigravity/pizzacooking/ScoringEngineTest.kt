package com.antigravity.pizzacooking

import com.antigravity.pizzacooking.data.repository.RecipeRepository
import com.antigravity.pizzacooking.domain.creative.CreativeCookingSession
import com.antigravity.pizzacooking.domain.scoring.ScoringEngine
import com.antigravity.pizzacooking.domain.statemachine.CookingSession
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ScoringEngineTest {

    @Test
    fun scoring_perfectRecipeExecution_awardsThreeStars() {
        val recipe = RecipeRepository.getRecipeById("R-01")!!
        val session = CookingSession(recipe)
        session.startSession()

        // Complete all steps manually
        while (!session.isFinished) {
            session.completeCurrentStep(manual = true)
            session.moveToNextStep()
        }

        // Apply criteria: sauce >= 80%, toppings >= min, baked = true
        session.recordSauceCoverage(0.85f)
        session.addTopping("ING-030", 0.5f, 0.5f)
        session.addTopping("ING-030", 0.3f, 0.4f)
        session.addTopping("ING-030", 0.7f, 0.6f)
        session.completeBaking()

        val result = ScoringEngine.calculateRecipeScore(session)
        assertEquals("Perfect manual completion must award 3 stars", 3, result.stars)
        assertTrue(result.feedbackMessage.contains("Master Chef"))
    }

    @Test
    fun scoring_noBakeRecipe_autoPassesOvenRequirement() {
        val noBakeRecipe = RecipeRepository.getRecipeById("R-03")!! // Watermelon pizza
        assertTrue(noBakeRecipe.isNoBake)

        val session = CookingSession(noBakeRecipe)
        session.startSession()

        // Complete steps manually
        while (!session.isFinished) {
            session.completeCurrentStep(manual = true)
            session.moveToNextStep()
        }

        session.recordSauceCoverage(0.85f)
        session.addTopping("ING-050", 0.5f, 0.5f)
        session.addTopping("ING-051", 0.3f, 0.4f)
        session.addTopping("ING-052", 0.7f, 0.6f)
        session.addTopping("ING-063", 0.2f, 0.8f)
        // Notice we do NOT call session.completeBaking() because it's no-bake!

        val result = ScoringEngine.calculateRecipeScore(session)
        assertEquals("No-bake recipe must award 3 stars without baking", 3, result.stars)
        assertTrue("isOvenPassed must be true for no-bake", result.isOvenPassed)
    }

    @Test
    fun scoring_minorSkips_awardsTwoStars() {
        val recipe = RecipeRepository.getRecipeById("R-01")!!
        val session = CookingSession(recipe)
        session.startSession()

        // 1 skip, rest manual
        session.completeCurrentStep(manual = false) // 1 skip
        session.moveToNextStep()

        while (!session.isFinished) {
            session.completeCurrentStep(manual = true)
            session.moveToNextStep()
        }

        session.recordSauceCoverage(0.65f)
        session.addTopping("ING-030", 0.5f, 0.5f)
        session.addTopping("ING-030", 0.3f, 0.4f)
        session.addTopping("ING-030", 0.7f, 0.6f)

        val result = ScoringEngine.calculateRecipeScore(session)
        assertEquals("Minor skip should award 2 stars", 2, result.stars)
    }

    @Test
    fun scoring_neverAwardsZeroStars() {
        val recipe = RecipeRepository.getRecipeById("R-01")!!
        val session = CookingSession(recipe)
        session.startSession()

        // Skip all steps, 0 sauce, 0 toppings
        while (!session.isFinished) {
            session.completeCurrentStep(manual = false)
            session.moveToNextStep()
        }

        val result = ScoringEngine.calculateRecipeScore(session)
        assertEquals("Even with full skips, minimum score is 1 star (never 0 stars for kids)", 1, result.stars)
    }

    @Test
    fun scoring_creativeMode_alwaysAwardsThreeStars() {
        val session = CreativeCookingSession()
        session.selectCrust("ING-001")
        session.selectSauce("ING-011")
        session.addTopping("ING-030", 0.5f, 0.5f)
        session.finishCreativePizza()

        val result = ScoringEngine.calculateCreativeScore(session)
        assertEquals("Creative mode must always award 3 stars", 3, result.stars)
    }
}
