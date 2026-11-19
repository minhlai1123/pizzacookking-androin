package com.antigravity.pizzacooking.domain.statemachine

enum class GameMode {
    RECIPE_MODE,
    CREATIVE_MODE
}

enum class CookingState {
    NOT_STARTED,
    IN_PROGRESS,
    STEP_COMPLETED,
    READY_FOR_NEXT_STEP,
    COMPLETED,
    SKIPPED
}

enum class InteractionType {
    TAP,
    SWIPE,
    DRAG_AND_DROP,
    HOLD,
    TODDLER_TAP_FALLBACK
}
