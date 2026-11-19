package com.antigravity.pizzacooking.domain.creative

import com.antigravity.pizzacooking.data.models.Ingredient
import com.antigravity.pizzacooking.data.repository.IngredientRepository
import com.antigravity.pizzacooking.domain.statemachine.PlacedTopping

class CreativeCookingSession {
    var selectedCrust: Ingredient? = null
        private set

    var selectedSauce: Ingredient? = null
        private set

    var selectedCheese: Ingredient? = null
        private set

    private val _placedToppings = mutableListOf<PlacedTopping>()
    val placedToppings: List<PlacedTopping> get() = _placedToppings

    var isBaked: Boolean = false
        private set

    var slicesCount: Int = 0
        private set

    var bitesCount: Int = 0
        private set

    var isFinished: Boolean = false
        private set

    fun selectCrust(ingredientId: String): Boolean {
        val ingredient = IngredientRepository.getIngredientById(ingredientId) ?: return false
        if (ingredient.category != "CRUST") return false
        selectedCrust = ingredient
        return true
    }

    fun selectSauce(ingredientId: String): Boolean {
        val ingredient = IngredientRepository.getIngredientById(ingredientId) ?: return false
        if (ingredient.category != "SAUCE") return false
        selectedSauce = ingredient
        return true
    }

    fun selectCheese(ingredientId: String?): Boolean {
        if (ingredientId == null) {
            selectedCheese = null
            return true
        }
        val ingredient = IngredientRepository.getIngredientById(ingredientId) ?: return false
        if (ingredient.category != "CHEESE") return false
        selectedCheese = ingredient
        return true
    }

    fun addTopping(ingredientId: String, x: Float, y: Float): Boolean {
        val ingredient = IngredientRepository.getIngredientById(ingredientId) ?: return false
        if (!ingredient.category.startsWith("TOPPING")) return false
        _placedToppings.add(PlacedTopping(ingredientId, x, y))
        return true
    }

    fun removeLastTopping(): Boolean {
        if (_placedToppings.isNotEmpty()) {
            _placedToppings.removeAt(_placedToppings.size - 1)
            return true
        }
        return false
    }

    fun clearToppings() {
        _placedToppings.clear()
    }

    fun bakePizza() {
        isBaked = true
    }

    fun slicePizza() {
        slicesCount++
    }

    fun bitePizza() {
        bitesCount++
    }

    fun finishCreativePizza(): Boolean {
        // In Creative Mode, kids have total freedom. Having at least a crust allows completion.
        if (selectedCrust == null) {
            // Default fallback to classic round crust if child skipped crust selection
            selectedCrust = IngredientRepository.getIngredientById("ING-001")
        }
        isFinished = true
        return true
    }
}
