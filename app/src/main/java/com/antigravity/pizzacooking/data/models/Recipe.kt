package com.antigravity.pizzacooking.data.models

data class CompletionRules(
    val minSauceCoverage: Float,
    val minToppings: Int,
    val requiresOven: Boolean
)

data class PresentationMetadata(
    val finishedDishLook: String,
    val backgroundTheme: String
)

data class Recipe(
    val recipeId: String,
    val name: String,
    val description: String,
    val category: String,
    val difficulty: Int,
    val isNoBake: Boolean,
    val crustId: String,
    val sauceId: String,
    val cheeseId: String?,
    val toppingIds: List<String>,
    val toolIds: List<String>,
    val specialMechanics: String,
    val completionRules: CompletionRules,
    val presentationMetadata: PresentationMetadata
)
