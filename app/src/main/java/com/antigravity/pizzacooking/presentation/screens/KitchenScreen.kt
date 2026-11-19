package com.antigravity.pizzacooking.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.antigravity.pizzacooking.data.models.Ingredient
import com.antigravity.pizzacooking.domain.statemachine.GameMode
import com.antigravity.pizzacooking.presentation.components.CartoonToppingItem
import com.antigravity.pizzacooking.presentation.components.ResponsiveKitchenCanvas
import com.antigravity.pizzacooking.presentation.theme.BasilFreshGreen
import com.antigravity.pizzacooking.presentation.theme.BrushedSteelGray
import com.antigravity.pizzacooking.presentation.theme.CheeseYellow
import com.antigravity.pizzacooking.presentation.theme.CreamDoughWhite
import com.antigravity.pizzacooking.presentation.theme.CrustBrownDark
import com.antigravity.pizzacooking.presentation.theme.DarkCharcoal
import com.antigravity.pizzacooking.presentation.theme.GentleBlue
import com.antigravity.pizzacooking.presentation.theme.GoldenCrust
import com.antigravity.pizzacooking.presentation.theme.MagicViolet
import com.antigravity.pizzacooking.presentation.theme.PastelKitchenTile
import com.antigravity.pizzacooking.presentation.theme.PlayfulCyan
import com.antigravity.pizzacooking.presentation.theme.PolishedOakWood
import com.antigravity.pizzacooking.presentation.theme.SoftCoralAlert
import com.antigravity.pizzacooking.presentation.theme.SoundEffects
import com.antigravity.pizzacooking.presentation.theme.StarGoldGlory
import com.antigravity.pizzacooking.presentation.theme.WarmKitchenWood
import com.antigravity.pizzacooking.presentation.theme.WarmMarbleCream
import com.antigravity.pizzacooking.presentation.theme.WarmTomatoRed
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun KitchenScreen(
    viewModel: KitchenViewModel,
    onHomeClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Breathing pulse for interactive items
    val infiniteTransition = rememberInfiniteTransition(label = "GentlePulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScale"
    )

    ResponsiveKitchenCanvas(ambientBackgroundColor = WarmKitchenWood) { widthDp, heightDp ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // FULL SCREEN KITCHEN BACKGROUND
            FullKitchenBackground()

            // TOP HEADER CONTROLS (Home, Audio, Prompt, Skip, Next)
            TopBarNavigation(
                stepTitle = uiState.currentStepTitle,
                stepPrompt = uiState.currentStepPrompt,
                recipe = uiState.currentRecipe,
                difficultyTier = uiState.difficultyTier,
                gameMode = uiState.gameMode,
                isAudioMuted = uiState.isAudioMuted,
                onHomeClicked = onHomeClicked,
                onToggleAudio = { viewModel.toggleAudio() },
                onSwitchMode = { viewModel.switchMode(it) },
                onSkipClicked = { viewModel.skipStep() },
                onNextClicked = { viewModel.advanceStep() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .align(Alignment.TopCenter)
            )

            // MAIN INTERACTIVE STAGE (Center Pizza + Right Shelf)
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 76.dp, bottom = 64.dp, start = 24.dp, end = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // LEFT: Interactive Pizza Stage (Centered & Prominent)
                InteractivePizzaStage(
                    uiState = uiState,
                    pulseScale = pulseScale,
                    onDoughKnead = { viewModel.interactDough() },
                    onDoughRoll = { viewModel.interactRollDough() },
                    onSauceSwirl = { viewModel.interactSwirlSauce() },
                    onCheeseGrate = { viewModel.interactGrateCheese() },
                    onBakePizza = { viewModel.bakePizzaInOven() },
                    onSlicePizza = { viewModel.slicePizza() },
                    onBitePizza = { viewModel.takeBite() },
                    onDropTopping = { id, x, y -> viewModel.addToppingAtPosition(id, x, y) },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )

                Spacer(modifier = Modifier.width(16.dp))

                // RIGHT: Cartoon Ingredient Shelf (with 5 tabs in Creative Mode)
                RightIngredientPantry(
                    gameMode = uiState.gameMode,
                    activeCategory = uiState.activeCreativeCategory,
                    availableIngredients = uiState.availableIngredients,
                    selectedIngredientId = uiState.selectedIngredientId,
                    onCategorySelected = { viewModel.selectCreativeCategory(it) },
                    onIngredientSelected = { viewModel.selectIngredient(it) },
                    onClearToppings = { viewModel.clearToppings() },
                    placedToppingsCount = uiState.placedToppings.size,
                    currentStepIndex = uiState.currentStepIndex,
                    modifier = Modifier
                        .width(260.dp)
                        .fillMaxHeight()
                )
            }

            // BOTTOM PROGRESS BREADCRUMB (6 Visual Stages)
            BottomCookingStepper(
                currentStepIndex = uiState.currentStepIndex,
                onStageClick = { targetStep -> viewModel.jumpToStep(targetStep) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 10.dp)
            )

            // CELEBRATION STAR RATING MODAL
            if (uiState.isFinished) {
                VictoryCelebrationDialog(
                    stars = uiState.scoreStars ?: 3,
                    recipeName = uiState.currentRecipe?.name ?: "Delicious Pizza",
                    onPlayAgain = {
                        val recipe = uiState.currentRecipe ?: com.antigravity.pizzacooking.data.repository.RecipeRepository.getAllRecipes().first()
                        viewModel.initSession(recipe, uiState.gameMode)
                    },
                    onMainMenu = onHomeClicked
                )
            }
        }
    }
}

