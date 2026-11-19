package com.antigravity.pizzacooking

import com.antigravity.pizzacooking.data.repository.RecipeRepository
import com.antigravity.pizzacooking.domain.statemachine.GameMode
import com.antigravity.pizzacooking.domain.statemachine.GameStateMachine
import com.antigravity.pizzacooking.domain.statemachine.ScreenState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GameStateMachineTest {

    @Test
    fun stateMachine_initialStateIsSplash() {
        val sm = GameStateMachine()
        assertEquals(ScreenState.SPLASH, sm.currentScreen)
        assertNull(sm.currentMode)
        assertNull(sm.activeCookingSession)
    }

    @Test
    fun stateMachine_validLinearTransitionsSucceed() {
        val sm = GameStateMachine()
        assertTrue("Splash -> MainMenu must succeed", sm.transitionTo(ScreenState.MAIN_MENU))
        assertEquals(ScreenState.MAIN_MENU, sm.currentScreen)

        assertTrue("MainMenu -> RecipeSelection must succeed", sm.transitionTo(ScreenState.RECIPE_SELECTION))
        assertEquals(ScreenState.RECIPE_SELECTION, sm.currentScreen)

        val recipe = RecipeRepository.getRecipeById("R-01")!!
        assertTrue("Starting recipe must succeed and move to RecipePreparation", sm.startRecipe(recipe))
        assertEquals(ScreenState.RECIPE_PREPARATION, sm.currentScreen)
        assertEquals(GameMode.RECIPE_MODE, sm.currentMode)
        assertNotNull(sm.activeCookingSession)
    }

    @Test
    fun stateMachine_illegalTransitionsAreRejected() {
        val sm = GameStateMachine()
        // Cannot jump directly from SPLASH to BAKING_OVEN
        assertFalse("Splash -> BakingOven must fail", sm.transitionTo(ScreenState.BAKING_OVEN))
        assertEquals(ScreenState.SPLASH, sm.currentScreen)

        sm.transitionTo(ScreenState.MAIN_MENU)
        // Cannot jump directly from MAIN_MENU to RESULT_EVALUATION
        assertFalse("MainMenu -> ResultEvaluation must fail", sm.transitionTo(ScreenState.RESULT_EVALUATION))
        assertEquals(ScreenState.MAIN_MENU, sm.currentScreen)
    }

    @Test
    fun stateMachine_stepAdvancementAndSkipFlow() {
        val sm = GameStateMachine(ScreenState.MAIN_MENU)
        sm.transitionTo(ScreenState.RECIPE_SELECTION)
        val recipe = RecipeRepository.getRecipeById("R-01")!!
        sm.startRecipe(recipe)

        val session = sm.activeCookingSession!!
        assertEquals(0, session.currentStepIndex)

        // Skip current step
        assertTrue(sm.skipCurrentStep())
        assertEquals("Skipped steps count should be 1", 1, session.skippedStepsCount)
    }

    @Test
    fun stateMachine_toddlerTapFallbackAddsProgress() {
        val sm = GameStateMachine(ScreenState.MAIN_MENU)
        sm.transitionTo(ScreenState.RECIPE_SELECTION)
        val recipe = RecipeRepository.getRecipeById("R-01")!!
        sm.startRecipe(recipe)

        val session = sm.activeCookingSession!!
        assertEquals(0, session.toddlerTapCount)
        assertEquals(0.0f, session.currentStepProgress, 0.001f)

        sm.applyToddlerTap()
        assertEquals(1, session.toddlerTapCount)
        assertEquals(0.25f, session.currentStepProgress, 0.001f)

        sm.applyToddlerTap()
        sm.applyToddlerTap()
        assertEquals(0.75f, session.currentStepProgress, 0.001f)
    }

    @Test
    fun stateMachine_antiDoubleScoringPreventsRepeatedEvaluation() {
        val sm = GameStateMachine(ScreenState.MAIN_MENU)
        sm.transitionTo(ScreenState.RECIPE_SELECTION)
        val recipe = RecipeRepository.getRecipeById("R-01")!!
        sm.startRecipe(recipe)

        // Complete steps
        while (!sm.activeCookingSession!!.isFinished) {
            sm.activeCookingSession!!.completeCurrentStep(manual = true)
            sm.advanceCookingStep()
        }

        // First finish call
        val result1 = sm.finishSession()
        assertNotNull(result1)
        assertEquals(ScreenState.RESULT_EVALUATION, sm.currentScreen)

        // Second finish call on the same finished session
        val result2 = sm.finishSession()
        assertNotNull(result2)
        // Must return the exact same cached instance without recalculating
        assertTrue("Subsequent finishSession call must return cached result", result1 === result2)
    }
}
