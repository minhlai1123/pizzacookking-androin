package com.antigravity.pizzacooking

import com.antigravity.pizzacooking.data.models.CompletionRules
import com.antigravity.pizzacooking.data.models.PresentationMetadata
import com.antigravity.pizzacooking.data.models.Recipe
import com.antigravity.pizzacooking.data.repository.RecipeRepository
import com.antigravity.pizzacooking.data.repository.RecipeValidator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RecipeValidatorTest {

    @Test
    fun validator_allTwentyCatalogRecipesPass() {
        val allRecipes = RecipeRepository.getAllRecipes()
        val result = RecipeValidator.validateAllRecipes(allRecipes)
        assertTrue("All 20 catalog recipes must be valid: ${result.errors}", result.isValid)
        assertTrue("Validation errors list must be empty", result.errors.isEmpty())
    }

    @Test
    fun validator_catchesInvalidRecipeId() {
        val validRecipe = RecipeRepository.getRecipeById("R-01")!!
        val invalidRecipe = validRecipe.copy(recipeId = "R-99")
        val result = RecipeValidator.validateRecipe(invalidRecipe)

        assertFalse("Recipe with invalid ID R-99 must fail", result.isValid)
        assertTrue(result.errors.any { it.contains("Invalid recipeId format") })
    }

    @Test
    fun validator_catchesNonExistentCrust() {
        val validRecipe = RecipeRepository.getRecipeById("R-01")!!
        val invalidRecipe = validRecipe.copy(crustId = "ING-999")
        val result = RecipeValidator.validateRecipe(invalidRecipe)

        assertFalse("Recipe with non-existent crust must fail", result.isValid)
        assertTrue(result.errors.any { it.contains("Crust ING-999 does not exist") })
    }

    @Test
    fun validator_catchesNonExistentTool() {
        val validRecipe = RecipeRepository.getRecipeById("R-01")!!
        val invalidRecipe = validRecipe.copy(toolIds = listOf("TOOL-999"))
        val result = RecipeValidator.validateRecipe(invalidRecipe)

        assertFalse("Recipe with non-existent tool must fail", result.isValid)
        assertTrue(result.errors.any { it.contains("Tool TOOL-999 does not exist") })
    }

    @Test
    fun validator_catchesInvalidDifficulty() {
        val validRecipe = RecipeRepository.getRecipeById("R-01")!!
        val invalidRecipe = validRecipe.copy(difficulty = 9)
        val result = RecipeValidator.validateRecipe(invalidRecipe)

        assertFalse("Recipe with difficulty 9 must fail", result.isValid)
        assertTrue(result.errors.any { it.contains("difficulty must be between 1 and 5") })
    }

    @Test
    fun validator_catchesNoBakeInconsistency() {
        val validRecipe = RecipeRepository.getRecipeById("R-03")!!
        assertTrue(validRecipe.isNoBake)

        // Force inconsistent state: isNoBake = true, but requiresOven = true
        val inconsistentRecipe = validRecipe.copy(
            completionRules = validRecipe.completionRules.copy(requiresOven = true)
        )
        val result = RecipeValidator.validateRecipe(inconsistentRecipe)

        assertFalse("No-bake recipe requiring oven must fail validation", result.isValid)
        assertTrue(result.errors.any { it.contains("No-bake recipe R-03 cannot have requiresOven = true") })
    }
}