// =================================================================================================
// 1. FULL SCREEN KITCHEN ENVIRONMENT BACKGROUND
// =================================================================================================
@Composable
private fun FullKitchenBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Upper Wall: Pastel Mint Tiles
        drawRect(
            color = PastelKitchenTile,
            topLeft = Offset(0f, 0f),
            size = Size(w, h * 0.45f)
        )

        // Subway tile lines
        val tileSize = 48f
        val numRows = (h * 0.45f / tileSize).toInt() + 1
        val numCols = (w / tileSize).toInt() + 1

        for (row in 0..numRows) {
            val y = row * tileSize
            drawLine(
                color = Color.White.copy(alpha = 0.5f),
                start = Offset(0f, y),
                end = Offset(w, y),
                strokeWidth = 2f
            )
        }
        for (col in 0..numCols) {
            val x = col * tileSize
            drawLine(
                color = Color.White.copy(alpha = 0.5f),
                start = Offset(x, 0f),
                end = Offset(x, h * 0.45f),
                strokeWidth = 2f
            )
        }

        // Sunny Kitchen Window on Left
        val winLeft = w * 0.08f
        val winTop = h * 0.06f
        val winW = 120f
        val winH = 90f
        // Sky inside window
        drawRoundRect(
            color = Color(0xFF81D4FA),
            topLeft = Offset(winLeft, winTop),
            size = Size(winW, winH),
            cornerRadius = CornerRadius(12f, 12f)
        )
        // White cloud in window
        drawCircle(color = Color.White.copy(alpha = 0.85f), radius = 18f, center = Offset(winLeft + 45f, winTop + 35f))
        drawCircle(color = Color.White.copy(alpha = 0.85f), radius = 24f, center = Offset(winLeft + 65f, winTop + 40f))
        // Window Frame
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(winLeft, winTop),
            size = Size(winW, winH),
            cornerRadius = CornerRadius(12f, 12f),
            style = Stroke(width = 6f)
        )
        // Window Cross Panes
        drawLine(color = Color.White, start = Offset(winLeft + winW / 2f, winTop), end = Offset(winLeft + winW / 2f, winTop + winH), strokeWidth = 4f)
        drawLine(color = Color.White, start = Offset(winLeft, winTop + winH / 2f), end = Offset(winLeft + winW, winTop + winH / 2f), strokeWidth = 4f)

        // Little green herb pot on windowsill
        drawRect(color = Color(0xFFBCAAA4), topLeft = Offset(winLeft + 15f, winTop + winH - 12f), size = Size(26f, 18f))
        drawCircle(color = Color(0xFF4CAF50), radius = 12f, center = Offset(winLeft + 28f, winTop + winH - 16f))

        // Lower Kitchen Prep Counter: Warm Polished Wood
        drawRect(
            color = WarmKitchenWood,
            topLeft = Offset(0f, h * 0.45f),
            size = Size(w, h * 0.55f)
        )

        // Checkered table runner accent
        val ckSize = 16f
        val numCks = (w / ckSize).toInt() + 1
        for (i in 0..numCks) {
            val col = if (i % 2 == 0) WarmTomatoRed else CreamDoughWhite
            drawRect(
                color = col,
                topLeft = Offset(i * ckSize, h * 0.45f - ckSize),
                size = Size(ckSize, ckSize)
            )
        }

        // Countertop wooden bevel trim line
        drawRect(
            color = PolishedOakWood,
            topLeft = Offset(0f, h * 0.45f),
            size = Size(w, 16f)
        )
        drawLine(
            color = CrustBrownDark,
            start = Offset(0f, h * 0.45f + 16f),
            end = Offset(w, h * 0.45f + 16f),
            strokeWidth = 4f
        )
    }
}

