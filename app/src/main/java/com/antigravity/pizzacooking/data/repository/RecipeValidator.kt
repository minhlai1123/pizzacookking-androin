package com.antigravity.pizzacooking.data.repository

import com.antigravity.pizzacooking.data.models.Recipe

data class RecipeValidationResult(
    val isValid: Boolean,
    val errors: List<String> = emptyList()
)

object RecipeValidator {

    private val RECIPE_ID_REGEX = Regex("^R-(0[1-9]|1[0-9]|20)$")

    fun validateRecipe(recipe: Recipe): RecipeValidationResult {
        val errors = mutableListOf<String>()

        if (!RECIPE_ID_REGEX.matches(recipe.recipeId)) {
            errors.add("Invalid recipeId format: ${recipe.recipeId}")
        }

        if (recipe.name.isBlank()) {
            errors.add("Recipe name cannot be blank for ${recipe.recipeId}")
        }

        if (recipe.description.isBlank()) {
            errors.add("Recipe description cannot be blank for ${recipe.recipeId}")
        }

        if (recipe.difficulty !in 1..5) {
            errors.add("Recipe difficulty must be between 1 and 5 for ${recipe.recipeId}")
        }

        // Validate crust
        val crust = IngredientRepository.getIngredientById(recipe.crustId)
        if (crust == null) {
            errors.add("Crust ${recipe.crustId} does not exist in IngredientRepository")
        } else if (crust.category != "CRUST") {
            errors.add("Ingredient ${recipe.crustId} is not a CRUST (found ${crust.category})")
        }

        // Validate sauce
        val sauce = IngredientRepository.getIngredientById(recipe.sauceId)
        if (sauce == null) {
            errors.add("Sauce ${recipe.sauceId} does not exist in IngredientRepository")
        } else if (sauce.category != "SAUCE") {
            errors.add("Ingredient ${recipe.sauceId} is not a SAUCE (found ${sauce.category})")
        }

        // Validate cheese (optional)
        if (recipe.cheeseId != null) {
            val cheese = IngredientRepository.getIngredientById(recipe.cheeseId)
            if (cheese == null) {
                errors.add("Cheese ${recipe.cheeseId} does not exist in IngredientRepository")
            } else if (cheese.category != "CHEESE") {
                errors.add("Ingredient ${recipe.cheeseId} is not a CHEESE (found ${cheese.category})")
            }
        }

        // Validate toppings
        if (recipe.toppingIds.isEmpty()) {
            errors.add("Recipe ${recipe.recipeId} must have at least one topping")
        }
        for (toppingId in recipe.toppingIds) {
            val topping = IngredientRepository.getIngredientById(toppingId)
            if (topping == null) {
                errors.add("Topping $toppingId does not exist in IngredientRepository")
            } else if (!topping.category.startsWith("TOPPING") && topping.category != "CHEESE") {
                errors.add("Ingredient $toppingId is not a valid topping or cheese garnish (found ${topping.category})")
            }
        }

        // Validate tools
        if (recipe.toolIds.isEmpty()) {
            errors.add("Recipe ${recipe.recipeId} must require at least one tool")
        }
        for (toolId in recipe.toolIds) {
            if (!ToolRepository.hasTool(toolId)) {
                errors.add("Tool $toolId does not exist in ToolRepository")
            }
        }

        // Validate completion rules
        if (recipe.completionRules.minSauceCoverage !in 0.0f..1.0f) {
            errors.add("minSauceCoverage must be between 0.0 and 1.0 (found ${recipe.completionRules.minSauceCoverage})")
        }
        if (recipe.completionRules.minToppings < 1) {
            errors.add("minToppings must be at least 1 (found ${recipe.completionRules.minToppings})")
        }

        // No-bake consistency
        if (recipe.isNoBake && recipe.completionRules.requiresOven) {
            errors.add("No-bake recipe ${recipe.recipeId} cannot have requiresOven = true")
        }

        return RecipeValidationResult(
            isValid = errors.isEmpty(),
            errors = errors
        )
    }

    fun validateAllRecipes(recipes: List<Recipe>): RecipeValidationResult {
        val allErrors = mutableListOf<String>()

        if (recipes.size != 20) {
            allErrors.add("Expected exactly 20 recipes, but found ${recipes.size}")
        }

        val uniqueIds = recipes.map { it.recipeId }.toSet()
        if (uniqueIds.size != recipes.size) {
            allErrors.add("Duplicate recipe IDs found")
        }

        for (recipe in recipes) {
            val result = validateRecipe(recipe)
            if (!result.isValid) {
                allErrors.addAll(result.errors)
            }
        }

        return RecipeValidationResult(
            isValid = allErrors.isEmpty(),
            errors = allErrors
        )
    }
}
