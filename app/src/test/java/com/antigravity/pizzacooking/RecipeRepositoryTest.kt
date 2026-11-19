package com.antigravity.pizzacooking

import com.antigravity.pizzacooking.data.repository.RecipeRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RecipeRepositoryTest {

    @Test
    fun repository_containsExactlyTwentyRecipes() {
        val allRecipes = RecipeRepository.getAllRecipes()
        assertEquals("Master recipe catalog must contain exactly 20 recipes", 20, allRecipes.size)
    }

    @Test
    fun repository_recipeIdsAreUniqueAndInRangeR01ToR20() {
        val allRecipes = RecipeRepository.getAllRecipes()
        val ids = allRecipes.map { it.recipeId }
        val uniqueIds = ids.toSet()

        assertEquals("All recipe IDs must be unique", 20, uniqueIds.size)

        for (i in 1..20) {
            val expectedId = String.format("R-%02d", i)
            assertTrue("Recipe ID $expectedId must exist in catalog", uniqueIds.contains(expectedId))
            val recipe = RecipeRepository.getRecipeById(expectedId)
            assertNotNull("Recipe for $expectedId must not be null", recipe)
        }
    }

    @Test
    fun repository_noBakeRecipesCorrectlyIdentified() {
        val noBake = RecipeRepository.getNoBakeRecipes()
        val noBakeIds = noBake.map { it.recipeId }.toSet()

        assertTrue("R-03 (Summer Watermelon) must be no-bake", noBakeIds.contains("R-03"))
        assertTrue("R-17 (Berry Tart) must be no-bake", noBakeIds.contains("R-17"))

        val r01 = RecipeRepository.getRecipeById("R-01")
        assertNotNull(r01)
        assertFalse("R-01 (Margherita) must require baking", r01!!.isNoBake)
        assertTrue("R-01 must require oven", r01.completionRules.requiresOven)
    }

    @Test
    fun repository_recipeDetailsArePopulated() {
        for (recipe in RecipeRepository.getAllRecipes()) {
            assertTrue("Recipe ${recipe.recipeId} must have non-empty name", recipe.name.isNotBlank())
            assertTrue("Recipe ${recipe.recipeId} must have non-empty description", recipe.description.isNotBlank())
            assertTrue("Recipe ${recipe.recipeId} must have difficulty in 1..5", recipe.difficulty in 1..5)
            assertTrue("Recipe ${recipe.recipeId} must have at least one topping", recipe.toppingIds.isNotEmpty())
            assertTrue("Recipe ${recipe.recipeId} must have tools", recipe.toolIds.isNotEmpty())
        }
    }
}
