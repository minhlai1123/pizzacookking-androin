package com.antigravity.pizzacooking.domain.statemachine

import com.antigravity.pizzacooking.data.models.Recipe
import com.antigravity.pizzacooking.domain.creative.CreativeCookingSession
import com.antigravity.pizzacooking.domain.scoring.ScoreResult
import com.antigravity.pizzacooking.domain.scoring.ScoringEngine

class GameStateMachine(
    initialScreen: ScreenState = ScreenState.SPLASH
) {
    var currentScreen: ScreenState = initialScreen
        private set

    var currentMode: GameMode? = null
        private set

    var activeCookingSession: CookingSession? = null
        private set

    var activeCreativeSession: CreativeCookingSession? = null
        private set

    var lastScoreResult: ScoreResult? = null
        private set

    private val allowedTransitions: Map<ScreenState, Set<ScreenState>> = mapOf(
        ScreenState.SPLASH to setOf(ScreenState.MAIN_MENU),
        ScreenState.MAIN_MENU to setOf(
            ScreenState.RECIPE_SELECTION,
            ScreenState.CREATIVE_MODE,
            ScreenState.SETTINGS_PARENTAL
        ),
        ScreenState.RECIPE_SELECTION to setOf(
            ScreenState.MAIN_MENU,
            ScreenState.RECIPE_PREPARATION
        ),
        ScreenState.RECIPE_PREPARATION to setOf(
            ScreenState.TOPPING_ASSEMBLY,
            ScreenState.RECIPE_SELECTION,
            ScreenState.MAIN_MENU
        ),
        ScreenState.TOPPING_ASSEMBLY to setOf(
            ScreenState.BAKING_OVEN,
            ScreenState.CUTTING_SERVING,
            ScreenState.RECIPE_PREPARATION,
            ScreenState.MAIN_MENU
        ),
        ScreenState.BAKING_OVEN to setOf(
            ScreenState.CUTTING_SERVING,
            ScreenState.MAIN_MENU
        ),
        ScreenState.CUTTING_SERVING to setOf(
            ScreenState.RESULT_EVALUATION,
            ScreenState.MAIN_MENU
        ),
        ScreenState.RESULT_EVALUATION to setOf(
            ScreenState.RECIPE_SELECTION,
            ScreenState.MAIN_MENU,
            ScreenState.RECIPE_PREPARATION,
            ScreenState.CREATIVE_MODE
        ),
        ScreenState.CREATIVE_MODE to setOf(
            ScreenState.CUTTING_SERVING,
            ScreenState.RESULT_EVALUATION,
            ScreenState.MAIN_MENU
        ),
        ScreenState.SETTINGS_PARENTAL to setOf(ScreenState.MAIN_MENU)
    )

    fun canTransitionTo(target: ScreenState): Boolean {
        return allowedTransitions[currentScreen]?.contains(target) == true
    }

    fun transitionTo(target: ScreenState): Boolean {
        if (canTransitionTo(target)) {
            currentScreen = target
            return true
        }
        return false
    }

    fun startRecipe(recipe: Recipe): Boolean {
        val session = CookingSession(recipe)
        session.startSession()
        activeCookingSession = session
        activeCreativeSession = null
        currentMode = GameMode.RECIPE_MODE
        return transitionTo(ScreenState.RECIPE_PREPARATION)
    }

    fun startCreativeMode(): Boolean {
        activeCreativeSession = CreativeCookingSession()
        activeCookingSession = null
        currentMode = GameMode.CREATIVE_MODE
        return transitionTo(ScreenState.CREATIVE_MODE)
    }

    fun advanceCookingStep(): Boolean {
        val session = activeCookingSession ?: return false
        val moved = session.moveToNextStep()
        if (moved) {
            // Check step type and transition screen accordingly
            when (session.currentStep?.stepType) {
                com.antigravity.pizzacooking.data.models.StepType.DOUGH_PREPARATION,
                com.antigravity.pizzacooking.data.models.StepType.CRUST_ROLLING,
                com.antigravity.pizzacooking.data.models.StepType.ALTERNATIVE_CRUST -> {
                    transitionTo(ScreenState.RECIPE_PREPARATION)
                }
                com.antigravity.pizzacooking.data.models.StepType.SAUCE_SPREADING,
                com.antigravity.pizzacooking.data.models.StepType.CHEESE_GRATING,
                com.antigravity.pizzacooking.data.models.StepType.TOPPING_DECORATION -> {
                    transitionTo(ScreenState.TOPPING_ASSEMBLY)
                }
                com.antigravity.pizzacooking.data.models.StepType.OVEN_BAKING -> {
                    transitionTo(ScreenState.BAKING_OVEN)
                }
                com.antigravity.pizzacooking.data.models.StepType.SLICING_AND_EATING -> {
                    transitionTo(ScreenState.CUTTING_SERVING)
                }
                else -> { /* stay on current screen */ }
            }
        } else {
            // Cooking steps completed, go to cutting or evaluation
            if (currentScreen != ScreenState.CUTTING_SERVING && currentScreen != ScreenState.RESULT_EVALUATION) {
                transitionTo(ScreenState.CUTTING_SERVING)
            }
        }
        return moved
    }

    fun skipCurrentStep(): Boolean {
        val session = activeCookingSession ?: return false
        val skipped = session.skipCurrentStep()
        if (skipped) {
            advanceCookingStep()
        } else if (session.isFinished) {
            transitionTo(ScreenState.CUTTING_SERVING)
        }
        return skipped
    }

    fun applyToddlerTap() {
        activeCookingSession?.applyToddlerTap()
    }

    fun finishSession(): ScoreResult? {
        // Anti-double scoring guard: if session has already been finished, return cached result
        if (lastScoreResult != null) {
            return lastScoreResult
        }

        when (currentMode) {
            GameMode.RECIPE_MODE -> {
                val session = activeCookingSession ?: return null
                val result = ScoringEngine.calculateRecipeScore(session)
                lastScoreResult = result
                transitionTo(ScreenState.RESULT_EVALUATION)
                return result
            }
            GameMode.CREATIVE_MODE -> {
                val session = activeCreativeSession ?: return null
                session.finishCreativePizza()
                val result = ScoringEngine.calculateCreativeScore(session)
                lastScoreResult = result
                transitionTo(ScreenState.RESULT_EVALUATION)
                return result
            }
            null -> return null
        }
    }

    fun resetToMainMenu() {
        activeCookingSession = null
        activeCreativeSession = null
        currentMode = null
        lastScoreResult = null
        currentScreen = ScreenState.MAIN_MENU
    }
}
