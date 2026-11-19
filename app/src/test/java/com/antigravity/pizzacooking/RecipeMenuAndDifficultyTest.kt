package com.antigravity.pizzacooking

import com.antigravity.pizzacooking.data.repository.RecipeRepository
import com.antigravity.pizzacooking.domain.statemachine.GameMode
import com.antigravity.pizzacooking.presentation.screens.KitchenViewModel
import com.antigravity.pizzacooking.presentation.screens.RecipeFilterCategory
import com.antigravity.pizzacooking.presentation.screens.RecipeMetadataRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RecipeMenuAndDifficultyTest {

    @Test
    fun all20Recipes_areRegisteredInMetadataWithValidTiers() {
        val allRecipes = RecipeRepository.getAllRecipes()
        assertEquals("Master recipe catalog must contain exactly 20 recipes", 20, allRecipes.size)

        for (recipe in allRecipes) {
            val info = RecipeMetadataRegistry.getInfo(recipe.recipeId)
            assertNotNull(info)
            assertEquals(recipe.recipeId, info.recipeId)
            assertTrue("Vietnamese name must not be blank for ${recipe.recipeId}", info.nameVi.isNotBlank())
            assertTrue("English name must not be blank for ${recipe.recipeId}", info.nameEn.isNotBlank())
            assertTrue("Description must not be blank for ${recipe.recipeId}", info.descriptionVi.isNotBlank())
            assertTrue("Difficulty tier must be between 1 and 3", info.difficultyTier in 1..3)
            assertTrue("Badge label must have star icon", info.difficultyLabelVi.contains("⭐"))
        }
    }

    @Test
    fun recipeDifficultyTiers_areCorrectlyPartitioned() {
        val allRecipes = RecipeRepository.getAllRecipes()

        val easyRecipes = allRecipes.filter { RecipeMetadataRegistry.getInfo(it.recipeId).difficultyTier == 1 }
        val mediumRecipes = allRecipes.filter { RecipeMetadataRegistry.getInfo(it.recipeId).difficultyTier == 2 }
        val hardRecipes = allRecipes.filter { RecipeMetadataRegistry.getInfo(it.recipeId).difficultyTier == 3 }

        assertEquals("Easy tier must have 6 recipes", 6, easyRecipes.size)
        assertEquals("Medium tier must have 9 recipes", 9, mediumRecipes.size)
        assertEquals("Hard tier must have 5 recipes", 5, hardRecipes.size)
        assertEquals("Sum of tiers must equal 20", 20, easyRecipes.size + mediumRecipes.size + hardRecipes.size)

        val noBakeRecipes = allRecipes.filter { it.isNoBake }
        assertEquals("No-Bake category must contain exactly 2 recipes (R-03 Watermelon, R-17 Berry Tart)", 2, noBakeRecipes.size)
        assertTrue(noBakeRecipes.any { it.recipeId == "R-03" })
        assertTrue(noBakeRecipes.any { it.recipeId == "R-17" })
    }

    @Test
    fun kitchenViewModel_setsDifficultyTierFromRecipe() {
        // Easy recipe
        val r01 = RecipeRepository.getRecipeById("R-01")!!
        val vmEasy = KitchenViewModel(initialRecipeId = "R-01")
        assertEquals(1, vmEasy.uiState.value.difficultyTier)

        // Medium recipe
        val r07 = RecipeRepository.getRecipeById("R-07")!!
        vmEasy.startRecipeCooking(r07)
        assertEquals(2, vmEasy.uiState.value.difficultyTier)

        // Hard recipe
        val r20 = RecipeRepository.getRecipeById("R-20")!!
        vmEasy.startRecipeCooking(r20)
        assertEquals(3, vmEasy.uiState.value.difficultyTier)
    }

    @Test
    fun gameplayProgressIncrements_scaleWithDifficulty() {
        // Easy mode gives larger increments (toddler friendly, 3 taps)
        val vmEasy = KitchenViewModel(initialRecipeId = "R-01")
        assertEquals(1, vmEasy.uiState.value.difficultyTier)
        vmEasy.interactDough()
        assertEquals(0.35f, vmEasy.uiState.value.doughRollProgress, 0.01f)

        // Hard mode gives smaller increments (requires more skill/effort)
        val vmHard = KitchenViewModel(initialRecipeId = "R-20")
        assertEquals(3, vmHard.uiState.value.difficultyTier)
        vmHard.interactDough()
        assertEquals(0.16f, vmHard.uiState.value.doughRollProgress, 0.01f)
    }
}
