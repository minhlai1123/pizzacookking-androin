package com.antigravity.pizzacooking.domain.statemachine

import com.antigravity.pizzacooking.data.models.CookingStep
import com.antigravity.pizzacooking.data.models.Recipe
import com.antigravity.pizzacooking.data.models.StepType

data class PlacedTopping(
    val ingredientId: String,
    val positionX: Float,
    val positionY: Float,
    val timestampMs: Long = System.currentTimeMillis()
)

class CookingSession(
    val recipe: Recipe,
    val steps: List<CookingStep> = defaultStepsForRecipe(recipe)
) {
    var currentStepIndex: Int = 0
        private set

    var cookingState: CookingState = CookingState.NOT_STARTED
        private set

    var sauceCoverage: Float = 0.0f
        private set

    private val _placedToppings = mutableListOf<PlacedTopping>()
    val placedToppings: List<PlacedTopping> get() = _placedToppings

    var isBaked: Boolean = false
        private set

    var slicesCount: Int = 0
        private set

    var bitesCount: Int = 0
        private set

    var manualStepsCompleted: Int = 0
        private set

    var skippedStepsCount: Int = 0
        private set

    var toddlerTapCount: Int = 0
        private set

    var currentStepProgress: Float = 0.0f
        private set

    init {
        // If recipe is no-bake, isBaked is considered auto-satisfied
        if (recipe.isNoBake) {
            isBaked = true
        }
    }

    val currentStep: CookingStep?
        get() = if (currentStepIndex in steps.indices) steps[currentStepIndex] else null

    val totalSteps: Int
        get() = steps.size

    val isFinished: Boolean
        get() = cookingState == CookingState.COMPLETED || currentStepIndex >= steps.size

    fun startSession() {
        if (cookingState == CookingState.NOT_STARTED) {
            currentStepIndex = 0
            cookingState = CookingState.IN_PROGRESS
            currentStepProgress = 0.0f
        }
    }

    fun updateProgress(progress: Float) {
        currentStepProgress = progress.coerceIn(0.0f, 1.0f)
        if (currentStepProgress >= 1.0f) {
            completeCurrentStep(manual = true)
        }
    }

    fun applyToddlerTap() {
        toddlerTapCount++
        // Each toddler tap adds 25% progress on hold/repetitive tasks
        val newProgress = (currentStepProgress + 0.25f).coerceAtMost(1.0f)
        updateProgress(newProgress)
    }

    fun recordSauceCoverage(coverage: Float) {
        sauceCoverage = coverage.coerceIn(0.0f, 1.0f)
    }

    fun addTopping(ingredientId: String, x: Float, y: Float) {
        _placedToppings.add(PlacedTopping(ingredientId, x, y))
    }

    fun completeBaking() {
        isBaked = true
    }

    fun recordSlice() {
        slicesCount++
    }

    fun recordBite() {
        bitesCount++
    }

    fun completeCurrentStep(manual: Boolean) {
        if (manual) {
            manualStepsCompleted++
        } else {
            skippedStepsCount++
        }
        cookingState = CookingState.STEP_COMPLETED
    }

    fun skipCurrentStep(): Boolean {
        val step = currentStep ?: return false
        if (!step.canSkip) return false

        completeCurrentStep(manual = false)
        return moveToNextStep()
    }

    fun moveToNextStep(): Boolean {
        if (currentStepIndex + 1 < steps.size) {
            currentStepIndex++
            currentStepProgress = 0.0f
            cookingState = CookingState.IN_PROGRESS
            return true
        } else {
            cookingState = CookingState.COMPLETED
            return false
        }
    }

    companion object {
        fun defaultStepsForRecipe(recipe: Recipe): List<CookingStep> {
            val list = mutableListOf<CookingStep>()
            var order = 1

            // 1. Dough preparation / Crust rolling
            list.add(
                CookingStep(
                    stepId = "STEP-001",
                    order = order++,
                    stepType = StepType.DOUGH_PREPARATION,
                    title = "Prepare Dough",
                    description = "Knead and roll out the delicious dough.",
                    requiredIngredients = listOf(recipe.crustId),
                    requiredTools = listOf("TOOL-001"),
                    expectedInteraction = "SWIPE"
                )
            )

            // 2. Sauce spreading
            list.add(
                CookingStep(
                    stepId = "STEP-006",
                    order = order++,
                    stepType = StepType.SAUCE_SPREADING,
                    title = "Spread Sauce",
                    description = "Ladle sauce in smooth spirals across the crust.",
                    requiredIngredients = listOf(recipe.sauceId),
                    requiredTools = listOf("TOOL-006"),
                    expectedInteraction = "SWIPE"
                )
            )

            // 3. Cheese grating (if recipe has cheese)
            if (recipe.cheeseId != null) {
                list.add(
                    CookingStep(
                        stepId = "STEP-007",
                        order = order++,
                        stepType = StepType.CHEESE_GRATING,
                        title = "Grate Cheese",
                        description = "Grate flavorful cheese all over the sauce.",
                        requiredIngredients = listOf(recipe.cheeseId),
                        requiredTools = listOf("TOOL-005"),
                        expectedInteraction = "SWIPE"
                    )
                )
            }

            // 4. Topping decoration
            list.add(
                CookingStep(
                    stepId = "STEP-008",
                    order = order++,
                    stepType = StepType.TOPPING_DECORATION,
                    title = "Place Toppings",
                    description = "Decorate your pizza with vibrant toppings.",
                    requiredIngredients = recipe.toppingIds,
                    requiredTools = emptyList(),
                    expectedInteraction = "DRAG_AND_DROP"
                )
            )

            // 5. Oven baking (only if not no-bake)
            if (!recipe.isNoBake) {
                list.add(
                    CookingStep(
                        stepId = "STEP-009",
                        order = order++,
                        stepType = StepType.OVEN_BAKING,
                        title = "Bake in Oven",
                        description = "Slide into the hot oven and watch it bake golden.",
                        requiredIngredients = emptyList(),
                        requiredTools = listOf("TOOL-007"),
                        expectedInteraction = "SWIPE"
                    )
                )
            }

            // 6. Slicing & Eating
            list.add(
                CookingStep(
                    stepId = "STEP-010",
                    order = order++,
                    stepType = StepType.SLICING_AND_EATING,
                    title = "Slice & Enjoy",
                    description = "Slice into delicious pieces and enjoy every bite!",
                    requiredIngredients = emptyList(),
                    requiredTools = listOf("TOOL-008"),
                    expectedInteraction = "SWIPE"
                )
            )

            return list
        }
    }
}
