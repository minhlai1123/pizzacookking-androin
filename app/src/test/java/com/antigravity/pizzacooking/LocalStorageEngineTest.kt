package com.antigravity.pizzacooking

import com.antigravity.pizzacooking.data.storage.LocalStorageEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class LocalStorageEngineTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    @Test
    fun localStorage_createsDefaultSaveDataWithAllTwentyRecipesUnlocked() {
        val storageDir = tempFolder.newFolder("pizzasave_test")
        val engine = LocalStorageEngine(storageDir)

        val data = engine.load()
        assertNotNull(data)
        assertEquals("1.0.0", data.appVersion)
        assertEquals(1, data.schemaVersion)
        assertEquals(20, data.recipesProgress.size)

        for (i in 1..20) {
            val id = String.format("R-%02d", i)
            val progress = data.recipesProgress[id]
            assertNotNull("Recipe progress for $id must exist", progress)
            assertTrue("Recipe $id must be unlocked by default", progress!!.unlocked)
            assertEquals("Default stars must be 0", 0, progress.stars)
        }
    }

    @Test
    fun localStorage_updatesRecipeScoreAndTimesCompleted() {
        val storageDir = tempFolder.newFolder("pizzasave_update_test")
        val engine = LocalStorageEngine(storageDir)

        engine.load()
        assertTrue(engine.updateRecipeScore("R-01", 3))

        val loaded = engine.load()
        val r01 = loaded.recipesProgress["R-01"]!!
        assertEquals(3, r01.stars)
        assertEquals(1, r01.timesCompleted)

        // Updating with lower stars shouldn't decrease high score
        engine.updateRecipeScore("R-01", 2)
        val loadedAgain = engine.load()
        assertEquals(3, loadedAgain.recipesProgress["R-01"]!!.stars)
        assertEquals(2, loadedAgain.recipesProgress["R-01"]!!.timesCompleted)
    }

    @Test
    fun localStorage_incrementsCreativeCount() {
        val storageDir = tempFolder.newFolder("pizzasave_creative_test")
        val engine = LocalStorageEngine(storageDir)

        engine.load()
        assertEquals(0, engine.load().creativeModeStats.pizzasCreated)

        engine.incrementCreativeCount()
        engine.incrementCreativeCount()

        assertEquals(2, engine.load().creativeModeStats.pizzasCreated)
    }

    @Test
    fun localStorage_atomicWriteCreatesBackup() {
        val storageDir = tempFolder.newFolder("pizzasave_backup_test")
        val engine = LocalStorageEngine(storageDir)

        engine.load()
        engine.updateRecipeScore("R-02", 2)

        val mainFile = File(storageDir, "pizza_cooking_save.json")
        val backupFile = File(storageDir, "pizza_cooking_save.json.bak")

        assertTrue("Main save file must exist", mainFile.exists())
        assertTrue("Backup save file must exist after secondary save", backupFile.exists())
    }

    @Test
    fun localStorage_recoversFromCorruptedPrimaryFileUsingBackup() {
        val storageDir = tempFolder.newFolder("pizzasave_recovery_test")
        val engine = LocalStorageEngine(storageDir)

        // Save initial valid data
        engine.load()
        engine.updateRecipeScore("R-05", 3)

        val mainFile = File(storageDir, "pizza_cooking_save.json")
        // Corrupt main file
        mainFile.writeText("{ corrupted_malformed_json: true, broken... }", Charsets.UTF_8)

        // Load should recover from backup
        val recovered = engine.load()
        assertNotNull(recovered)
        val r05 = recovered.recipesProgress["R-05"]!!
        assertEquals("Recovered data must preserve R-05 stars", 3, r05.stars)
    }

    @Test
    fun localStorage_fallsBackToCleanDefaultsIfBothFilesCorrupted() {
        val storageDir = tempFolder.newFolder("pizzasave_total_corruption_test")
        val engine = LocalStorageEngine(storageDir)

        engine.load()
        engine.updateRecipeScore("R-05", 3)

        val mainFile = File(storageDir, "pizza_cooking_save.json")
        val backupFile = File(storageDir, "pizza_cooking_save.json.bak")

        mainFile.writeText("corrupted", Charsets.UTF_8)
        backupFile.writeText("corrupted", Charsets.UTF_8)

        // Load should not crash, but fallback to fresh default save data
        val safeFallback = engine.load()
        assertNotNull(safeFallback)
        assertEquals(20, safeFallback.recipesProgress.size)
    }
}
