package com.antigravity.pizzacooking

import com.antigravity.pizzacooking.domain.creative.CreativeCookingSession
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CreativeModeTest {

    @Test
    fun creativeSession_canSelectAnyIngredientFreely() {
        val session = CreativeCookingSession()

        // Select Crust (ING-002: Gluten-Free Cauliflower)
        assertTrue(session.selectCrust("ING-002"))
        assertNotNull(session.selectedCrust)
        assertEquals("ING-002", session.selectedCrust!!.ingredientId)

        // Invalid crust rejection (cannot pass sauce as crust)
        assertFalse(session.selectCrust("ING-011"))

        // Select Sauce (ING-013: Sweet Chocolate Ganache)
        assertTrue(session.selectSauce("ING-013"))
        assertNotNull(session.selectedSauce)
        assertEquals("ING-013", session.selectedSauce!!.ingredientId)

        // Select Cheese (optional)
        assertTrue(session.selectCheese("ING-021"))
        assertNotNull(session.selectedCheese)

        // Add multiple toppings
        assertTrue(session.addTopping("ING-030", 0.2f, 0.3f))
        assertTrue(session.addTopping("ING-050", 0.5f, 0.5f))
        assertEquals(2, session.placedToppings.size)

        // Remove topping
        assertTrue(session.removeLastTopping())
        assertEquals(1, session.placedToppings.size)

        // Baking and slicing
        assertFalse(session.isBaked)
        session.bakePizza()
        assertTrue(session.isBaked)

        session.slicePizza()
        assertEquals(1, session.slicesCount)

        session.bitePizza()
        assertEquals(1, session.bitesCount)

        assertTrue(session.finishCreativePizza())
        assertTrue(session.isFinished)
    }

    @Test
    fun creativeSession_canUseAllNewExpandedToppingsFromAllFiveCategories() {
        val session = CreativeCookingSession()
        session.selectCrust("ING-001")
        session.selectSauce("ING-011")

        // Add one from each new category
        assertTrue(session.addTopping("ING-075", 0.1f, 0.1f)) // Veggie: Roasted Garlic
        assertTrue(session.addTopping("ING-085", 0.2f, 0.2f)) // Meat/Seafood: Octopus Tentacles
        assertTrue(session.addTopping("ING-098", 0.3f, 0.3f)) // Fruit: Mango Sea Turtles
        assertTrue(session.addTopping("ING-105", 0.4f, 0.4f)) // Sweet: Gummy Worms
        assertTrue(session.addTopping("ING-115", 0.5f, 0.5f)) // Fantasy: Beach Floaties
        assertTrue(session.addTopping("ING-122", 0.6f, 0.6f)) // Fantasy: Starlight Sugar Pearls

        assertEquals(6, session.placedToppings.size)
        assertTrue(session.finishCreativePizza())
        assertTrue(session.isFinished)
    }

    @Test
    fun creativeSession_defaultsToClassicCrustIfChildDidNotSelect() {
        val session = CreativeCookingSession()
        // No crust selected
        assertTrue(session.finishCreativePizza())
        assertNotNull("Should fallback gracefully to classic crust", session.selectedCrust)
        assertEquals("ING-001", session.selectedCrust!!.ingredientId)
    }
}
