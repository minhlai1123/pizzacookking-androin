package com.antigravity.pizzacooking.data.repository

import com.antigravity.pizzacooking.data.models.Ingredient

object IngredientRepository {
    private val masterIngredients: Map<String, Ingredient> = listOf(
        Ingredient("ING-001", "Classic Round Dough", "CRUST", "crust_classic_round", listOf("R-01", "R-02", "R-05", "R-06", "R-11", "R-12", "R-14"), listOf("TOOL-001"), "SWIPE", "Knead and roll"),
        Ingredient("ING-002", "Fresh Watermelon Slab", "CRUST", "crust_watermelon_slab", listOf("R-03"), listOf("TOOL-002"), "SWIPE", "Slice thick round slab"),
        Ingredient("ING-003", "Chocolate Cookie Dough", "CRUST", "crust_chocolate_cookie", listOf("R-04"), listOf("TOOL-001"), "SWIPE", "Roll out cookie dough"),
        Ingredient("ING-004", "Rainbow Swirl Dough", "CRUST", "crust_rainbow_swirl", listOf("R-09"), listOf("TOOL-001"), "SWIPE", "Braid 5 pastel dough ropes"),
        Ingredient("ING-005", "Black Charcoal Dough", "CRUST", "crust_black_charcoal", listOf("R-08"), listOf("TOOL-001"), "SWIPE", "Roll out dark bamboo charcoal dough"),
        Ingredient("ING-006", "Snowflake 6-Point Dough", "CRUST", "crust_snowflake_cut", listOf("R-10"), listOf("TOOL-001", "TOOL-012"), "TAP", "Stamp with snowflake cutter"),
        Ingredient("ING-007", "Rustic Sourdough Oval", "CRUST", "crust_sourdough_oval", listOf("R-15"), listOf("TOOL-001"), "SWIPE", "Shape oval artisan sourdough"),
        Ingredient("ING-008", "Scalloped Tart Shell", "CRUST", "crust_scalloped_tart", listOf("R-17"), listOf("TOOL-012"), "TAP", "Press into fluted tart mold"),
        Ingredient("ING-009", "Flower 6-Petal Herb Dough", "CRUST", "crust_flower_petal", listOf("R-19"), listOf("TOOL-001", "TOOL-012"), "TAP", "Stamp into 6-petal flower"),
        Ingredient("ING-010", "Cosmic Indigo Dough", "CRUST", "crust_cosmic_indigo", listOf("R-20"), listOf("TOOL-001"), "SWIPE", "Roll out deep indigo starry dough"),
        Ingredient("ING-011", "San Marzano Tomato Sauce", "SAUCE", "sauce_san_marzano", listOf("R-01", "R-02", "R-06"), listOf("TOOL-006"), "SWIPE", "Simmer with fresh basil"),
        Ingredient("ING-012", "Creamy Garlic Alfredo", "SAUCE", "sauce_garlic_alfredo", listOf("R-05", "R-10"), listOf("TOOL-006"), "SWIPE", "Melt cream and butter"),
        Ingredient("ING-013", "Pastel Rainbow Fruit Glaze", "SAUCE", "sauce_rainbow_puree", listOf("R-03"), listOf("TOOL-003", "TOOL-004", "TOOL-006"), "HOLD", "Blend coconut cream and fresh fruits"),
        Ingredient("ING-014", "Hazelnut Chocolate Spread", "SAUCE", "sauce_hazelnut_chocolate", listOf("R-04"), listOf("TOOL-006"), "SWIPE", "Warm gently for silky spread"),
        Ingredient("ING-015", "Glowing Pumpkin Puree", "SAUCE", "sauce_pumpkin_puree", listOf("R-08"), listOf("TOOL-006"), "SWIPE", "Puree sweet orange pumpkin"),
        Ingredient("ING-016", "Honey Teriyaki Glaze", "SAUCE", "sauce_honey_teriyaki", listOf("R-12"), listOf("TOOL-010"), "SWIPE", "Simmer soy and honey until glossy"),
        Ingredient("ING-017", "Strawberry Cotton Candy Puree", "SAUCE", "sauce_strawberry_candy", listOf("R-09", "R-16"), listOf("TOOL-006"), "SWIPE", "Melt strawberries and cotton candy"),
        Ingredient("ING-018", "Vibrant Basil Pesto", "SAUCE", "sauce_basil_pesto", listOf("R-19"), listOf("TOOL-006"), "SWIPE", "Crush basil, pine nuts, and olive oil"),
        Ingredient("ING-019", "Smoky Barbecue Glaze", "SAUCE", "sauce_smoky_bbq", listOf("R-11"), listOf("TOOL-010"), "SWIPE", "Simmer dark brown smoky glaze"),
        Ingredient("ING-020", "Interstellar Blueberry Swirl", "SAUCE", "sauce_cosmic_blueberry", listOf("R-20"), listOf("TOOL-006"), "SWIPE", "Puree wild blackberries and blueberries into cosmic vortex"),
        Ingredient("ING-021", "Fresh Mozzarella Slices", "CHEESE", "cheese_mozzarella_slices", listOf("R-01", "R-02", "R-05", "R-06", "R-14"), listOf("TOOL-002"), "DRAG_AND_DROP", "Slice fresh round cheese"),
        Ingredient("ING-022", "Shredded Golden Cheddar", "CHEESE", "cheese_shredded_cheddar", listOf("R-05", "R-07", "R-11", "R-13"), listOf("TOOL-005"), "SWIPE", "Grate cheese block on box grater"),
        Ingredient("ING-023", "Star-Cut Cheese Slices", "CHEESE", "cheese_star_cut", listOf("R-09"), listOf("TOOL-012"), "TAP", "Stamp cheese with star cutter"),
        Ingredient("ING-024", "Whipped Ricotta Dollops", "CHEESE", "cheese_ricotta_dollops", listOf("R-19"), listOf("TOOL-006"), "DRAG_AND_DROP", "Scoop light fluffy cloud dollops"),
        Ingredient("ING-025", "Lava Red Cheddar", "CHEESE", "cheese_lava_red", listOf("R-18"), listOf("TOOL-005"), "SWIPE", "Shred spicy red habanero cheddar"),
        Ingredient("ING-026", "Powdered Snow Parmesan", "CHEESE", "cheese_snow_parmesan", listOf("R-05", "R-10", "R-15"), listOf("TOOL-005"), "SWIPE", "Shake fine snowy dusting"),
        Ingredient("ING-030", "San Marzano Basil Leaves", "TOPPING_VEGGIE", "top_basil_leaves", listOf("R-01"), listOf(), "DRAG_AND_DROP", "Pluck fresh leaves"),
        Ingredient("ING-031", "Sliced Green Bell Peppers", "TOPPING_VEGGIE", "top_green_peppers", listOf("R-02", "R-10"), listOf("TOOL-002"), "DRAG_AND_DROP", "Slice into rings"),
        Ingredient("ING-032", "Black Kalamata Olives", "TOPPING_VEGGIE", "top_black_olives", listOf("R-02", "R-08", "R-19"), listOf("TOOL-002"), "DRAG_AND_DROP", "Cut olive rounds & spider legs"),
        Ingredient("ING-033", "Red Onion Rings", "TOPPING_VEGGIE", "top_red_onions", listOf("R-11", "R-18"), listOf("TOOL-002"), "DRAG_AND_DROP", "Slice rings"),
        Ingredient("ING-034", "Wild Forest Chanterelles", "TOPPING_VEGGIE", "top_wild_chanterelles", listOf("R-15"), listOf("TOOL-002"), "DRAG_AND_DROP", "Slice forest mushrooms"),
        Ingredient("ING-035", "Black Truffle Shavings", "TOPPING_VEGGIE", "top_black_truffle", listOf("R-15"), listOf("TOOL-014"), "SWIPE", "Shave paper-thin on wooden slicer"),
        Ingredient("ING-036", "Green Jalapeno Rings", "TOPPING_VEGGIE", "top_green_jalapeno", listOf("R-13", "R-18"), listOf("TOOL-015"), "DRAG_AND_DROP", "Pick with silicone tongs"),
        Ingredient("ING-037", "Habanero Chili Peppers", "TOPPING_VEGGIE", "top_habanero_chili", listOf("R-18"), listOf("TOOL-015"), "DRAG_AND_DROP", "Pick with silicone tongs"),
        Ingredient("ING-038", "Zucchini Ribbon Roses", "TOPPING_VEGGIE", "top_zucchini_roses", listOf("R-19"), listOf("TOOL-002"), "DRAG_AND_DROP", "Roll thin ribbon into rose"),
        Ingredient("ING-039", "Edible Pansy Flowers", "TOPPING_VEGGIE", "top_pansy_flowers", listOf("R-19"), listOf(), "DRAG_AND_DROP", "Gentle floral garnish"),
        Ingredient("ING-040", "Spicy Pepperoni Slices", "TOPPING_MEAT", "top_pepperoni_slices", listOf("R-02"), listOf("TOOL-002"), "DRAG_AND_DROP", "Slice pepperoni stick"),
        Ingredient("ING-041", "Honey Glazed Diced Ham", "TOPPING_MEAT", "top_diced_ham", listOf("R-06"), listOf("TOOL-002"), "DRAG_AND_DROP", "Dice smoked ham"),
        Ingredient("ING-042", "Octopus Mini Frankfurters", "TOPPING_MEAT", "top_octopus_franks", listOf("R-07"), listOf("TOOL-002"), "DRAG_AND_DROP", "Notch frankfurter into tentacles"),
        Ingredient("ING-043", "Shredded BBQ Chicken", "TOPPING_MEAT", "top_shredded_chicken", listOf("R-11"), listOf(), "DRAG_AND_DROP", "Pull tender grilled chicken"),
        Ingredient("ING-044", "Crispy Bacon Strips", "TOPPING_MEAT", "top_crispy_bacon", listOf("R-14"), listOf(), "DRAG_AND_DROP", "Grill bacon ribbons"),
        Ingredient("ING-045", "Takoyaki Baby Octopus Bites", "TOPPING_MEAT", "top_baby_octopus", listOf("R-12"), listOf(), "DRAG_AND_DROP", "Boil tender octopus pieces"),
        Ingredient("ING-046", "Dancing Bonito Flakes", "TOPPING_MEAT", "top_bonito_flakes", listOf("R-12"), listOf(), "DRAG_AND_DROP", "Scatter on hot crust to dance"),
        Ingredient("ING-047", "Seasoned Taco Minced Beef", "TOPPING_MEAT", "top_taco_beef", listOf("R-13"), listOf(), "DRAG_AND_DROP", "Brown spiced ground beef"),
        Ingredient("ING-048", "Fresh Farm Egg", "TOPPING_MEAT", "top_farm_egg", listOf("R-14"), listOf(), "TAP", "Crack shell into central dough nest"),
        Ingredient("ING-050", "Mango Carved Sea Turtles", "TOPPING_FRUIT", "top_mango_turtles", listOf("R-03"), listOf("TOOL-002"), "DRAG_AND_DROP", "Carve sweet mango into turtle"),
        Ingredient("ING-051", "Sweet Pineapple Chunks", "TOPPING_FRUIT", "top_pineapple_chunks", listOf("R-06"), listOf("TOOL-002"), "DRAG_AND_DROP", "Slice tropical pineapple"),
        Ingredient("ING-052", "Fresh Wild Berries", "TOPPING_FRUIT", "top_wild_berries", listOf("R-17"), listOf(), "DRAG_AND_DROP", "Wash and sort raspberries and blueberries"),
        Ingredient("ING-053", "Sliced Fresh Strawberries", "TOPPING_FRUIT", "top_strawberry_slices", listOf("R-04", "R-17"), listOf("TOOL-002"), "DRAG_AND_DROP", "Slice strawberries thin"),
        Ingredient("ING-060", "Sour Cream Spiderweb", "TOPPING_SWEET", "top_sour_cream_web", listOf("R-08"), listOf("TOOL-009"), "SWIPE", "Pipe concentric web pattern"),
        Ingredient("ING-061", "Japanese Kewpie Mayo", "TOPPING_SWEET", "top_kewpie_mayo", listOf("R-12"), listOf("TOOL-010"), "SWIPE", "Squeeze 3-strip zig-zag lines"),
        Ingredient("ING-062", "Pure Golden Honey", "TOPPING_SWEET", "top_golden_honey", listOf("R-17"), listOf("TOOL-011"), "SWIPE", "Drizzle golden sheen with wooden wand"),
        Ingredient("ING-063", "Mini Beach Lifebuoy Candies", "TOPPING_SWEET", "top_lifebuoy_candies", listOf("R-03"), listOf(), "DRAG_AND_DROP", "Candy ring garnish"),
        Ingredient("ING-064", "Rainbow Gummy Bears", "TOPPING_SWEET", "top_gummy_bears", listOf("R-04"), listOf(), "DRAG_AND_DROP", "Assorted colorful chewy bears"),
        Ingredient("ING-065", "Twisted Marshmallow Swirls", "TOPPING_SWEET", "top_marshmallows", listOf("R-04"), listOf(), "DRAG_AND_DROP", "Fluffy vanilla marshmallows"),
        Ingredient("ING-066", "Gold Chocolate Doubloons", "TOPPING_SWEET", "top_gold_doubloons", listOf("R-07"), listOf(), "DRAG_AND_DROP", "Pirate treasure coins"),
        Ingredient("ING-067", "Skull & Crossbones Biscuits", "TOPPING_SWEET", "top_skull_biscuits", listOf("R-07", "R-08"), listOf(), "DRAG_AND_DROP", "Spooky ginger cookies"),
        Ingredient("ING-068", "Unicorn Horn Fondant Swirl", "TOPPING_SWEET", "top_unicorn_horn", listOf("R-09"), listOf(), "DRAG_AND_DROP", "Spiral golden horn center"),
        Ingredient("ING-069", "Pastel Angel Meringue Wings", "TOPPING_SWEET", "top_meringue_wings", listOf("R-09"), listOf(), "DRAG_AND_DROP", "Pair of crispy light wings"),
        Ingredient("ING-070", "Fresh Popped Popcorn", "TOPPING_SWEET", "top_popped_popcorn", listOf("R-16"), listOf("TOOL-013"), "DRAG_AND_DROP", "Pop kernels in lidded pan"),
        Ingredient("ING-071", "Caramel Pretzel Knots", "TOPPING_SWEET", "top_caramel_pretzels", listOf("R-16"), listOf(), "DRAG_AND_DROP", "Salted sweet crunchy twists"),
        Ingredient("ING-072", "Planetary Sphere Cake-Pops", "TOPPING_SWEET", "top_planet_cakepops", listOf("R-20"), listOf(), "DRAG_AND_DROP", "Saturn, Earth, Jupiter orb candies"),
        Ingredient("ING-073", "Crystal Sugar Meteorites", "TOPPING_SWEET", "top_sugar_meteorites", listOf("R-20"), listOf(), "DRAG_AND_DROP", "Prismatic edible sparkling rocks"),
        Ingredient("ING-074", "Candy Canes & Reindeer Gingerbread", "TOPPING_SWEET", "top_christmas_novelties", listOf("R-10"), listOf(), "DRAG_AND_DROP", "Peppermint candy and holiday cookies"),
        Ingredient("ING-075", "Golden Roasted Garlic Cloves", "TOPPING_VEGGIE", "topping_roasted_garlic", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Scatter mellow roasted cloves"),
        Ingredient("ING-076", "Creamy Avocado Slices", "TOPPING_VEGGIE", "topping_avocado_slices", listOf("CREATIVE_MODE"), listOf("TOOL-002"), "DRAG_AND_DROP", "Slice fresh green avocado"),
        Ingredient("ING-077", "Tender Artichoke Hearts", "TOPPING_VEGGIE", "topping_artichoke_hearts", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Place marinated tender hearts"),
        Ingredient("ING-078", "Steamed Broccoli Florets", "TOPPING_VEGGIE", "topping_broccoli_florets", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Scatter cute mini green broccoli trees"),
        Ingredient("ING-079", "Sweet Baby Green Peas", "TOPPING_VEGGIE", "topping_green_peas", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Sprinkle popping green garden peas"),
        Ingredient("ING-080", "Curled Zucchini Ribbons", "TOPPING_VEGGIE", "topping_zucchini_ribbons", listOf("CREATIVE_MODE"), listOf("TOOL-002"), "DRAG_AND_DROP", "Twirl green summer zucchini ribbons"),
        Ingredient("ING-081", "Sun-Dried Ruby Tomatoes", "TOPPING_VEGGIE", "topping_sundried_tomatoes", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Arrange chewy intensely sweet sun-dried slices"),
        Ingredient("ING-082", "Fragrant Rosemary Sprigs", "TOPPING_VEGGIE", "topping_rosemary_sprigs", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Gently lay aromatic herbal needles"),
        Ingredient("ING-083", "Carrot Cutout Stars", "TOPPING_VEGGIE", "topping_carrot_stars", listOf("CREATIVE_MODE"), listOf("TOOL-012"), "DRAG_AND_DROP", "Stamp orange carrot slices into star shapes"),
        Ingredient("ING-084", "Crisp Cucumber Wheels", "TOPPING_VEGGIE", "topping_cucumber_wheels", listOf("CREATIVE_MODE"), listOf("TOOL-002"), "DRAG_AND_DROP", "Slice thin refreshing cucumber circles"),
        Ingredient("ING-085", "Boiled Octopus Tentacles", "TOPPING_MEAT", "topping_octopus_tentacles", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Place purple curly ocean tentacles"),
        Ingredient("ING-086", "Sausage Octopus Cuties", "TOPPING_MEAT", "topping_sausage_octopus", listOf("CREATIVE_MODE"), listOf("TOOL-002"), "DRAG_AND_DROP", "Notch wiener ends to bloom into 8 legs"),
        Ingredient("ING-087", "Jumbo Coral Pink Shrimp", "TOPPING_MEAT", "topping_pink_shrimp", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Arrange curled poached pink shrimp"),
        Ingredient("ING-088", "Seared Golden Scallop Coins", "TOPPING_MEAT", "topping_golden_scallops", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Arrange sweet caramelized sea scallops"),
        Ingredient("ING-089", "Slow-Cooked Pulled Pork", "TOPPING_MEAT", "topping_pulled_pork", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Shred tender smoky barbecue pork"),
        Ingredient("ING-090", "Grilled Beef Steak Strips", "TOPPING_MEAT", "topping_steak_strips", listOf("CREATIVE_MODE"), listOf("TOOL-002"), "DRAG_AND_DROP", "Slice juicy seared steak slices"),
        Ingredient("ING-091", "Crispy Calamari Rings", "TOPPING_MEAT", "topping_calamari_rings", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Arrange golden crispy squid circles"),
        Ingredient("ING-092", "Mini Turkey Cocktail Sausages", "TOPPING_MEAT", "topping_cocktail_sausages", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Place bite-sized savory sausages"),
        Ingredient("ING-093", "Smoked Salmon Rosettes", "TOPPING_MEAT", "topping_salmon_rosettes", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Roll pink smoked salmon into flower roses"),
        Ingredient("ING-094", "Ocean Crab Surimi Chunks", "TOPPING_MEAT", "topping_crab_surimi", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Flake red and white sweet seafood sticks"),
        Ingredient("ING-095", "Sweet Ripe Peach Slices", "TOPPING_FRUIT", "topping_peach_slices", listOf("CREATIVE_MODE"), listOf("TOOL-002"), "DRAG_AND_DROP", "Slice fuzzy golden sweet summer peaches"),
        Ingredient("ING-096", "Fresh Wild Raspberries", "TOPPING_FRUIT", "topping_wild_raspberries", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Scatter tart ruby red berries"),
        Ingredient("ING-097", "Honey Purple Fig Halves", "TOPPING_FRUIT", "topping_fig_halves", listOf("CREATIVE_MODE"), listOf("TOOL-002"), "DRAG_AND_DROP", "Halve jammy honeyed purple figs"),
        Ingredient("ING-098", "Carved Mango Sea Turtles", "TOPPING_FRUIT", "topping_mango_sea_turtles", listOf("CREATIVE_MODE"), listOf("TOOL-002"), "DRAG_AND_DROP", "Shape sweet mango cheeks into swimming turtles"),
        Ingredient("ING-099", "Juicy Watermelon Mini Cubes", "TOPPING_FRUIT", "topping_watermelon_cubes", listOf("CREATIVE_MODE"), listOf("TOOL-002"), "DRAG_AND_DROP", "Dice crisp seedless pink watermelon"),
        Ingredient("ING-100", "Crisp Green Apple Moons", "TOPPING_FRUIT", "topping_apple_moons", listOf("CREATIVE_MODE"), listOf("TOOL-002"), "DRAG_AND_DROP", "Slice crescent moon tart green apples"),
        Ingredient("ING-101", "Seedless Sweet Red Grapes", "TOPPING_FRUIT", "topping_red_grapes", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Pluck plump juicy crimson grapes"),
        Ingredient("ING-102", "Cantaloupe Melon Pearls", "TOPPING_FRUIT", "topping_cantaloupe_pearls", listOf("CREATIVE_MODE"), listOf("TOOL-006"), "DRAG_AND_DROP", "Scoop round orange melon spheres"),
        Ingredient("ING-103", "Ruby Red Dragonfruit Cubes", "TOPPING_FRUIT", "topping_dragonfruit_cubes", listOf("CREATIVE_MODE"), listOf("TOOL-002"), "DRAG_AND_DROP", "Dice speckled vibrant magenta dragonfruit"),
        Ingredient("ING-104", "Golden Papaya Flowers", "TOPPING_FRUIT", "topping_papaya_flowers", listOf("CREATIVE_MODE"), listOf("TOOL-012"), "DRAG_AND_DROP", "Cut tropical golden papaya into flower shapes"),
        Ingredient("ING-105", "Rainbow Gummy Worms", "TOPPING_SWEET", "topping_gummy_worms", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Wiggle colorful sugary gummy worms"),
        Ingredient("ING-106", "Glazed Mini Donuts", "TOPPING_SWEET", "topping_mini_donuts", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Place strawberry glazed tiny donuts"),
        Ingredient("ING-107", "Rainbow Sour Belt Ribbons", "TOPPING_SWEET", "topping_sour_belts", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Drape tangy striped sour ribbons"),
        Ingredient("ING-108", "Chocolate Hazelnut Drops", "TOPPING_SWEET", "topping_hazelnut_drops", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Dot glossy milk chocolate coated nuts"),
        Ingredient("ING-109", "White Chocolate Buttons", "TOPPING_SWEET", "topping_white_choco_buttons", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Scatter sweet creamy white chocolate discs"),
        Ingredient("ING-110", "Toffee Butter Crunch Bits", "TOPPING_SWEET", "topping_toffee_crunch", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Sprinkle caramelized crunchy toffee nuggets"),
        Ingredient("ING-111", "Peppermint Candy Swirls", "TOPPING_SWEET", "topping_peppermint_swirls", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Place festive red and white mint spirals"),
        Ingredient("ING-112", "Crispy Puffed Rice Treats", "TOPPING_SWEET", "topping_puffed_rice", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Scatter crunchy marshmallow-bonded cereal clusters"),
        Ingredient("ING-113", "Pastel Bubblegum Balls", "TOPPING_SWEET", "topping_bubblegum_balls", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Scatter shiny candy-coated gum spheres"),
        Ingredient("ING-114", "Cinnamon Sugar Toast Cubes", "TOPPING_SWEET", "topping_cinnamon_toast", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Sprinkle warm fragrant crunchy brioche croutons"),
        Ingredient("ING-115", "Gummy Beach Pool Floaties", "TOPPING_FANTASY", "topping_beach_floaties", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Place candy flamingo and unicorn swim rings"),
        Ingredient("ING-116", "Sparkling Cyan Sugar Ice-Cubes", "TOPPING_FANTASY", "topping_cyan_ice_cubes", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Scatter chilled clear blue crystal gelatin gems"),
        Ingredient("ING-117", "Saturn Ring Planet Macarons", "TOPPING_FANTASY", "topping_saturn_macarons", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Place galaxy macarons with sugar rings"),
        Ingredient("ING-118", "Gingerbread Cookie Men", "TOPPING_FANTASY", "topping_gingerbread_men", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Place iced friendly holiday cookie figures"),
        Ingredient("ING-119", "Halloween Sugar Spiderweb Net", "TOPPING_FANTASY", "topping_sugar_spiderweb", listOf("CREATIVE_MODE"), listOf("TOOL-009"), "DRAG_AND_DROP", "Drape spun sugar creepy web patterns"),
        Ingredient("ING-120", "Dark Chocolate Bat Wings", "TOPPING_FANTASY", "topping_chocolate_bat_wings", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Plant crisp spooky cocoa wings"),
        Ingredient("ING-121", "Shimmering Edible Rainbow Stars", "TOPPING_FANTASY", "topping_rainbow_stars", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Scatter iridescent pastel wafer stars"),
        Ingredient("ING-122", "Starlight Glowing Sugar Pearls", "TOPPING_FANTASY", "topping_glowing_pearls", listOf("CREATIVE_MODE"), listOf("TOOL-015"), "DRAG_AND_DROP", "Scatter luminescent golden sugar dragees")
    ).associateBy { it.ingredientId }

    fun getAllIngredients(): List<Ingredient> = masterIngredients.values.toList()

    fun getIngredientById(id: String): Ingredient? = masterIngredients[id]

    fun hasIngredient(id: String): Boolean = masterIngredients.containsKey(id)

    fun getIngredientsByCategory(category: String): List<Ingredient> = masterIngredients.values.filter { it.category == category }

    fun getCrusts(): List<Ingredient> = masterIngredients.values.filter { it.category == "CRUST" }

    fun getSauces(): List<Ingredient> = masterIngredients.values.filter { it.category == "SAUCE" }

    fun getCheeses(): List<Ingredient> = masterIngredients.values.filter { it.category == "CHEESE" }

    fun getToppings(): List<Ingredient> = masterIngredients.values.filter { it.category.startsWith("TOPPING") }

    fun getIngredientsForRecipe(recipeId: String): List<Ingredient> = masterIngredients.values.filter { it.applicableRecipes.contains(recipeId) }
}
