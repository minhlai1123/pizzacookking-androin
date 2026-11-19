package com.antigravity.pizzacooking.data.models

data class Tool(
    val toolId: String,
    val name: String,
    val category: String,
    val supportedActions: List<String>,
    val compatibleIngredients: List<String>,
    val interactionType: String
)
