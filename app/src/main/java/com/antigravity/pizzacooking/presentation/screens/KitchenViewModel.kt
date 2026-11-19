package com.antigravity.pizzacooking.presentation.screens

import com.antigravity.pizzacooking.data.models.Ingredient
import com.antigravity.pizzacooking.data.models.Recipe
import com.antigravity.pizzacooking.data.models.Tool
import com.antigravity.pizzacooking.data.repository.IngredientRepository
import com.antigravity.pizzacooking.data.repository.RecipeRepository
import com.antigravity.pizzacooking.data.repository.ToolRepository
import com.antigravity.pizzacooking.domain.statemachine.GameMode
import com.antigravity.pizzacooking.domain.statemachine.GameStateMachine
import com.antigravity.pizzacooking.domain.statemachine.PlacedTopping
import com.antigravity.pizzacooking.presentation.theme.SoundEffects
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class KitchenViewModel(
    initialRecipeId: String = "R-01",
    initialMode: GameMode = GameMode.RECIPE_MODE
) {
    private val stateMachine = GameStateMachine()

    private val _uiState = MutableStateFlow(KitchenUiState())
    val uiState: StateFlow<KitchenUiState> = _uiState.asStateFlow()

    private val stepDefinitions = listOf(
        StepInfo("STEP-001", "Dough Kneading", "Tap & punch the dough ball!", "TOOL-001"),
        StepInfo("STEP-002", "Dough Rolling", "Swipe rolling pin to flatten dough!", "TOOL-001"),
        StepInfo("STEP-003", "Dough Cutting", "Stamp crust into shape!", "TOOL-012"),
        StepInfo("STEP-004", "Sauce Blending", "Blend fresh pizza sauce!", "TOOL-003"),
        StepInfo("STEP-005", "Crust Shaping", "Pinch delicious crust edges!", "TOOL-001"),
        StepInfo("STEP-006", "Sauce Spreading", "Swirl rich sauce across the pizza!", "TOOL-006"),
        StepInfo("STEP-007", "Cheese Grating", "Swipe to grate fresh cheese!", "TOOL-005"),
        StepInfo("STEP-008", "Topping Decorating", "Pick yummy toppings & put on pizza!", null),
        StepInfo("STEP-009", "Oven Baking", "Slide pizza into the oven to bake!", "TOOL-007"),
        StepInfo("STEP-010", "Slicing & Eating", "Slice your pizza and tap to eat!", "TOOL-008")
    )

    init {
        val recipe = RecipeRepository.getRecipeById(initialRecipeId) ?: RecipeRepository.getAllRecipes().first()
        initSession(recipe, initialMode)
    }

    fun initSession(recipe: Recipe, mode: GameMode) {
        if (mode == GameMode.RECIPE_MODE) {
            stateMachine.startRecipe(recipe)
        } else {
            stateMachine.startCreativeMode()
        }

        val step = stepDefinitions[0]
        val availableIngs = getIngredientsForStep(mode, step.id, recipe, "VEGETABLE")
        val activeTool = step.toolId?.let { ToolRepository.getToolById(it) }

        val tier = when {
            recipe.difficulty <= 2 -> 1
            recipe.difficulty == 3 -> 2
            else -> 3
        }

        _uiState.value = KitchenUiState(
            gameMode = mode,
            currentRecipe = recipe,
            difficultyTier = tier,
            currentStepIndex = 0,
            totalSteps = 10,
            currentStepId = step.id,
            currentStepTitle = step.title,
            currentStepPrompt = step.prompt,
            selectedIngredientId = null,
            selectedToolId = step.toolId,
            activeCreativeCategory = "VEGETABLE",
            availableIngredients = availableIngs,
            activeTool = activeTool,
            placedToppings = emptyList(),
            sauceCoverage = 0.0f,
            cheeseGrated = 0.0f,
            doughRollProgress = 0.0f,
            isBaked = recipe.isNoBake,
            ovenHeatProgress = if (recipe.isNoBake) 1.0f else 0.0f,
            slicesCount = 0,
            bitesCount = 0,
            isAudioMuted = SoundEffects.isMuted,
            skippedStepsCount = 0,
            isFinished = false,
            scoreStars = null,
            feedbackMessage = "Ready to cook: ${recipe.name}!"
        )
    }

    fun startRecipeCooking(recipe: Recipe) {
        initSession(recipe, GameMode.RECIPE_MODE)
    }

    fun switchMode(mode: GameMode) {
        val currentRecipe = _uiState.value.currentRecipe ?: RecipeRepository.getAllRecipes().first()
        initSession(currentRecipe, mode)
        SoundEffects.playPop()
    }

    fun selectCreativeCategory(category: String) {
        _uiState.update { current ->
            val ingredients = getCreativeIngredientsForCategory(category)
            current.copy(
                activeCreativeCategory = category,
                availableIngredients = ingredients,
                feedbackMessage = "Browsing category: $category"
            )
        }
        SoundEffects.playPop()
    }

    fun selectIngredient(ingredientId: String) {
        val ingredient = IngredientRepository.getIngredientById(ingredientId) ?: return

        _uiState.update { current ->
            var newSauce = current.sauceCoverage
            var newCheese = current.cheeseGrated
            val newToppings = current.placedToppings.toMutableList()

            when {
                ingredient.category == "SAUCE" -> {
                    newSauce = 1.0f
                    stateMachine.activeCookingSession?.recordSauceCoverage(1.0f)
                    stateMachine.activeCreativeSession?.selectSauce(ingredientId)
                    SoundEffects.playSplat()
                }
                ingredient.category == "CHEESE" -> {
                    newCheese = 1.0f
                    stateMachine.activeCreativeSession?.selectCheese(ingredientId)
                    SoundEffects.playGrate()
                }
                ingredient.category.startsWith("TOPPING") -> {
                    // Place topping at randomized kid-friendly radius on pizza
                    val angle = Random.nextDouble(0.0, 2.0 * Math.PI)
                    val r = Random.nextDouble(0.1, 0.75) // relative radius 0.0 to 1.0
                    val posX = (0.5 + r * Math.cos(angle)).toFloat()
                    val posY = (0.5 + r * Math.sin(angle)).toFloat()

                    if (newToppings.size < 30) {
                        val placed = PlacedTopping(ingredientId, posX, posY)
                        newToppings.add(placed)
                        stateMachine.activeCookingSession?.addTopping(ingredientId, posX, posY)
                        stateMachine.activeCreativeSession?.addTopping(ingredientId, posX, posY)
                        SoundEffects.playPop()
                    }
                }
            }

            current.copy(
                selectedIngredientId = ingredientId,
                sauceCoverage = newSauce,
                cheeseGrated = newCheese,
                placedToppings = newToppings,
                feedbackMessage = "Added: ${ingredient.name}"
            )
        }
    }

    fun addToppingAtPosition(ingredientId: String, normX: Float, normY: Float) {
        val ingredient = IngredientRepository.getIngredientById(ingredientId) ?: return
        _uiState.update { current ->
            val newToppings = current.placedToppings.toMutableList()
            if (newToppings.size < 30) {
                newToppings.add(PlacedTopping(ingredientId, normX, normY))
                stateMachine.activeCookingSession?.addTopping(ingredientId, normX, normY)
                stateMachine.activeCreativeSession?.addTopping(ingredientId, normX, normY)
                SoundEffects.playPop()
            }
            current.copy(
                selectedIngredientId = ingredientId,
                placedToppings = newToppings,
                feedbackMessage = "Placed: ${ingredient.name}"
            )
        }
    }

    fun clearToppings() {
        _uiState.update { current ->
            current.copy(placedToppings = emptyList(), feedbackMessage = "Cleared toppings!")
        }
        SoundEffects.playWhoosh()
    }

    fun selectTool(toolId: String) {
        val tool = ToolRepository.getToolById(toolId) ?: return
        _uiState.update { current ->
            current.copy(
                selectedToolId = toolId,
                activeTool = tool,
                feedbackMessage = "Using tool: ${tool.name}"
            )
        }
        SoundEffects.playPop()
    }

    fun advanceStep() {
        val nextIndex = _uiState.value.currentStepIndex + 1
        if (nextIndex < stepDefinitions.size) {
            jumpToStep(nextIndex)
        } else {
            finishSession()
        }
    }

    fun jumpToStep(targetIndex: Int) {
        if (targetIndex in stepDefinitions.indices) {
            val step = stepDefinitions[targetIndex]
            val recipe = _uiState.value.currentRecipe ?: RecipeRepository.getAllRecipes().first()
            val available = getIngredientsForStep(_uiState.value.gameMode, step.id, recipe, _uiState.value.activeCreativeCategory)
            val tool = step.toolId?.let { ToolRepository.getToolById(it) }

            stateMachine.advanceCookingStep()

            _uiState.update { current ->
                var rollProg = current.doughRollProgress
                var sauce = current.sauceCoverage
                var cheese = current.cheeseGrated
                var baked = current.isBaked

                if (targetIndex >= 1) rollProg = 1.0f
                if (targetIndex >= 6 && sauce == 0.0f) sauce = 0.85f
                if (targetIndex >= 7 && cheese == 0.0f) cheese = 0.90f
                if (targetIndex >= 8) baked = true

                current.copy(
                    currentStepIndex = targetIndex,
                    currentStepId = step.id,
                    currentStepTitle = step.title,
                    currentStepPrompt = step.prompt,
                    selectedIngredientId = null,
                    selectedToolId = step.toolId,
                    availableIngredients = available,
                    activeTool = tool,
                    doughRollProgress = rollProg,
                    sauceCoverage = sauce,
                    cheeseGrated = cheese,
                    isBaked = baked,
                    feedbackMessage = step.title
                )
            }
            SoundEffects.playWhoosh()
        }
    }

    fun skipStep() {
        stateMachine.skipCurrentStep()
        val nextIndex = _uiState.value.currentStepIndex + 1

        if (nextIndex < stepDefinitions.size) {
            val step = stepDefinitions[nextIndex]
            val recipe = _uiState.value.currentRecipe ?: RecipeRepository.getAllRecipes().first()
            val available = getIngredientsForStep(_uiState.value.gameMode, step.id, recipe, _uiState.value.activeCreativeCategory)
            val tool = step.toolId?.let { ToolRepository.getToolById(it) }

            _uiState.update { current ->
                val newSkip = current.skippedStepsCount + 1
                current.copy(
                    currentStepIndex = nextIndex,
                    currentStepId = step.id,
                    currentStepTitle = step.title,
                    currentStepPrompt = step.prompt,
                    selectedIngredientId = null,
                    selectedToolId = step.toolId,
                    availableIngredients = available,
                    activeTool = tool,
                    skippedStepsCount = newSkip,
                    feedbackMessage = "Skipped to: ${step.title}"
                )
            }
            SoundEffects.playWhoosh()
        } else {
            finishSession()
        }
    }

    fun applyToddlerTap() {
        stateMachine.applyToddlerTap()
        _uiState.update { current ->
            val newProgress = when (current.currentStepIndex) {
                0, 1 -> (current.doughRollProgress + 0.25f).coerceAtMost(1.0f)
                5 -> (current.sauceCoverage + 0.25f).coerceAtMost(1.0f)
                6 -> (current.cheeseGrated + 0.25f).coerceAtMost(1.0f)
                else -> 1.0f
            }
            if (current.currentStepIndex <= 1) SoundEffects.playBoing()
            if (current.currentStepIndex == 5) SoundEffects.playSplat()
            if (current.currentStepIndex == 6) SoundEffects.playGrate()

            current.copy(
                doughRollProgress = if (current.currentStepIndex <= 1) newProgress else current.doughRollProgress,
                sauceCoverage = if (current.currentStepIndex == 5) newProgress else current.sauceCoverage,
                cheeseGrated = if (current.currentStepIndex == 6) newProgress else current.cheeseGrated,
                feedbackMessage = "Quick Tap! +25%"
            )
        }
    }

    fun interactDough() {
        _uiState.update { current ->
            val inc = when (current.difficultyTier) {
                1 -> 0.35f
                2 -> 0.22f
                else -> 0.16f
            }
            val nextProg = (current.doughRollProgress + inc).coerceAtMost(1.0f)
            current.copy(doughRollProgress = nextProg, feedbackMessage = "Kneading dough!")
        }
        SoundEffects.playBoing()
    }

    fun interactRollDough() {
        _uiState.update { current ->
            val inc = when (current.difficultyTier) {
                1 -> 0.40f
                2 -> 0.25f
                else -> 0.18f
            }
            val nextProg = (current.doughRollProgress + inc).coerceAtMost(1.0f)
            current.copy(doughRollProgress = nextProg, feedbackMessage = "Rolling dough flat!")
        }
        SoundEffects.playRoll()
    }

    fun interactSwirlSauce() {
        _uiState.update { current ->
            val inc = when (current.difficultyTier) {
                1 -> 0.35f
                2 -> 0.22f
                else -> 0.16f
            }
            val nextSauce = (current.sauceCoverage + inc).coerceAtMost(1.0f)
            current.copy(sauceCoverage = nextSauce, feedbackMessage = "Swirling tomato sauce!")
        }
        SoundEffects.playSplat()
    }

    fun interactGrateCheese() {
        _uiState.update { current ->
            val inc = when (current.difficultyTier) {
                1 -> 0.40f
                2 -> 0.25f
                else -> 0.18f
            }
            val nextCheese = (current.cheeseGrated + inc).coerceAtMost(1.0f)
            current.copy(cheeseGrated = nextCheese, feedbackMessage = "Grating mozzarella cheese!")
        }
        SoundEffects.playGrate()
    }

    fun bakePizzaInOven() {
        _uiState.update { current ->
            current.copy(
                isBaked = true,
                ovenHeatProgress = 1.0f,
                feedbackMessage = "Pizza baked to golden perfection!"
            )
        }
        SoundEffects.playDing()
    }

    fun setOvenHeatProgress(progress: Float) {
        _uiState.update { it.copy(ovenHeatProgress = progress) }
    }

    fun takeBite() {
        _uiState.update { current ->
            val newBites = current.bitesCount + 1
            stateMachine.activeCookingSession?.recordBite()
            current.copy(
                bitesCount = newBites,
                feedbackMessage = "Yummy crunchy bite! (${newBites}/6)"
            )
        }
        SoundEffects.playCrunch()
        if (_uiState.value.bitesCount >= 4) {
            finishSession()
        }
    }

    fun slicePizza() {
        _uiState.update { current ->
            val newSlices = if (current.slicesCount == 0) 6 else current.slicesCount
            stateMachine.activeCookingSession?.recordSlice()
            current.copy(
                slicesCount = newSlices,
                feedbackMessage = "Pizza cut into 6 slices! Tap to eat!"
            )
        }
        SoundEffects.playCrunch()
    }

    fun finishSession() {
        val score = stateMachine.finishSession()
        val tier = _uiState.value.difficultyTier
        val fallback = calculateStarsFallback(_uiState.value.skippedStepsCount)
        val calculated = score?.stars ?: fallback
        val stars = if (_uiState.value.skippedStepsCount > 0) {
            fallback
        } else {
            when (tier) {
                1 -> 3 // Level 1 (Easy) awards 3 stars on full completion
                2 -> calculated.coerceAtLeast(2)
                else -> calculated
            }
        }

        _uiState.update { current ->
            current.copy(
                isFinished = true,
                scoreStars = stars,
                feedbackMessage = "Hooray! You earned $stars Stars!"
            )
        }
        SoundEffects.playFanfare()
    }

    fun toggleAudio() {
        SoundEffects.isMuted = !SoundEffects.isMuted
        _uiState.update { it.copy(isAudioMuted = SoundEffects.isMuted) }
    }

    private fun calculateStarsFallback(skipped: Int): Int {
        return when {
            skipped == 0 -> 3
            skipped == 1 -> 2
            else -> 1 // Zero 0-star penalty policy for kids
        }
    }

    private fun getIngredientsForStep(mode: GameMode, stepId: String, recipe: Recipe, creativeCategory: String): List<Ingredient> {
        if (mode == GameMode.CREATIVE_MODE) {
            return getCreativeIngredientsForCategory(creativeCategory)
        }

        // Recipe Mode: Contextual ingredients
        return when (stepId) {
            "STEP-001", "STEP-002", "STEP-003", "STEP-005" -> {
                IngredientRepository.getAllIngredients().filter { it.ingredientId == recipe.crustId }
            }
            "STEP-004", "STEP-006" -> {
                IngredientRepository.getAllIngredients().filter { it.ingredientId == recipe.sauceId }
            }
            "STEP-007" -> {
                IngredientRepository.getAllIngredients().filter { it.ingredientId == recipe.cheeseId }
            }
            "STEP-008" -> {
                IngredientRepository.getAllIngredients().filter { it.ingredientId in recipe.toppingIds }
            }
            else -> emptyList()
        }
    }

    private fun getCreativeIngredientsForCategory(category: String): List<Ingredient> {
        val catPrefix = when (category) {
            "VEGETABLE" -> "TOPPING_VEGGIE"
            "MEAT" -> "TOPPING_MEAT"
            "FRUIT" -> "TOPPING_FRUIT"
            "SWEET" -> "TOPPING_SWEET"
            "FANTASY" -> "TOPPING_FANTASY"
            else -> "TOPPING_VEGGIE"
        }
        return IngredientRepository.getAllIngredients().filter { it.category == catPrefix }
    }

    private data class StepInfo(
        val id: String,
        val title: String,
        val prompt: String,
        val toolId: String?
    )
}
