package com.antigravity.pizzacooking.data.models

enum class StepType {
    DOUGH_PREPARATION,
    CRUST_ROLLING,
    INGREDIENT_SLICING,
    SAUCE_SIMMERING,
    ALTERNATIVE_CRUST,
    SAUCE_SPREADING,
    CHEESE_GRATING,
    TOPPING_DECORATION,
    OVEN_BAKING,
    SLICING_AND_EATING
}

data class CookingStep(
    val stepId: String,
    val order: Int,
    val stepType: StepType,
    val title: String,
    val description: String,
    val requiredIngredients: List<String>,
    val requiredTools: List<String>,
    val expectedInteraction: String,
    val canSkip: Boolean = true
)
