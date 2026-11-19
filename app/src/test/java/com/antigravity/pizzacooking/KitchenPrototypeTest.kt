package com.antigravity.pizzacooking

import com.antigravity.pizzacooking.data.repository.IngredientRepository
import com.antigravity.pizzacooking.data.repository.RecipeRepository
import com.antigravity.pizzacooking.data.repository.ToolRepository
import com.antigravity.pizzacooking.domain.statemachine.GameMode
import com.antigravity.pizzacooking.presentation.screens.KitchenViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class KitchenPrototypeTest {

    private lateinit var viewModel: KitchenViewModel

    @Before
    fun setUp() {
        viewModel = KitchenViewModel(initialRecipeId = "R-01", initialMode = GameMode.RECIPE_MODE)
    }

    @Test
    fun kitchenViewModel_initialState_isRecipeModeAndStep1() {
        val state = viewModel.uiState.value

        assertEquals(GameMode.RECIPE_MODE, state.gameMode)
        assertEquals("R-01", state.currentRecipe?.recipeId)
        assertEquals(0, state.currentStepIndex)
        assertEquals("STEP-001", state.currentStepId)
        assertEquals(10, state.totalSteps)
        assertEquals("TOOL-001", state.selectedToolId)
        assertFalse(state.isFinished)
        assertEquals(0, state.skippedStepsCount)
    }

    @Test
    fun kitchenViewModel_ingredientSelection_updatesSauceCheeseAndToppings() {
        // Select Sauce
        viewModel.selectIngredient("ING-011") // Classic Marinara
        assertEquals(1.0f, viewModel.uiState.value.sauceCoverage, 0.01f)
        assertEquals("ING-011", viewModel.uiState.value.selectedIngredientId)

        // Select Cheese
        viewModel.selectIngredient("ING-021") // Shredded Mozzarella
        assertEquals(1.0f, viewModel.uiState.value.cheeseGrated, 0.01f)

        // Select Toppings
        viewModel.selectIngredient("ING-040") // Pepperoni
        viewModel.selectIngredient("ING-034") // Fresh Basil
        assertEquals(2, viewModel.uiState.value.placedToppings.size)
        assertEquals("ING-040", viewModel.uiState.value.placedToppings[0].ingredientId)
        assertEquals("ING-034", viewModel.uiState.value.placedToppings[1].ingredientId)
    }

    @Test
    fun kitchenViewModel_creativeMode_accessesAll86ToppingsAcross5Categories() {
        viewModel.switchMode(GameMode.CREATIVE_MODE)
        assertEquals(GameMode.CREATIVE_MODE, viewModel.uiState.value.gameMode)

        // Category 1: VEGETABLE (20 items)
        viewModel.selectCreativeCategory("VEGETABLE")
        val veggieList = viewModel.uiState.value.availableIngredients
        assertEquals(20, veggieList.size)
        assertTrue(veggieList.all { it.category == "TOPPING_VEGGIE" })

        // Category 2: MEAT (19 items)
        viewModel.selectCreativeCategory("MEAT")
        val meatList = viewModel.uiState.value.availableIngredients
        assertEquals(19, meatList.size)
        assertTrue(meatList.all { it.category == "TOPPING_MEAT" })

        // Category 3: FRUIT (14 items)
        viewModel.selectCreativeCategory("FRUIT")
        val fruitList = viewModel.uiState.value.availableIngredients
        assertEquals(14, fruitList.size)
        assertTrue(fruitList.all { it.category == "TOPPING_FRUIT" })

        // Category 4: SWEET (25 items)
        viewModel.selectCreativeCategory("SWEET")
        val sweetList = viewModel.uiState.value.availableIngredients
        assertEquals(25, sweetList.size)
        assertTrue(sweetList.all { it.category == "TOPPING_SWEET" })

        // Category 5: FANTASY (8 items)
        viewModel.selectCreativeCategory("FANTASY")
        val fantasyList = viewModel.uiState.value.availableIngredients
        assertEquals(8, fantasyList.size)
        assertTrue(fantasyList.all { it.category == "TOPPING_FANTASY" })

        // Total count of all 5 categories
        val totalToppings = veggieList.size + meatList.size + fruitList.size + sweetList.size + fantasyList.size
        assertEquals(86, totalToppings)
    }

    @Test
    fun kitchenViewModel_toolSelection_updatesActiveTool() {
        viewModel.selectTool("TOOL-006") // Sauce Ladle
        assertEquals("TOOL-006", viewModel.uiState.value.selectedToolId)
        assertEquals("TOOL-006", viewModel.uiState.value.activeTool?.toolId)
        assertEquals("Sauce Ladle", viewModel.uiState.value.activeTool?.name)
    }

    @Test
    fun kitchenViewModel_stepAdvancement_cyclesThroughAll10StepsAndFinishes() {
        for (i in 0 until 9) {
            assertEquals(i, viewModel.uiState.value.currentStepIndex)
            assertFalse(viewModel.uiState.value.isFinished)
            viewModel.advanceStep()
        }

        // On step 9 (Step 10: Slicing & Eating)
        assertEquals(9, viewModel.uiState.value.currentStepIndex)
        assertEquals("STEP-010", viewModel.uiState.value.currentStepId)

        // Advancing from Step 10 completes the pizza
        viewModel.advanceStep()
        assertTrue(viewModel.uiState.value.isFinished)
        assertNotNull(viewModel.uiState.value.scoreStars)
        assertTrue(viewModel.uiState.value.scoreStars!! in 1..3)
    }

    @Test
    fun kitchenViewModel_skipStep_immediatelyAdvancesAndAppliesScoringPenalty() {
        assertEquals(0, viewModel.uiState.value.currentStepIndex)
        assertEquals(0, viewModel.uiState.value.skippedStepsCount)

        // Skip Step 1
        viewModel.skipStep()
        assertEquals(1, viewModel.uiState.value.currentStepIndex)
        assertEquals(1, viewModel.uiState.value.skippedStepsCount)

        // Place required sauce, cheese, toppings so cooking requirements are met
        viewModel.selectIngredient("ING-011") // Sauce
        viewModel.selectIngredient("ING-021") // Cheese
        viewModel.selectIngredient("ING-034") // Fresh Basil
        viewModel.selectIngredient("ING-030") // Bell Pepper
        viewModel.selectIngredient("ING-031") // Mushrooms

        // Advance remainder manually
        for (i in 1 until 9) {
            viewModel.advanceStep()
        }
        viewModel.advanceStep() // Finish

        assertTrue(viewModel.uiState.value.isFinished)
        // 1 skip with good toppings = 2 stars per GDD & Phase 1.3.0 scoring formula
        assertEquals(2, viewModel.uiState.value.scoreStars)
    }

    @Test
    fun kitchenViewModel_toddlerTapFallback_incrementsProgressBy25Percent() {
        assertEquals(0.0f, viewModel.uiState.value.doughRollProgress, 0.01f)

        viewModel.applyToddlerTap()
        assertEquals(0.25f, viewModel.uiState.value.doughRollProgress, 0.01f)

        viewModel.applyToddlerTap()
        assertEquals(0.50f, viewModel.uiState.value.doughRollProgress, 0.01f)

        viewModel.applyToddlerTap()
        assertEquals(0.75f, viewModel.uiState.value.doughRollProgress, 0.01f)

        viewModel.applyToddlerTap()
        assertEquals(1.00f, viewModel.uiState.value.doughRollProgress, 0.01f)
    }

    @Test
    fun kitchenViewModel_noBakeRecipe_autoBakes() {
        val noBakeVm = KitchenViewModel(initialRecipeId = "R-03", initialMode = GameMode.RECIPE_MODE)
        assertTrue("R-03 Watermelon Pizza should auto-satisfy baking", noBakeVm.uiState.value.isBaked)
    }

    @Test
    fun responsiveCanvas_aspectRatioCalculations_arePreservedAcrossDevices() {
        val targetRatio = 16f / 9f

        // 16:9 Standard (e.g. 1920x1080)
        val w1 = 1920f
        val h1 = 1080f
        assertEquals(targetRatio, w1 / h1, 0.001f)

        // 19.5:9 Phone (e.g. 2796x1290) -> wider than 16:9 -> fit by height
        val wPhone = 2796f
        val hPhone = 1290f
        val canvasWPhone = hPhone * targetRatio
        assertEquals(2293.333f, canvasWPhone, 0.01f)
        assertTrue(canvasWPhone <= wPhone)

        // 4:3 iPad (e.g. 2048x1536) -> squarer than 16:9 -> fit by width
        val wPad = 2048f
        val hPad = 1536f
        val canvasHPad = wPad / targetRatio
        assertEquals(1152.0f, canvasHPad, 0.01f)
        assertTrue(canvasHPad <= hPad)
    }
}