// =================================================================================================
// 2. TOP BAR NAVIGATION CONTROLS
// =================================================================================================
@Composable
private fun TopBarNavigation(
    stepTitle: String,
    stepPrompt: String,
    recipe: com.antigravity.pizzacooking.data.models.Recipe?,
    difficultyTier: Int,
    gameMode: GameMode,
    isAudioMuted: Boolean,
    onHomeClicked: () -> Unit,
    onToggleAudio: () -> Unit,
    onSwitchMode: (GameMode) -> Unit,
    onSkipClicked: () -> Unit,
    onNextClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val localizedInfo = remember(recipe?.recipeId) {
        recipe?.let { RecipeMetadataRegistry.getInfo(it.recipeId) }
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Anchor: Home + Sound + Mode Pill
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Big Home Button
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .shadow(4.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(WarmTomatoRed)
                    .border(3.dp, CrustBrownDark, RoundedCornerShape(16.dp))
                    .clickable { onHomeClicked() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White, modifier = Modifier.size(28.dp))
            }

            // Sound Toggle
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .shadow(4.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isAudioMuted) SoftCoralAlert else GentleBlue)
                    .border(3.dp, CrustBrownDark, RoundedCornerShape(16.dp))
                    .clickable { onToggleAudio() },
                contentAlignment = Alignment.Center
            ) {
                Text(text = if (isAudioMuted) "🔇" else "🔊", fontSize = 22.sp)
            }

            // Mode Selector
            Box(
                modifier = Modifier
                    .height(52.dp)
                    .shadow(4.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (gameMode == GameMode.RECIPE_MODE) GoldenCrust else MagicViolet)
                    .border(3.dp, CrustBrownDark, RoundedCornerShape(16.dp))
                    .clickable {
                        val next = if (gameMode == GameMode.RECIPE_MODE) GameMode.CREATIVE_MODE else GameMode.RECIPE_MODE
                        onSwitchMode(next)
                    }
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (gameMode == GameMode.RECIPE_MODE) "📖 RECIPE" else "🎨 CREATIVE",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        // Center: Child-Friendly Chef Guidance Speech Bubble with Recipe & Difficulty Badge
        Box(
            modifier = Modifier
                .shadow(6.dp, RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .background(WarmMarbleCream)
                .border(3.dp, GoldenCrust, RoundedCornerShape(24.dp))
                .padding(horizontal = 18.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "👨‍🍳 ", fontSize = 20.sp)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Recipe & Difficulty Tag
                    val badgeStr = when (difficultyTier) {
                        1 -> "⭐ EASY"
                        2 -> "⭐⭐ MEDIUM"
                        else -> "⭐⭐⭐ HARD"
                    }
                    val recipeTitle = localizedInfo?.nameEn ?: recipe?.name ?: "DELICIOUS PIZZA"
                    Text(
                        text = "[$badgeStr] $recipeTitle".uppercase(),
                        color = WarmTomatoRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "${stepTitle}: $stepPrompt",
                        color = DarkCharcoal,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Right Anchor: SKIP & NEXT Buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // SKIP Button
            Box(
                modifier = Modifier
                    .height(52.dp)
                    .shadow(4.dp, RoundedCornerShape(18.dp))
                    .clip(RoundedCornerShape(18.dp))
                    .background(PlayfulCyan)
                    .border(3.dp, CrustBrownDark, RoundedCornerShape(18.dp))
                    .clickable { onSkipClicked() }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = ">> SKIP", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Black)
            }

            // NEXT / DONE Button
            Box(
                modifier = Modifier
                    .height(52.dp)
                    .shadow(4.dp, RoundedCornerShape(18.dp))
                    .clip(RoundedCornerShape(18.dp))
                    .background(BasilFreshGreen)
                    .border(3.dp, CrustBrownDark, RoundedCornerShape(18.dp))
                    .clickable { onNextClicked() }
                    .padding(horizontal = 18.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "NEXT >>", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}

// =================================================================================================
// 3. MAIN INTERACTIVE PIZZA STAGE
// =================================================================================================
@Composable
private fun InteractivePizzaStage(
    uiState: KitchenUiState,
    pulseScale: Float,
    onDoughKnead: () -> Unit,
    onDoughRoll: () -> Unit,
    onSauceSwirl: () -> Unit,
    onCheeseGrate: () -> Unit,
    onBakePizza: () -> Unit,
    onSlicePizza: () -> Unit,
    onBitePizza: () -> Unit,
    onDropTopping: (String, Float, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Wooden Prep Peel / Cutting Board (Strict 1:1 Aspect Ratio Circle)
        Box(
            modifier = Modifier
                .fillMaxHeight(0.96f)
                .aspectRatio(1f)
                .shadow(16.dp, CircleShape)
                .clip(CircleShape)
                .background(PolishedOakWood)
                .border(8.dp, CrustBrownDark, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Wood plank texture lines
            Canvas(modifier = Modifier.fillMaxSize()) {
                val r = size.width / 2f
                val c = Offset(r, r)
                drawCircle(color = Color.White.copy(alpha = 0.08f), radius = r * 0.92f)
                drawCircle(color = CrustBrownDark.copy(alpha = 0.25f), radius = r * 0.88f, style = Stroke(width = 6f))
            }

            // STAGE SPECIFIC PIZZA RENDERING
            when (uiState.currentStepIndex) {
                // STAGE 1: DOUGH KNEADING & ROLLING
                0, 1, 2, 4 -> {
                    DoughStageInteractive(
                        rollProgress = uiState.doughRollProgress,
                        pulseScale = pulseScale,
                        onPunch = onDoughKnead,
                        onRoll = onDoughRoll
                    )
                }

                // STAGE 2: SAUCE SPREADING
                3, 5 -> {
                    SauceStageInteractive(
                        coverage = uiState.sauceCoverage,
                        pulseScale = pulseScale,
                        onSwirl = onSauceSwirl
                    )
                }

                // STAGE 3: CHEESE GRATING
                6 -> {
                    CheeseStageInteractive(
                        cheeseGrated = uiState.cheeseGrated,
                        pulseScale = pulseScale,
                        onGrate = onCheeseGrate
                    )
                }

                // STAGE 4: TOPPING DECORATING
                7 -> {
                    ToppingStageInteractive(
                        uiState = uiState,
                        onDropTopping = onDropTopping
                    )
                }

                // STAGE 5: OVEN BAKING
                8 -> {
                    OvenBakingStageInteractive(
                        isBaked = uiState.isBaked,
                        difficultyTier = uiState.difficultyTier,
                        onBake = onBakePizza
                    )
                }

                // STAGE 6: SLICING & EATING
                else -> {
                    SlicingAndEatingStage(
                        slicesCount = uiState.slicesCount,
                        bitesCount = uiState.bitesCount,
                        placedToppings = uiState.placedToppings,
                        onSlice = onSlicePizza,
                        onBite = onBitePizza
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// STAGE 1: DOUGH KNEAD & ROLL
// -------------------------------------------------------------------------------------------------
@Composable
private fun DoughStageInteractive(
    rollProgress: Float,
    pulseScale: Float,
    onPunch: () -> Unit,
    onRoll: () -> Unit
) {
    val animatedDoughSize by animateFloatAsState(
        targetValue = 160f + 140f * rollProgress,
        animationSpec = tween(350, easing = FastOutSlowInEasing),
        label = "DoughSize"
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Dough Disc
        Box(
            modifier = Modifier
                .size(animatedDoughSize.dp)
                .scale(if (rollProgress < 0.2f) pulseScale else 1.0f)
                .shadow(12.dp, CircleShape)
                .clip(CircleShape)
                .background(CreamDoughWhite)
                .border(6.dp, GoldenCrust, CircleShape)
                .clickable {
                    if (rollProgress < 0.99f) onRoll() else onPunch()
                },
            contentAlignment = Alignment.Center
        ) {
            if (rollProgress < 0.8f) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🥖", fontSize = 32.sp)
                    Text(
                        text = "TAP or SWIPE\nto Roll Dough!",
                        color = CrustBrownDark,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Text(
                    text = "✨ Crust Ready! ✨",
                    color = BasilFreshGreen,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        // Animated Rolling Pin Floating Indicator
        if (rollProgress < 0.95f) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
                    .shadow(6.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(StarGoldGlory)
                    .border(2.dp, CrustBrownDark, RoundedCornerShape(16.dp))
                    .clickable { onRoll() }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "👆 Tap to Roll! (${(rollProgress * 100).roundToInt()}%)",
                    color = DarkCharcoal,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// STAGE 2: SAUCE SWIRLING
// -------------------------------------------------------------------------------------------------
@Composable
private fun SauceStageInteractive(
    coverage: Float,
    pulseScale: Float,
    onSwirl: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Rolled Crust Base
        Box(
            modifier = Modifier
                .size(300.dp)
                .shadow(12.dp, CircleShape)
                .clip(CircleShape)
                .background(CreamDoughWhite)
                .border(8.dp, GoldenCrust, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Expanding Sauce Puddle
            val sauceRadius = (260f * coverage.coerceAtLeast(0.15f))
            Box(
                modifier = Modifier
                    .size(sauceRadius.dp)
                    .clip(CircleShape)
                    .background(WarmTomatoRed)
                    .border(4.dp, Color(0xFFB71C1C), CircleShape)
                    .clickable { onSwirl() },
                contentAlignment = Alignment.Center
            ) {
                if (coverage < 0.8f) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🍅", fontSize = 32.sp)
                        Text(
                            text = "SWIRL SAUCE!\nTap here!",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // Helper Action Pill
        if (coverage < 0.9f) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
                    .shadow(6.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(WarmTomatoRed)
                    .border(2.dp, CrustBrownDark, RoundedCornerShape(16.dp))
                    .clickable { onSwirl() }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "🥄 Swirl Ladle! (${(coverage * 100).roundToInt()}%)",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// STAGE 3: CHEESE GRATING
// -------------------------------------------------------------------------------------------------
@Composable
private fun CheeseStageInteractive(
    cheeseGrated: Float,
    pulseScale: Float,
    onGrate: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Pizza with Sauce
        Box(
            modifier = Modifier
                .size(300.dp)
                .clip(CircleShape)
                .background(CreamDoughWhite)
                .border(8.dp, GoldenCrust, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Sauce
            Box(
                modifier = Modifier
                    .size(260.dp)
                    .clip(CircleShape)
                    .background(WarmTomatoRed)
            )

            // Cheese Layer
            val cheeseSize = (240f * cheeseGrated.coerceAtLeast(0.15f))
            Box(
                modifier = Modifier
                    .size(cheeseSize.dp)
                    .clip(CircleShape)
                    .background(CheeseYellow.copy(alpha = 0.9f))
                    .clickable { onGrate() },
                contentAlignment = Alignment.Center
            ) {
                if (cheeseGrated < 0.8f) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🧀", fontSize = 32.sp)
                        Text(
                            text = "GRATE CHEESE!\nTap to grate!",
                            color = CrustBrownDark,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // Grater Action Pill
        if (cheeseGrated < 0.9f) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
                    .shadow(6.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(StarGoldGlory)
                    .border(2.dp, CrustBrownDark, RoundedCornerShape(16.dp))
                    .clickable { onGrate() }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "🧀 Grate Cheese! (${(cheeseGrated * 100).roundToInt()}%)",
                    color = DarkCharcoal,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// STAGE 4: TOPPING DECORATING (INTERACTIVE DRAG & DROP + TAP)
// -------------------------------------------------------------------------------------------------
@Composable
private fun ToppingStageInteractive(
    uiState: KitchenUiState,
    onDropTopping: (String, Float, Float) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val pizzaSizePx = 300f

        // Pizza Base
        Box(
            modifier = Modifier
                .size(300.dp)
                .clip(CircleShape)
                .background(CreamDoughWhite)
                .border(8.dp, GoldenCrust, CircleShape)
                .pointerInput(uiState.selectedIngredientId) {
                    detectDragGestures { change, _ ->
                        val selected = uiState.selectedIngredientId
                        if (selected != null && selected.startsWith("ING-")) {
                            val normX = (change.position.x / size.width).coerceIn(0.15f, 0.85f)
                            val normY = (change.position.y / size.height).coerceIn(0.15f, 0.85f)
                            onDropTopping(selected, normX, normY)
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            // Sauce
            Box(modifier = Modifier.size(260.dp).clip(CircleShape).background(WarmTomatoRed))
            // Cheese
            Box(modifier = Modifier.size(240.dp).clip(CircleShape).background(CheeseYellow))

            // PLACED CARTOON TOPPINGS
            uiState.placedToppings.forEach { topping ->
                val offX = ((topping.positionX - 0.5f) * 200f).dp
                val offY = ((topping.positionY - 0.5f) * 200f).dp

                CartoonToppingItem(
                    ingredientId = topping.ingredientId,
                    size = 38.dp,
                    modifier = Modifier.offset(x = offX, y = offY)
                )
            }

            // Toddler Target Guides for Easy Mode (Level 1)
            if (uiState.difficultyTier == 1 && uiState.placedToppings.size < 4) {
                val guides = listOf(
                    Offset(-45f, -40f),
                    Offset(45f, -30f),
                    Offset(0f, 40f),
                    Offset(-40f, 30f)
                )
                guides.forEach { pos ->
                    Box(
                        modifier = Modifier
                            .offset(x = pos.x.dp, y = pos.y.dp)
                            .size(34.dp)
                            .border(2.dp, Color.White.copy(alpha = 0.85f), CircleShape)
                            .background(Color.White.copy(alpha = 0.25f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "✨", fontSize = 14.sp)
                    }
                }
            }

            if (uiState.placedToppings.isEmpty()) {
                Text(
                    text = "👇 Pick Toppings on the Right\n& tap or drag onto Pizza!",
                    color = CrustBrownDark,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// STAGE 5: OVEN BAKING STAGE (DIFFICULTY AWARE)
// -------------------------------------------------------------------------------------------------
@Composable
private fun OvenBakingStageInteractive(
    isBaked: Boolean,
    difficultyTier: Int,
    onBake: () -> Unit
) {
    var timerSeconds by remember { mutableIntStateOf(3) }
    var isBakingNow by remember { mutableStateOf(false) }

    // Heat Gauge for Hard Mode (Level 3)
    var heatGauge by remember { mutableFloatStateOf(0.1f) }
    var heatDirection by remember { mutableFloatStateOf(0.025f) }

    LaunchedEffect(isBakingNow, difficultyTier) {
        if (isBakingNow) {
            if (difficultyTier == 3) {
                while (isBakingNow) {
                    delay(35)
                    heatGauge += heatDirection
                    if (heatGauge >= 1.0f) {
                        heatGauge = 1.0f
                        heatDirection = -0.025f
                    } else if (heatGauge <= 0.05f) {
                        heatGauge = 0.05f
                        heatDirection = 0.025f
                    }
                }
            } else {
                while (timerSeconds > 0) {
                    delay(1000)
                    timerSeconds--
                }
                isBakingNow = false
                onBake()
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (!isBaked && !isBakingNow) {
            // Pizza Ready for Oven
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(280.dp)
                        .clip(CircleShape)
                        .background(CreamDoughWhite)
                        .border(8.dp, GoldenCrust, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier.size(240.dp).clip(CircleShape).background(WarmTomatoRed))
                    Box(modifier = Modifier.size(220.dp).clip(CircleShape).background(CheeseYellow))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .shadow(6.dp, RoundedCornerShape(18.dp))
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFFE65100))
                        .border(3.dp, DarkCharcoal, RoundedCornerShape(18.dp))
                        .clickable { isBakingNow = true }
                        .padding(horizontal = 24.dp, vertical = 10.dp)
                ) {
                    Text(text = "🔥 SLIDE INTO OVEN! 🔥", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Black)
                }
            }
        } else if (isBakingNow) {
            // Oven Chamber Simulation
            Box(
                modifier = Modifier
                    .size(320.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color(0xFF263238))
                    .border(8.dp, Color(0xFFD84315), RoundedCornerShape(32.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🔥 OVEN BAKING... 🔥", color = Color(0xFFFFB300), fontSize = 18.sp, fontWeight = FontWeight.Black)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (difficultyTier == 3) {
                        // Hard mode: Heat Gauge Needle
                        Text(text = "Stop needle in the PERFECT GOLD zone! ⭐", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))

                        // Gauge Bar
                        Box(
                            modifier = Modifier
                                .width(220.dp)
                                .height(26.dp)
                                .clip(RoundedCornerShape(13.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFF29B6F6), Color(0xFFFFD54F), Color(0xFFD32F2F))
                                    )
                                )
                                .border(2.dp, Color.White, RoundedCornerShape(13.dp))
                        ) {
                            // Perfect Golden Star Zone
                            Text(
                                text = "⭐ PERFECT ⭐",
                                color = DarkCharcoal,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.align(Alignment.Center)
                            )
                            // Needle cursor
                            val cursorX = (220f * heatGauge - 8f).coerceAtLeast(0f).dp
                            Box(
                                modifier = Modifier
                                    .offset(x = cursorX)
                                    .width(16.dp)
                                    .height(26.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color.White)
                                    .border(2.dp, DarkCharcoal, RoundedCornerShape(4.dp))
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Action Button to stop baking in gold zone
                        Box(
                            modifier = Modifier
                                .shadow(6.dp, RoundedCornerShape(16.dp))
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF4CAF50))
                                .border(3.dp, Color.White, RoundedCornerShape(16.dp))
                                .clickable {
                                    isBakingNow = false
                                    if (heatGauge in 0.35f..0.75f) {
                                        SoundEffects.playFanfare()
                                    } else {
                                        SoundEffects.playDing()
                                    }
                                    onBake()
                                }
                                .padding(horizontal = 20.dp, vertical = 8.dp)
                        ) {
                            Text(text = "🔥 TAKE PIZZA OUT NOW! 🔥", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Black)
                        }
                    } else {
                        // Easy & Medium mode: Cheerful countdown
                        Text(text = "$timerSeconds", color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.ExtraBold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Sizzling & Melting!", color = Color(0xFFFFCC80), fontSize = 13.sp)
                    }
                }
            }
        } else {
            // Baked Golden Pizza!
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(280.dp)
                        .clip(CircleShape)
                        .background(GoldenCrust)
                        .border(8.dp, CrustBrownDark, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier.size(240.dp).clip(CircleShape).background(WarmTomatoRed))
                    Box(modifier = Modifier.size(220.dp).clip(CircleShape).background(Color(0xFFFFD54F)))
                    Text(text = "✨ BAKED GOLDEN! ✨", color = CrustBrownDark, fontSize = 16.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// STAGE 6: SLICING & EATING
// -------------------------------------------------------------------------------------------------
@Composable
private fun SlicingAndEatingStage(
    slicesCount: Int,
    bitesCount: Int,
    placedToppings: List<com.antigravity.pizzacooking.domain.statemachine.PlacedTopping>,
    onSlice: () -> Unit,
    onBite: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(300.dp)
                .clip(CircleShape)
                .background(GoldenCrust)
                .border(8.dp, CrustBrownDark, CircleShape)
                .clickable {
                    if (slicesCount == 0) onSlice() else onBite()
                },
            contentAlignment = Alignment.Center
        ) {
            // Baked Sauce & Melted Cheese
            Box(modifier = Modifier.size(260.dp).clip(CircleShape).background(WarmTomatoRed))
            Box(modifier = Modifier.size(230.dp).clip(CircleShape).background(Color(0xFFFFD54F)))

            // Slicing Dotted Guide Lines
            Canvas(modifier = Modifier.fillMaxSize()) {
                val c = Offset(size.width / 2f, size.height / 2f)
                val r = size.width / 2f
                val dash = PathEffect.dashPathEffect(floatArrayOf(14f, 8f), 0f)

                // 6 slice lines (0, 60, 120 degrees)
                for (angleDeg in listOf(0f, 60f, 120f)) {
                    val rad = Math.toRadians(angleDeg.toDouble())
                    val dx = (r * Math.cos(rad)).toFloat()
                    val dy = (r * Math.sin(rad)).toFloat()
                    drawLine(
                        color = CrustBrownDark,
                        start = Offset(c.x - dx, c.y - dy),
                        end = Offset(c.x + dx, c.y + dy),
                        strokeWidth = 4f,
                        pathEffect = dash
                    )
                }
            }

            // Placed Toppings
            placedToppings.forEach { topping ->
                val offX = ((topping.positionX - 0.5f) * 180f).dp
                val offY = ((topping.positionY - 0.5f) * 180f).dp

                CartoonToppingItem(
                    ingredientId = topping.ingredientId,
                    size = 32.dp,
                    modifier = Modifier.offset(x = offX, y = offY)
                )
            }

            // Visual Bite Marks
            if (bitesCount > 0) {
                for (b in 1..bitesCount) {
                    val angle = (b * 60.0) * Math.PI / 180.0
                    val bx = (100.0 * Math.cos(angle)).dp
                    val by = (100.0 * Math.sin(angle)).dp

                    Box(
                        modifier = Modifier
                            .offset(x = bx, y = by)
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(WarmKitchenWood)
                            .border(2.dp, CrustBrownDark, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "CHOMP!", color = CreamDoughWhite, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Center Action Prompt
            if (slicesCount == 0) {
                Box(
                    modifier = Modifier
                        .shadow(6.dp, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(text = "🔪 TAP TO SLICE!", color = WarmTomatoRed, fontSize = 14.sp, fontWeight = FontWeight.Black)
                }
            } else if (bitesCount < 5) {
                Box(
                    modifier = Modifier
                        .shadow(6.dp, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(text = "😋 TAP TO EAT! ($bitesCount/5)", color = BasilFreshGreen, fontSize = 14.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

// =================================================================================================
// 4. RIGHT CARTOON INGREDIENT SHELF & PANTRY
// =================================================================================================
@Composable
private fun RightIngredientPantry(
    gameMode: GameMode,
    activeCategory: String,
    availableIngredients: List<Ingredient>,
    selectedIngredientId: String?,
    onCategorySelected: (String) -> Unit,
    onIngredientSelected: (String) -> Unit,
    onClearToppings: () -> Unit,
    placedToppingsCount: Int,
    currentStepIndex: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .shadow(8.dp, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(WarmMarbleCream)
            .border(3.dp, PolishedOakWood, RoundedCornerShape(24.dp))
            .padding(10.dp)
    ) {
        // Shelf Header & Topping Count Badge
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (gameMode == GameMode.RECIPE_MODE) "RECIPE SHELF" else "PANTRY",
                color = CrustBrownDark,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black
            )

            if (currentStepIndex == 7) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Count: $placedToppingsCount", color = DarkCharcoal, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(SoftCoralAlert)
                            .clickable { onClearToppings() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.White, modifier = Modifier.size(14.dp))
                    }
                }
            }
        }

        // 5 Cartoon Category Tabs (Creative Mode)
        if (gameMode == GameMode.CREATIVE_MODE) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PantryCategoryTab("🥕", "VEGETABLE", activeCategory == "VEGETABLE") { onCategorySelected("VEGETABLE") }
                PantryCategoryTab("🥩", "MEAT", activeCategory == "MEAT") { onCategorySelected("MEAT") }
                PantryCategoryTab("🍓", "FRUIT", activeCategory == "FRUIT") { onCategorySelected("FRUIT") }
                PantryCategoryTab("🍬", "SWEET", activeCategory == "SWEET") { onCategorySelected("SWEET") }
                PantryCategoryTab("⭐", "FANTASY", activeCategory == "FANTASY") { onCategorySelected("FANTASY") }
            }
        }

        // Scrollable Ingredient Ceramic Bowls
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(availableIngredients) { ingredient ->
                val isSelected = ingredient.ingredientId == selectedIngredientId

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(if (isSelected) 6.dp else 2.dp, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) StarGoldGlory else Color.White)
                        .border(
                            width = if (isSelected) 3.dp else 1.5.dp,
                            color = if (isSelected) GoldenCrust else BrushedSteelGray,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { onIngredientSelected(ingredient.ingredientId) }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Cute Cartoon Topping Icon
                    CartoonToppingItem(
                        ingredientId = ingredient.ingredientId,
                        size = 36.dp
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = ingredient.name,
                            color = DarkCharcoal,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }

                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = DarkCharcoal,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PantryCategoryTab(
    emoji: String,
    categoryKey: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .shadow(if (isSelected) 4.dp else 1.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) StarGoldGlory else BrushedSteelGray)
            .border(2.dp, if (isSelected) GoldenCrust else Color.Transparent, RoundedCornerShape(12.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = emoji, fontSize = 20.sp)
    }
}

// =================================================================================================
// 5. BOTTOM COOKING STEPPER (6 Intuitive Stages)
// =================================================================================================
@Composable
private fun BottomCookingStepper(
    currentStepIndex: Int,
    onStageClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val stages = listOf(
        Pair("🥖 Dough", 0),
        Pair("🍅 Sauce", 5),
        Pair("🧀 Cheese", 6),
        Pair("🍄 Toppings", 7),
        Pair("🔥 Oven", 8),
        Pair("🍕 Eat!", 9)
    )

    Box(
        modifier = modifier
            .shadow(6.dp, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(DarkCharcoal.copy(alpha = 0.90f))
            .padding(horizontal = 16.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            stages.forEach { (label, stepTarget) ->
                val isCompleted = currentStepIndex > stepTarget
                val isActive = currentStepIndex == stepTarget ||
                        (stepTarget == 0 && currentStepIndex in 0..4)

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isActive) WarmTomatoRed else if (isCompleted) StarGoldGlory else Color.White.copy(alpha = 0.2f))
                        .border(2.dp, if (isActive) StarGoldGlory else Color.Transparent, RoundedCornerShape(14.dp))
                        .clickable { onStageClick(stepTarget) }
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = if (isActive || !isCompleted) Color.White else DarkCharcoal,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}

// =================================================================================================
// 6. VICTORY CELEBRATION MODAL
// =================================================================================================
@Composable
private fun VictoryCelebrationDialog(
    stars: Int,
    recipeName: String,
    onPlayAgain: () -> Unit,
    onMainMenu: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.65f))
            .clickable { /* Block touches */ },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(380.dp)
                .shadow(20.dp, RoundedCornerShape(32.dp))
                .clip(RoundedCornerShape(32.dp))
                .background(WarmMarbleCream)
                .border(6.dp, GoldenCrust, RoundedCornerShape(32.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🎉 YUMMY PIZZA! 🎉",
                color = WarmTomatoRed,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black
            )

            Text(
                text = recipeName,
                color = DarkCharcoal,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 3 BIG SMILING GOLDEN STARS
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                for (i in 1..3) {
                    val earned = i <= stars
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .shadow(if (earned) 8.dp else 2.dp, CircleShape)
                            .clip(CircleShape)
                            .background(if (earned) StarGoldGlory else BrushedSteelGray)
                            .border(3.dp, DarkCharcoal, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = if (earned) "⭐" else "⚪", fontSize = 32.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .shadow(4.dp, RoundedCornerShape(18.dp))
                        .clip(RoundedCornerShape(18.dp))
                        .background(BasilFreshGreen)
                        .border(3.dp, DarkCharcoal, RoundedCornerShape(18.dp))
                        .clickable { onPlayAgain() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "PLAY AGAIN", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Black)
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .shadow(4.dp, RoundedCornerShape(18.dp))
                        .clip(RoundedCornerShape(18.dp))
                        .background(WarmTomatoRed)
                        .border(3.dp, DarkCharcoal, RoundedCornerShape(18.dp))
                        .clickable { onMainMenu() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "MAIN MENU", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}
