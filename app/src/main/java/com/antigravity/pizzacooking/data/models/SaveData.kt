package com.antigravity.pizzacooking.data.models

data class SettingsData(
    val musicVolume: Float = 0.8f,
    val sfxVolume: Float = 1.0f,
    val hapticEnabled: Boolean = true
)

data class RecipeProgress(
    val unlocked: Boolean = true,
    val stars: Int = 0,
    val timesCompleted: Int = 0
)

data class CreativeModeStats(
    val pizzasCreated: Int = 0
)

data class SaveData(
    val appVersion: String = "1.0.0",
    val schemaVersion: Int = 1,
    val settings: SettingsData = SettingsData(),
    val recipesProgress: Map<String, RecipeProgress> = emptyMap(),
    val creativeModeStats: CreativeModeStats = CreativeModeStats()
)
