package com.antigravity.pizzacooking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.antigravity.pizzacooking.data.repository.RecipeRepository
import com.antigravity.pizzacooking.domain.statemachine.GameMode
import com.antigravity.pizzacooking.presentation.components.SafeAreaContainer
import com.antigravity.pizzacooking.presentation.screens.KitchenScreen
import com.antigravity.pizzacooking.presentation.screens.KitchenViewModel
import com.antigravity.pizzacooking.presentation.screens.MainMenuScreen
import com.antigravity.pizzacooking.presentation.screens.RecipeSelectionScreen
import com.antigravity.pizzacooking.presentation.screens.SplashScreen
import com.antigravity.pizzacooking.presentation.theme.PizzaCookingWorldTheme

enum class ScreenState {
    SPLASH,
    MAIN_MENU,
    RECIPE_SELECTION,
    KITCHEN
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PizzaCookingWorldTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SafeAreaContainer {
                        PizzaCookingAppNavigation()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        com.antigravity.pizzacooking.presentation.theme.SoundEffects.startBgm()
    }

    override fun onPause() {
        super.onPause()
        com.antigravity.pizzacooking.presentation.theme.SoundEffects.stopBgm()
    }
}

@Composable
fun PizzaCookingAppNavigation() {
    var currentScreen by remember { mutableStateOf(ScreenState.SPLASH) }
    val kitchenViewModel = remember { KitchenViewModel() }

    when (currentScreen) {
        ScreenState.SPLASH -> {
            SplashScreen(
                onSplashFinished = {
                    currentScreen = ScreenState.MAIN_MENU
                }
            )
        }
        ScreenState.MAIN_MENU -> {
            MainMenuScreen(
                onPlayClicked = {
                    currentScreen = ScreenState.RECIPE_SELECTION
                },
                onGrownUpsClicked = {
                    // Parental Gate Modal
                }
            )
        }
        ScreenState.RECIPE_SELECTION -> {
            RecipeSelectionScreen(
                onRecipeSelected = { selectedRecipe ->
                    kitchenViewModel.initSession(selectedRecipe, GameMode.RECIPE_MODE)
                    currentScreen = ScreenState.KITCHEN
                },
                onCreativeModeSelected = {
                    val defaultRecipe = RecipeRepository.getAllRecipes().first()
                    kitchenViewModel.initSession(defaultRecipe, GameMode.CREATIVE_MODE)
                    currentScreen = ScreenState.KITCHEN
                },
                onBackClicked = {
                    currentScreen = ScreenState.MAIN_MENU
                }
            )
        }
        ScreenState.KITCHEN -> {
            KitchenScreen(
                viewModel = kitchenViewModel,
                onHomeClicked = {
                    currentScreen = ScreenState.RECIPE_SELECTION
                }
            )
        }
    }
}
