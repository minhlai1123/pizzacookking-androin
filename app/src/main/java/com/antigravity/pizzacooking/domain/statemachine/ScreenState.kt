package com.antigravity.pizzacooking.domain.statemachine

enum class ScreenState(val screenId: String, val screenName: String) {
    SPLASH("M-01", "Splash Screen"),
    MAIN_MENU("M-02", "Main Menu"),
    RECIPE_SELECTION("M-03", "Recipe Selection"),
    RECIPE_PREPARATION("M-04", "Recipe Preparation"),
    TOPPING_ASSEMBLY("M-05", "Topping Assembly"),
    BAKING_OVEN("M-06", "Baking Oven"),
    CUTTING_SERVING("M-07", "Cutting & Serving"),
    RESULT_EVALUATION("M-08", "Result Evaluation"),
    CREATIVE_MODE("M-09", "Creative Mode Freeform"),
    SETTINGS_PARENTAL("M-10", "Settings & Parental Gate")
}
