package com.antigravity.pizzacooking.data.repository

import com.antigravity.pizzacooking.data.models.CompletionRules
import com.antigravity.pizzacooking.data.models.PresentationMetadata
import com.antigravity.pizzacooking.data.models.Recipe

object RecipeRepository {
    private val masterRecipes: Map<String, Recipe> = listOf(
        Recipe(
            recipeId = "R-01",
            name = "Classic Margherita Pizza",
            description = "Authentic Neapolitan pizza with San Marzano tomato sauce, fresh mozzarella rounds, and aromatic basil leaves.",
            category = "Classic & Gourmet",
            difficulty = 1,
            isNoBake = false,
            crustId = "ING-001",
            sauceId = "ING-011",
            cheeseId = "ING-021",
            toppingIds = listOf("ING-030"),
            toolIds = listOf("TOOL-001", "TOOL-006", "TOOL-007", "TOOL-008"),
            specialMechanics = "SPIRAL_SPREAD",
            completionRules = CompletionRules(
                minSauceCoverage = 0.75f,
                minToppings = 3,
                requiresOven = true
            ),
            presentationMetadata = PresentationMetadata(
                finishedDishLook = "Golden crust with bubbly cheese and fresh green basil",
                backgroundTheme = "BG_ITALIAN_KITCHEN"
            )
        ),
        Recipe(
            recipeId = "R-02",
            name = "Pepperoni Supreme Pizza",
            description = "Classic American favorite loaded with sliced pepperoni, green bell peppers, and savory black olives.",
            category = "Classic & Gourmet",
            difficulty = 2,
            isNoBake = false,
            crustId = "ING-001",
            sauceId = "ING-011",
            cheeseId = "ING-021",
            toppingIds = listOf("ING-040", "ING-031", "ING-032"),
            toolIds = listOf("TOOL-001", "TOOL-002", "TOOL-006", "TOOL-007", "TOOL-008"),
            specialMechanics = "PEPPERONI_SLICE_CHEESE_PULL",
            completionRules = CompletionRules(
                minSauceCoverage = 0.75f,
                minToppings = 5,
                requiresOven = true
            ),
            presentationMetadata = PresentationMetadata(
                finishedDishLook = "Crispy curled pepperoni cups with melted stringy cheese",
                backgroundTheme = "BG_ITALIAN_KITCHEN"
            )
        ),
        Recipe(
            recipeId = "R-03",
            name = "Summer Watermelon Pizza",
            description = "Refreshing chilled summer treat with thick watermelon slab crust, blended rainbow coconut glaze, and cute mango sea turtles.",
            category = "Sweet & Dessert",
            difficulty = 2,
            isNoBake = true,
            crustId = "ING-002",
            sauceId = "ING-013",
            cheeseId = null,
            toppingIds = listOf("ING-050", "ING-063"),
            toolIds = listOf("TOOL-002", "TOOL-003", "TOOL-004", "TOOL-006", "TOOL-008"),
            specialMechanics = "WATERMELON_CUT_BLENDER_NO_BAKE",
            completionRules = CompletionRules(
                minSauceCoverage = 0.7f,
                minToppings = 4,
                requiresOven = false
            ),
            presentationMetadata = PresentationMetadata(
                finishedDishLook = "Crisp red watermelon slice with pastel rainbow glaze and candy floaties",
                backgroundTheme = "BG_TROPICAL_BEACH"
            )
        ),
        Recipe(
            recipeId = "R-04",
            name = "Sweet Candy & Marshmallow Pizza",
            description = "Dream dessert pizza with dark chocolate cookie crust, hazelnut chocolate spread, gummy bears, and toasted marshmallows.",
            category = "Sweet & Dessert",
            difficulty = 2,
            isNoBake = false,
            crustId = "ING-003",
            sauceId = "ING-014",
            cheeseId = null,
            toppingIds = listOf("ING-064", "ING-065", "ING-053"),
            toolIds = listOf("TOOL-001", "TOOL-006", "TOOL-007", "TOOL-008"),
            specialMechanics = "MARSHMALLOW_MELT",
            completionRules = CompletionRules(
                minSauceCoverage = 0.75f,
                minToppings = 5,
                requiresOven = true
            ),
            presentationMetadata = PresentationMetadata(
                finishedDishLook = "Glistening dark chocolate base with gooey marshmallows and gummy bears",
                backgroundTheme = "BG_CANDY_DREAMLAND"
            )
        ),
        Recipe(
            recipeId = "R-05",
            name = "Four Cheese Formaggi Pizza",
            description = "Cheese lover paradise combining mozzarella, cheddar, gorgonzola, and parmesan over creamy alfredo sauce.",
            category = "Classic & Gourmet",
            difficulty = 2,
            isNoBake = false,
            crustId = "ING-001",
            sauceId = "ING-012",
            cheeseId = "ING-021",
            toppingIds = listOf("ING-022", "ING-026"),
            toolIds = listOf("TOOL-001", "TOOL-005", "TOOL-006", "TOOL-007", "TOOL-008"),
            specialMechanics = "FOUR_SIDED_GRATER",
            completionRules = CompletionRules(
                minSauceCoverage = 0.8f,
                minToppings = 4,
                requiresOven = true
            ),
            presentationMetadata = PresentationMetadata(
                finishedDishLook = "Rich bubbling mosaic of four golden cheeses",
                backgroundTheme = "BG_ITALIAN_KITCHEN"
            )
        ),
        Recipe(
            recipeId = "R-06",
            name = "Tropical Hawaiian Breeze Pizza",
            description = "Sunny island creation balancing sweet diced pineapple chunks and smoky ham over tomato herb sauce.",
            category = "Special Occasions & Breakfast",
            difficulty = 2,
            isNoBake = false,
            crustId = "ING-001",
            sauceId = "ING-011",
            cheeseId = "ING-021",
            toppingIds = listOf("ING-051", "ING-041"),
            toolIds = listOf("TOOL-001", "TOOL-002", "TOOL-006", "TOOL-007", "TOOL-008"),
            specialMechanics = "PINEAPPLE_BURST",
            completionRules = CompletionRules(
                minSauceCoverage = 0.75f,
                minToppings = 5,
                requiresOven = true
            ),
            presentationMetadata = PresentationMetadata(
                finishedDishLook = "Golden pineapple tidbits caramelized alongside tender pink ham",
                backgroundTheme = "BG_TROPICAL_BEACH"
            )
        ),
        Recipe(
            recipeId = "R-07",
            name = "Ahoy Pirate Treasure Pizza",
            description = "Swashbuckling adventure with sea-blue crust, golden cheddar sauce, octopus-cut frankfurters, and gold doubloons.",
            category = "Fantasy & Fun",
            difficulty = 3,
            isNoBake = false,
            crustId = "ING-001",
            sauceId = "ING-011",
            cheeseId = "ING-022",
            toppingIds = listOf("ING-042", "ING-066", "ING-067"),
            toolIds = listOf("TOOL-001", "TOOL-002", "TOOL-006", "TOOL-007", "TOOL-008"),
            specialMechanics = "OCTOPUS_NOTCH",
            completionRules = CompletionRules(
                minSauceCoverage = 0.75f,
                minToppings = 5,
                requiresOven = true
            ),
            presentationMetadata = PresentationMetadata(
                finishedDishLook = "Curled sausage octopuses guarding gleaming gold chocolate coins",
                backgroundTheme = "BG_PIRATE_COVE"
            )
        ),
        Recipe(
            recipeId = "R-08",
            name = "Spooky Halloween Vampire Pizza",
            description = "Eerie midnight delight with pitch black charcoal crust, orange pumpkin puree, sour cream spiderweb, and olive spiders.",
            category = "Fantasy & Fun",
            difficulty = 3,
            isNoBake = false,
            crustId = "ING-005",
            sauceId = "ING-015",
            cheeseId = "ING-021",
            toppingIds = listOf("ING-060", "ING-032", "ING-067"),
            toolIds = listOf("TOOL-001", "TOOL-002", "TOOL-006", "TOOL-007", "TOOL-009"),
            specialMechanics = "SPIDERWEB_PIPING",
            completionRules = CompletionRules(
                minSauceCoverage = 0.75f,
                minToppings = 4,
                requiresOven = true
            ),
            presentationMetadata = PresentationMetadata(
                finishedDishLook = "Striking orange pumpkin sauce contrasting sharp black crust and intricate white spiderweb",
                backgroundTheme = "BG_SPOOKY_CASTLE"
            )
        ),
        Recipe(
            recipeId = "R-09",
            name = "Magical Rainbow Unicorn Pizza",
            description = "Fairytale masterpiece with 5-color braided pastel crust, pink cotton candy sauce, golden horn, and edible glitter dust.",
            category = "Fantasy & Fun",
            difficulty = 3,
            isNoBake = false,
            crustId = "ING-004",
            sauceId = "ING-017",
            cheeseId = "ING-023",
            toppingIds = listOf("ING-068", "ING-069"),
            toolIds = listOf("TOOL-001", "TOOL-006", "TOOL-007", "TOOL-012"),
            specialMechanics = "RAINBOW_BRAID_GOLD_SIEVE",
            completionRules = CompletionRules(
                minSauceCoverage = 0.8f,
                minToppings = 4,
                requiresOven = true
            ),
            presentationMetadata = PresentationMetadata(
                finishedDishLook = "Shimmering rainbow swirl crust with golden horn and meringue wings",
                backgroundTheme = "BG_CANDY_DREAMLAND"
            )
        ),
        Recipe(
            recipeId = "R-10",
            name = "Winter Wonderland Christmas Pizza",
            description = "Holiday celebration featuring 6-point snowflake crust, white alfredo snow sauce, snowy parmesan dusting, and snowman marshmallow.",
            category = "Special Occasions & Breakfast",
            difficulty = 3,
            isNoBake = false,
            crustId = "ING-006",
            sauceId = "ING-012",
            cheeseId = "ING-026",
            toppingIds = listOf("ING-074", "ING-031"),
            toolIds = listOf("TOOL-001", "TOOL-005", "TOOL-006", "TOOL-007", "TOOL-012"),
            specialMechanics = "SNOWFLAKE_CUT_PARMESAN_SHAKE",
            completionRules = CompletionRules(
                minSauceCoverage = 0.75f,
                minToppings = 5,
                requiresOven = true
            ),
            presentationMetadata = PresentationMetadata(
                finishedDishLook = "Crisp white snowflake covered in parmesan powder snow and peppermint candy canes",
                backgroundTheme = "BG_CHRISTMAS_FIREPLACE"
            )
        ),
        Recipe(
            recipeId = "R-11",
            name = "Gourmet BBQ Chicken Feast Pizza",
            description = "Hearty whole-wheat crust layered with tender shredded chicken, red onion rings, and tangy zigzag barbecue glaze.",
            category = "Classic & Gourmet",
            difficulty = 3,
            isNoBake = false,
            crustId = "ING-001",
            sauceId = "ING-019",
            cheeseId = "ING-022",
            toppingIds = listOf("ING-043", "ING-033"),
            toolIds = listOf("TOOL-001", "TOOL-006", "TOOL-007", "TOOL-010"),
            specialMechanics = "BBQ_BOTTLE_ZIGZAG",
            completionRules = CompletionRules(
                minSauceCoverage = 0.75f,
                minToppings = 5,
                requiresOven = true
            ),
            presentationMetadata = PresentationMetadata(
                finishedDishLook = "Rustic brown BBQ drizzle over smoky grilled chicken and melted cheddar",
                backgroundTheme = "BG_ITALIAN_KITCHEN"
            )
        ),
        Recipe(
            recipeId = "R-12",
            name = "Japanese Takoyaki Pizza",
            description = "Exciting fusion with fluffy batter crust, glossy teriyaki glaze, tender octopus bites, and bonito flakes that dance when hot.",
            category = "Classic & Gourmet",
            difficulty = 4,
            isNoBake = false,
            crustId = "ING-001",
            sauceId = "ING-016",
            cheeseId = "ING-021",
            toppingIds = listOf("ING-045", "ING-061", "ING-046"),
            toolIds = listOf("TOOL-001", "TOOL-006", "TOOL-007", "TOOL-010"),
            specialMechanics = "TAKOBRUSH_DANCING_BONITO",
            completionRules = CompletionRules(
                minSauceCoverage = 0.75f,
                minToppings = 4,
                requiresOven = true
            ),
            presentationMetadata = PresentationMetadata(
                finishedDishLook = "Glossy teriyaki glaze with 3-strip kewpie mayo and dancing bonito curls",
                backgroundTheme = "BG_ITALIAN_KITCHEN"
            )
        ),
        Recipe(
            recipeId = "R-13",
            name = "Fiesta Mexican Taco Pizza",
            description = "Crunchy pressed corn tortilla crust with black bean salsa, spiced beef, guacamole scoops, and nacho chip crowns.",
            category = "Classic & Gourmet",
            difficulty = 3,
            isNoBake = false,
            crustId = "ING-001",
            sauceId = "ING-011",
            cheeseId = "ING-022",
            toppingIds = listOf("ING-047", "ING-036"),
            toolIds = listOf("TOOL-001", "TOOL-006", "TOOL-007", "TOOL-015"),
            specialMechanics = "TORTILLA_PRESS_GUACAMOLE",
            completionRules = CompletionRules(
                minSauceCoverage = 0.75f,
                minToppings = 5,
                requiresOven = true
            ),
            presentationMetadata = PresentationMetadata(
                finishedDishLook = "Festive Mexican fiesta pizza crowned with crispy nacho triangles and jalapeno rings",
                backgroundTheme = "BG_ITALIAN_KITCHEN"
            )
        ),
        Recipe(
            recipeId = "R-14",
            name = "Sunny Breakfast Egg & Bacon Pizza",
            description = "Fluffy brioche dough with garlic butter, crispy bacon ribbons, and a freshly cracked farm egg with bouncy yolk in the center.",
            category = "Special Occasions & Breakfast",
            difficulty = 3,
            isNoBake = false,
            crustId = "ING-001",
            sauceId = "ING-012",
            cheeseId = "ING-021",
            toppingIds = listOf("ING-044", "ING-048"),
            toolIds = listOf("TOOL-001", "TOOL-006", "TOOL-007", "TOOL-008"),
            specialMechanics = "EGG_CRACK_BOUNCY_YOLK",
            completionRules = CompletionRules(
                minSauceCoverage = 0.75f,
                minToppings = 4,
                requiresOven = true
            ),
            presentationMetadata = PresentationMetadata(
                finishedDishLook = "Golden sunny egg yolk nested inside crispy smoked bacon curls",
                backgroundTheme = "BG_ITALIAN_KITCHEN"
            )
        ),
        Recipe(
            recipeId = "R-15",
            name = "Wild Forest Mushroom Truffle Pizza",
            description = "Artisan sourdough crust with porcini cream sauce, wild chanterelles, and aromatic black truffle paper-thin shavings.",
            category = "Classic & Gourmet",
            difficulty = 3,
            isNoBake = false,
            crustId = "ING-007",
            sauceId = "ING-012",
            cheeseId = "ING-026",
            toppingIds = listOf("ING-034", "ING-035"),
            toolIds = listOf("TOOL-001", "TOOL-006", "TOOL-007", "TOOL-014"),
            specialMechanics = "WOODEN_TRUFFLE_SHAVE",
            completionRules = CompletionRules(
                minSauceCoverage = 0.75f,
                minToppings = 4,
                requiresOven = true
            ),
            presentationMetadata = PresentationMetadata(
                finishedDishLook = "Earthy forest wild mushrooms under fluttering paper-thin black truffle shavings",
                backgroundTheme = "BG_ITALIAN_KITCHEN"
            )
        ),
        Recipe(
            recipeId = "R-16",
            name = "Circus Fun Carnival Pizza",
            description = "Vibrant red and white circus tent swirl dough, sweet strawberry glaze, caramel pretzels, and freshly popped popcorn.",
            category = "Fantasy & Fun",
            difficulty = 4,
            isNoBake = false,
            crustId = "ING-004",
            sauceId = "ING-017",
            cheeseId = null,
            toppingIds = listOf("ING-070", "ING-071"),
            toolIds = listOf("TOOL-001", "TOOL-006", "TOOL-007", "TOOL-013"),
            specialMechanics = "GLASS_POPCORN_BURST",
            completionRules = CompletionRules(
                minSauceCoverage = 0.75f,
                minToppings = 4,
                requiresOven = true
            ),
            presentationMetadata = PresentationMetadata(
                finishedDishLook = "Red and white circus striped crust topped with fluffy white popped popcorn and caramel pretzels",
                backgroundTheme = "BG_CARNIVAL_CIRCUS"
            )
        ),
        Recipe(
            recipeId = "R-17",
            name = "Berry Berry Fruit Tart Pizza",
            description = "Elegant chilled French dessert pizza with fluted tart shell, whipped custard cream, wild berries, and pure honey drizzle.",
            category = "Sweet & Dessert",
            difficulty = 3,
            isNoBake = true,
            crustId = "ING-008",
            sauceId = "ING-017",
            cheeseId = null,
            toppingIds = listOf("ING-052", "ING-053", "ING-062"),
            toolIds = listOf("TOOL-006", "TOOL-008", "TOOL-011", "TOOL-012"),
            specialMechanics = "HONEY_DRIZZLE_CHILL",
            completionRules = CompletionRules(
                minSauceCoverage = 0.8f,
                minToppings = 5,
                requiresOven = false
            ),
            presentationMetadata = PresentationMetadata(
                finishedDishLook = "Concentric rings of jewel-like wild berries with glossy golden honey sheen",
                backgroundTheme = "BG_CANDY_DREAMLAND"
            )
        ),
        Recipe(
            recipeId = "R-18",
            name = "Volcano Spicy Pepper Blast Pizza",
            description = "Fiery paprika volcano dough mound filled with bubbling arrabbiata lava, hot jalapenos, habaneros, and fiery cheese.",
            category = "Special Occasions & Breakfast",
            difficulty = 4,
            isNoBake = false,
            crustId = "ING-001",
            sauceId = "ING-011",
            cheeseId = "ING-025",
            toppingIds = listOf("ING-036", "ING-037", "ING-033"),
            toolIds = listOf("TOOL-001", "TOOL-006", "TOOL-007", "TOOL-015"),
            specialMechanics = "VOLCANO_CRATER_CHILI_TONGS",
            completionRules = CompletionRules(
                minSauceCoverage = 0.8f,
                minToppings = 5,
                requiresOven = true
            ),
            presentationMetadata = PresentationMetadata(
                finishedDishLook = "Dramatic smoking cheese crater overflowing with fiery red and yellow chilies",
                backgroundTheme = "BG_ITALIAN_KITCHEN"
            )
        ),
        Recipe(
            recipeId = "R-19",
            name = "Garden Veggie Bloom Pizza",
            description = "Botanical garden on 6-petal herb crust with bright basil pesto, whipped ricotta clouds, and zucchini ribbon roses.",
            category = "Special Occasions & Breakfast",
            difficulty = 4,
            isNoBake = false,
            crustId = "ING-009",
            sauceId = "ING-018",
            cheeseId = "ING-024",
            toppingIds = listOf("ING-038", "ING-039"),
            toolIds = listOf("TOOL-001", "TOOL-002", "TOOL-006", "TOOL-007", "TOOL-012"),
            specialMechanics = "VEGGIE_RIBBON_ROSE_ROLL",
            completionRules = CompletionRules(
                minSauceCoverage = 0.8f,
                minToppings = 4,
                requiresOven = true
            ),
            presentationMetadata = PresentationMetadata(
                finishedDishLook = "Emerald green basil pesto garden blossoming with yellow zucchini roses and edible pansies",
                backgroundTheme = "BG_ITALIAN_KITCHEN"
            )
        ),
        Recipe(
            recipeId = "R-20",
            name = "Royal Galaxy Cosmic Pizza",
            description = "Interstellar journey on indigo nebula crust with cosmic blueberry black-hole swirl, planetary cake-pops, and aurora lights.",
            category = "Fantasy & Fun",
            difficulty = 5,
            isNoBake = false,
            crustId = "ING-010",
            sauceId = "ING-020",
            cheeseId = "ING-021",
            toppingIds = listOf("ING-072", "ING-073"),
            toolIds = listOf("TOOL-001", "TOOL-006", "TOOL-007", "TOOL-008"),
            specialMechanics = "GALAXY_VORTEX_AURORA",
            completionRules = CompletionRules(
                minSauceCoverage = 0.85f,
                minToppings = 5,
                requiresOven = true
            ),
            presentationMetadata = PresentationMetadata(
                finishedDishLook = "Cosmic deep indigo swirl with planetary cake pops orbiting under northern aurora glow",
                backgroundTheme = "BG_COSMIC_GALAXY"
            )
        )
    ).associateBy { it.recipeId }

    fun getAllRecipes(): List<Recipe> = masterRecipes.values.toList()

    fun getRecipeById(id: String): Recipe? = masterRecipes[id]

    fun hasRecipe(id: String): Boolean = masterRecipes.containsKey(id)

    fun getRecipesByCategory(category: String): List<Recipe> = masterRecipes.values.filter { it.category == category }

    fun getNoBakeRecipes(): List<Recipe> = masterRecipes.values.filter { it.isNoBake }
}
