package com.antigravity.pizzacooking.data.models

data class Ingredient(
    val ingredientId: String,
    val name: String,
    val category: String,
    val visualIdentifier: String,
    val applicableRecipes: List<String>,
    val compatibleTools: List<String>,
    val interactionType: String,
    val preparationRequirements: String
)
