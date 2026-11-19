package com.antigravity.pizzacooking

import com.antigravity.pizzacooking.data.repository.IngredientRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class IngredientRepositoryTest {

    @Test
    fun repository_containsOneHundredTwelveIngredients() {
        val allIngredients = IngredientRepository.getAllIngredients()
        assertEquals("Master ingredient catalog must contain exactly 112 ingredients", 112, allIngredients.size)
    }

    @Test
    fun repository_categoriesCountMatchesSpecification() {
        val crusts = IngredientRepository.getCrusts()
        val sauces = IngredientRepository.getSauces()
        val cheeses = IngredientRepository.getCheeses()
        val toppings = IngredientRepository.getToppings()

        assertEquals("Must contain 10 crusts", 10, crusts.size)
        assertEquals("Must contain 10 sauces", 10, sauces.size)
        assertEquals("Must contain 6 cheeses", 6, cheeses.size)
        assertEquals("Must contain 86 toppings (>= 84 required by GDD)", 86, toppings.size)
    }

    @Test
    fun repository_allIngredientIdsAndNamesAreUnique() {
        val all = IngredientRepository.getAllIngredients()
        val ids = all.map { it.ingredientId }
        val names = all.map { it.name }
        val visualIds = all.map { it.visualIdentifier }

        assertEquals("All 112 ingredient IDs must be unique", 112, ids.toSet().size)
        assertEquals("All 112 ingredient names must be unique", 112, names.toSet().size)
        assertEquals("All 112 visual identifiers must be unique", 112, visualIds.toSet().size)
    }

    @Test
    fun repository_ingredientLookupReturnsValidData() {
        val classicCrust = IngredientRepository.getIngredientById("ING-001")
        assertNotNull(classicCrust)
        assertEquals("Classic Round Dough", classicCrust!!.name)
        assertEquals("CRUST", classicCrust.category)
        assertTrue(classicCrust.applicableRecipes.contains("R-01"))

        val tomatoSauce = IngredientRepository.getIngredientById("ING-011")
        assertNotNull(tomatoSauce)
        assertEquals("San Marzano Tomato Sauce", tomatoSauce!!.name)
        assertEquals("SAUCE", tomatoSauce.category)

        val mozzarella = IngredientRepository.getIngredientById("ING-021")
        assertNotNull(mozzarella)
        assertEquals("Fresh Mozzarella Slices", mozzarella!!.name)
        assertEquals("CHEESE", mozzarella.category)
    }

    @Test
    fun repository_newCreativeToppingsAccessibleAndProperlyConfigured() {
        // Test new veggie topping (ING-075)
        val roastedGarlic = IngredientRepository.getIngredientById("ING-075")
        assertNotNull("ING-075 must exist", roastedGarlic)
        assertEquals("Golden Roasted Garlic Cloves", roastedGarlic!!.name)
        assertEquals("TOPPING_VEGGIE", roastedGarlic.category)
        assertTrue(roastedGarlic.applicableRecipes.contains("CREATIVE_MODE"))

        // Test new seafood topping (ING-085)
        val octopus = IngredientRepository.getIngredientById("ING-085")
        assertNotNull("ING-085 must exist", octopus)
        assertEquals("Boiled Octopus Tentacles", octopus!!.name)
        assertEquals("TOPPING_MEAT", octopus.category)

        // Test new fruit topping (ING-098)
        val mangoTurtles = IngredientRepository.getIngredientById("ING-098")
        assertNotNull("ING-098 must exist", mangoTurtles)
        assertEquals("Carved Mango Sea Turtles", mangoTurtles!!.name)
        assertEquals("TOPPING_FRUIT", mangoTurtles.category)

        // Test new sweet topping (ING-105)
        val gummyWorms = IngredientRepository.getIngredientById("ING-105")
        assertNotNull("ING-105 must exist", gummyWorms)
        assertEquals("Rainbow Gummy Worms", gummyWorms!!.name)
        assertEquals("TOPPING_SWEET", gummyWorms.category)

        // Test new fantasy topping (ING-115)
        val beachFloaties = IngredientRepository.getIngredientById("ING-115")
        assertNotNull("ING-115 must exist", beachFloaties)
        assertEquals("Gummy Beach Pool Floaties", beachFloaties!!.name)
        assertEquals("TOPPING_FANTASY", beachFloaties.category)

        // Test last expanded topping (ING-122)
        val sugarPearls = IngredientRepository.getIngredientById("ING-122")
        assertNotNull("ING-122 must exist", sugarPearls)
        assertEquals("Starlight Glowing Sugar Pearls", sugarPearls!!.name)
        assertEquals("TOPPING_FANTASY", sugarPearls.category)
    }

    @Test
    fun repository_fiveToppingSubcategoriesBreakdown() {
        val veggies = IngredientRepository.getIngredientsByCategory("TOPPING_VEGGIE")
        val meats = IngredientRepository.getIngredientsByCategory("TOPPING_MEAT")
        val fruits = IngredientRepository.getIngredientsByCategory("TOPPING_FRUIT")
        val sweets = IngredientRepository.getIngredientsByCategory("TOPPING_SWEET")
        val fantasy = IngredientRepository.getIngredientsByCategory("TOPPING_FANTASY")

        assertEquals("Must contain 20 veggie toppings", 20, veggies.size)
        assertEquals("Must contain 19 meat/seafood toppings", 19, meats.size)
        assertEquals("Must contain 14 fresh fruit toppings", 14, fruits.size)
        assertEquals("Must contain 25 sweet/candy toppings", 25, sweets.size)
        assertEquals("Must contain 8 fantasy/novelty toppings", 8, fantasy.size)

        val totalToppings = veggies.size + meats.size + fruits.size + sweets.size + fantasy.size
        assertEquals(86, totalToppings)
    }

    @Test
    fun repository_ingredientsForRecipeR01() {
        val ingredients = IngredientRepository.getIngredientsForRecipe("R-01")
        assertTrue("R-01 must have ingredients associated", ingredients.isNotEmpty())
        val ids = ingredients.map { it.ingredientId }
        assertTrue(ids.contains("ING-001"))
        assertTrue(ids.contains("ING-011"))
        assertTrue(ids.contains("ING-021"))
        assertTrue(ids.contains("ING-030"))
    }
}
