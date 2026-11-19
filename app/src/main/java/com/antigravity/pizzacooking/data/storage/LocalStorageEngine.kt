package com.antigravity.pizzacooking.data.storage

import com.antigravity.pizzacooking.data.models.CreativeModeStats
import com.antigravity.pizzacooking.data.models.RecipeProgress
import com.antigravity.pizzacooking.data.models.SaveData
import com.antigravity.pizzacooking.data.models.SettingsData
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class LocalStorageEngine(
    private val storageDir: File,
    private val fileName: String = "pizza_cooking_save.json"
) {
    private val saveFile: File = File(storageDir, fileName)
    private val backupFile: File = File(storageDir, "$fileName.bak")
    private val tempFile: File = File(storageDir, "$fileName.tmp")

    @Synchronized
    fun load(): SaveData {
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }

        if (!saveFile.exists()) {
            val initial = createDefaultSaveData()
            save(initial)
            return initial
        }

        return try {
            val content = saveFile.readText(Charsets.UTF_8)
            parseSaveData(content)
        } catch (e: Exception) {
            // Corruption detected, try backup
            tryRestoreFromBackup() ?: run {
                val fallback = createDefaultSaveData()
                save(fallback)
                fallback
            }
        }
    }

    @Synchronized
    fun save(data: SaveData): Boolean {
        return try {
            if (!storageDir.exists()) {
                storageDir.mkdirs()
            }

            val jsonString = serializeSaveData(data)

            // Write to temporary file first
            FileOutputStream(tempFile).use { fos ->
                fos.write(jsonString.toByteArray(Charsets.UTF_8))
                fos.flush()
                fos.fd.sync()
            }

            // Atomic rename temp to primary
            val moved = if (tempFile.renameTo(saveFile)) {
                true
            } else {
                // Fallback for systems where renameTo fails across boundaries
                tempFile.copyTo(saveFile, overwrite = true)
                tempFile.delete()
                true
            }

            if (moved) {
                // Keep backup copy of verified valid save
                saveFile.copyTo(backupFile, overwrite = true)
            }
            moved
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    @Synchronized
    fun updateRecipeScore(recipeId: String, stars: Int): Boolean {
        val current = load()
        val currentProgress = current.recipesProgress[recipeId] ?: RecipeProgress(unlocked = true, stars = 0, timesCompleted = 0)
        val updatedProgress = currentProgress.copy(
            stars = maxOf(currentProgress.stars, stars),
            timesCompleted = currentProgress.timesCompleted + 1
        )
        val updatedMap = current.recipesProgress.toMutableMap()
        updatedMap[recipeId] = updatedProgress
        return save(current.copy(recipesProgress = updatedMap))
    }

    @Synchronized
    fun incrementCreativeCount(): Boolean {
        val current = load()
        val updatedStats = current.creativeModeStats.copy(
            pizzasCreated = current.creativeModeStats.pizzasCreated + 1
        )
        return save(current.copy(creativeModeStats = updatedStats))
    }

    @Synchronized
    fun updateSettings(musicVolume: Float, sfxVolume: Float, hapticEnabled: Boolean): Boolean {
        val current = load()
        val updatedSettings = SettingsData(
            musicVolume = musicVolume.coerceIn(0.0f, 1.0f),
            sfxVolume = sfxVolume.coerceIn(0.0f, 1.0f),
            hapticEnabled = hapticEnabled
        )
        return save(current.copy(settings = updatedSettings))
    }

    @Synchronized
    fun resetData(): Boolean {
        val defaultData = createDefaultSaveData()
        return save(defaultData)
    }

    private fun tryRestoreFromBackup(): SaveData? {
        return if (backupFile.exists()) {
            try {
                val content = backupFile.readText(Charsets.UTF_8)
                val restored = parseSaveData(content)
                // Overwrite corrupted main file with valid backup
                save(restored)
                restored
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }

    fun createDefaultSaveData(): SaveData {
        val defaultProgress = mutableMapOf<String, RecipeProgress>()
        for (i in 1..20) {
            val id = String.format("R-%02d", i)
            defaultProgress[id] = RecipeProgress(unlocked = true, stars = 0, timesCompleted = 0)
        }
        return SaveData(
            appVersion = "1.0.0",
            schemaVersion = 1,
            settings = SettingsData(),
            recipesProgress = defaultProgress,
            creativeModeStats = CreativeModeStats(pizzasCreated = 0)
        )
    }

    fun serializeSaveData(data: SaveData): String {
        val sb = StringBuilder()
        sb.append("{\n")
        sb.append("  \"appVersion\": \"${escapeJson(data.appVersion)}\",\n")
        sb.append("  \"schemaVersion\": ${data.schemaVersion},\n")
        sb.append("  \"settings\": {\n")
        sb.append("    \"musicVolume\": ${data.settings.musicVolume},\n")
        sb.append("    \"sfxVolume\": ${data.settings.sfxVolume},\n")
        sb.append("    \"hapticEnabled\": ${data.settings.hapticEnabled}\n")
        sb.append("  },\n")
        sb.append("  \"recipesProgress\": {\n")
        val recipeEntries = data.recipesProgress.entries.toList()
        for (i in recipeEntries.indices) {
            val (key, value) = recipeEntries[i]
            val comma = if (i < recipeEntries.size - 1) "," else ""
            sb.append("    \"${escapeJson(key)}\": {\n")
            sb.append("      \"unlocked\": ${value.unlocked},\n")
            sb.append("      \"stars\": ${value.stars},\n")
            sb.append("      \"timesCompleted\": ${value.timesCompleted}\n")
            sb.append("    }$comma\n")
        }
        sb.append("  },\n")
        sb.append("  \"creativeModeStats\": {\n")
        sb.append("    \"pizzasCreated\": ${data.creativeModeStats.pizzasCreated}\n")
        sb.append("  }\n")
        sb.append("}")
        return sb.toString()
    }

    fun parseSaveData(json: String): SaveData {
        val trimmed = json.trim()
        if (!trimmed.startsWith("{") || !trimmed.endsWith("}") || !trimmed.contains("\"recipesProgress\"") || !trimmed.contains("\"appVersion\"")) {
            throw IllegalArgumentException("Corrupted JSON: Invalid structure or missing required keys")
        }

        // Robust regex-based lightweight parser for schema migration and offline safety
        val appVersion = extractString(json, "appVersion") ?: "1.0.0"
        val schemaVersion = extractInt(json, "schemaVersion") ?: 1

        val musicVolume = extractFloat(json, "musicVolume") ?: 0.8f
        val sfxVolume = extractFloat(json, "sfxVolume") ?: 1.0f
        val hapticEnabled = extractBoolean(json, "hapticEnabled") ?: true

        val pizzasCreated = extractInt(json, "pizzasCreated") ?: 0

        // Parse recipes
        val recipes = mutableMapOf<String, RecipeProgress>()
        for (i in 1..20) {
            val id = String.format("R-%02d", i)
            val recipeBlock = extractObjectBlock(json, id)
            if (recipeBlock != null) {
                val unlocked = extractBoolean(recipeBlock, "unlocked") ?: true
                val stars = extractInt(recipeBlock, "stars") ?: 0
                val times = extractInt(recipeBlock, "timesCompleted") ?: 0
                recipes[id] = RecipeProgress(unlocked = unlocked, stars = stars, timesCompleted = times)
            } else {
                recipes[id] = RecipeProgress(unlocked = true, stars = 0, timesCompleted = 0)
            }
        }

        // Schema migration check
        val finalSchemaVersion = if (schemaVersion < 1) 1 else schemaVersion

        return SaveData(
            appVersion = appVersion,
            schemaVersion = finalSchemaVersion,
            settings = SettingsData(musicVolume, sfxVolume, hapticEnabled),
            recipesProgress = recipes,
            creativeModeStats = CreativeModeStats(pizzasCreated)
        )
    }

    private fun escapeJson(str: String): String {
        return str.replace("\\", "\\\\").replace("\"", "\\\"")
    }

    private fun extractString(json: String, key: String): String? {
        val pattern = Regex("\"$key\"\\s*:\\s*\"([^\"]*)\"")
        return pattern.find(json)?.groupValues?.get(1)
    }

    private fun extractInt(json: String, key: String): Int? {
        val pattern = Regex("\"$key\"\\s*:\\s*(\\d+)")
        return pattern.find(json)?.groupValues?.get(1)?.toIntOrNull()
    }

    private fun extractFloat(json: String, key: String): Float? {
        val pattern = Regex("\"$key\"\\s*:\\s*([0-9]*\\.?[0-9]+)")
        return pattern.find(json)?.groupValues?.get(1)?.toFloatOrNull()
    }

    private fun extractBoolean(json: String, key: String): Boolean? {
        val pattern = Regex("\"$key\"\\s*:\\s*(true|false)")
        return pattern.find(json)?.groupValues?.get(1)?.toBooleanStrictOrNull()
    }

    private fun extractObjectBlock(json: String, key: String): String? {
        val pattern = Regex("\"$key\"\\s*:\\s*\\{([^\\}]*)\\}")
        return pattern.find(json)?.groupValues?.get(1)
    }
}
