package com.antigravity.pizzacooking.presentation.screens

import com.antigravity.pizzacooking.data.models.Ingredient
import com.antigravity.pizzacooking.data.models.Recipe
import com.antigravity.pizzacooking.data.models.Tool
import com.antigravity.pizzacooking.domain.statemachine.GameMode
import com.antigravity.pizzacooking.domain.statemachine.PlacedTopping

data class KitchenUiState(
    val gameMode: GameMode = GameMode.RECIPE_MODE,
    val currentRecipe: Recipe? = null,
    val currentStepIndex: Int = 0,
    val totalSteps: Int = 10,
    val currentStepId: String = "STEP-001",
    val currentStepTitle: String = "Dough Kneading",
    val currentStepPrompt: String = "Tap and punch the dough ball!",
    val selectedIngredientId: String? = null,
    val selectedToolId: String? = null,
    val activeCreativeCategory: String = "VEGETABLE",
    val availableIngredients: List<Ingredient> = emptyList(),
    val activeTool: Tool? = null,
    val placedToppings: List<PlacedTopping> = emptyList(),
    val sauceCoverage: Float = 0.0f,
    val cheeseGrated: Float = 0.0f,
    val doughRollProgress: Float = 0.0f,
    val isBaked: Boolean = false,
    val slicesCount: Int = 0,
    val bitesCount: Int = 0,
    val isAudioMuted: Boolean = false,
    val skippedStepsCount: Int = 0,
    val difficultyTier: Int = 1,
    val ovenHeatProgress: Float = 0.0f,
    val isFinished: Boolean = false,
    val scoreStars: Int? = null,
    val feedbackMessage: String = "Welcome to Pizza Kitchen!"
)
