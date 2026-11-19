package com.antigravity.pizzacooking.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.antigravity.pizzacooking.data.models.Recipe
import com.antigravity.pizzacooking.data.repository.RecipeRepository
import com.antigravity.pizzacooking.presentation.components.CartoonPizzaThumbnail
import com.antigravity.pizzacooking.presentation.components.CartoonToppingItem
import com.antigravity.pizzacooking.presentation.components.ResponsiveKitchenCanvas
import com.antigravity.pizzacooking.presentation.theme.BasilFreshGreen
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
import com.antigravity.pizzacooking.presentation.theme.WarmTomatoRed

/**
 * Filter Categories for 20-recipe menu
 */
enum class RecipeFilterCategory(val label: String, val icon: String) {
    ALL("All (20)", "🌟"),
    EASY("Easy (6)", "⭐"),
    MEDIUM("Medium (9)", "⭐⭐"),
    HARD("Hard (5)", "⭐⭐⭐"),
    NO_BAKE("No-Bake (2)", "🍓");

    val labelVi: String get() = label
}

/**
 * Localized Presentation Metadata for 20 Recipes
 */
data class RecipeLocalizedInfo(
    val recipeId: String,
    val nameEn: String,
    val difficultyTier: Int, // 1 = Easy, 2 = Medium, 3 = Hard
    val difficultyLabel: String,
    val badgeColor: Color,
    val descriptionEn: String,
    val nameVi: String = nameEn,
    val difficultyLabelVi: String = difficultyLabel,
    val descriptionVi: String = descriptionEn
)

object RecipeMetadataRegistry {
    private val metadataMap = mapOf(
        "R-01" to RecipeLocalizedInfo("R-01", "Classic Margherita", 1, "⭐ EASY", Color(0xFF4CAF50), "Fresh tomato sauce, melty mozzarella cheese, and fragrant basil."),
        "R-02" to RecipeLocalizedInfo("R-02", "Pepperoni Supreme", 1, "⭐ EASY", Color(0xFF4CAF50), "Crispy savory pepperoni slices, green bell peppers, and black olives."),
        "R-03" to RecipeLocalizedInfo("R-03", "Summer Watermelon", 1, "⭐ EASY", Color(0xFF4CAF50), "Juicy sweet watermelon crust, rainbow coconut sauce, no-bake treat!"),
        "R-04" to RecipeLocalizedInfo("R-04", "Sweet Candy & Marshmallow", 1, "⭐ EASY", Color(0xFF4CAF50), "Dark chocolate crust, chewy gummy bears, and fluffy marshmallows."),
        "R-05" to RecipeLocalizedInfo("R-05", "Four Cheese Formaggi", 1, "⭐ EASY", Color(0xFF4CAF50), "A rich, gooey blend of four cheeses with delicious cheesy pulls!"),
        "R-06" to RecipeLocalizedInfo("R-06", "Tropical Hawaiian Breeze", 1, "⭐ EASY", Color(0xFF4CAF50), "Savory ham slices paired with sweet and tangy golden pineapples."),
        "R-07" to RecipeLocalizedInfo("R-07", "Ahoy Pirate Treasure", 2, "⭐⭐ MEDIUM", Color(0xFFFF9800), "Search for chocolate gold coins on a mysterious high-seas pizza!"),
        "R-08" to RecipeLocalizedInfo("R-08", "Spooky Halloween Vampire", 2, "⭐⭐ MEDIUM", Color(0xFFFF9800), "Mystic black crust, crimson red sauce, and playful vampire bats."),
        "R-09" to RecipeLocalizedInfo("R-09", "Magical Rainbow Unicorn", 2, "⭐⭐ MEDIUM", Color(0xFFFF9800), "Pastel strawberry sauce, sparkling stars, and unicorn horn magic!"),
        "R-10" to RecipeLocalizedInfo("R-10", "Winter Wonderland Christmas", 2, "⭐⭐ MEDIUM", Color(0xFFFF9800), "Snowy white mozzarella, green Christmas trees, and red ornaments."),
        "R-11" to RecipeLocalizedInfo("R-11", "Gourmet BBQ Chicken", 2, "⭐⭐ MEDIUM", Color(0xFFFF9800), "Tender grilled chicken drizzled with rich, smoky barbecue sauce."),
        "R-12" to RecipeLocalizedInfo("R-12", "Japanese Takoyaki", 3, "⭐⭐⭐ HARD", Color(0xFFE91E63), "Japanese style with tender octopus chunks, savory mayo, and seaweed."),
        "R-13" to RecipeLocalizedInfo("R-13", "Fiesta Mexican Taco", 2, "⭐⭐ MEDIUM", Color(0xFFFF9800), "Seasoned ground beef, crispy corn tortilla chips, and zesty salsa."),
        "R-14" to RecipeLocalizedInfo("R-14", "Sunny Breakfast Egg & Bacon", 2, "⭐⭐ MEDIUM", Color(0xFFFF9800), "Golden sunny-side-up eggs and crispy savory bacon for breakfast."),
        "R-15" to RecipeLocalizedInfo("R-15", "Wild Forest Mushroom", 2, "⭐⭐ MEDIUM", Color(0xFFFF9800), "Fresh forest mushrooms infused with the aroma of truffle oil."),
        "R-16" to RecipeLocalizedInfo("R-16", "Circus Fun Carnival", 3, "⭐⭐⭐ HARD", Color(0xFFE91E63), "Sweet crunchy popcorn and rainbow candy confetti just like a circus!"),
        "R-17" to RecipeLocalizedInfo("R-17", "Berry Berry Fruit Tart", 2, "⭐⭐ MEDIUM", Color(0xFFFF9800), "Soft sweet tart crust, sweet cream, loaded with chilled strawberries!"),
        "R-18" to RecipeLocalizedInfo("R-18", "Volcano Spicy Pepper", 3, "⭐⭐⭐ HARD", Color(0xFFE91E63), "Smoking fiery peppers bursting with flavor for brave little chefs!"),
        "R-19" to RecipeLocalizedInfo("R-19", "Garden Veggie Bloom", 3, "⭐⭐⭐ HARD", Color(0xFFE91E63), "Arranged with zucchini ribbons, cherry tomatoes, and veggie petals."),
        "R-20" to RecipeLocalizedInfo("R-20", "Royal Galaxy Cosmic", 3, "⭐⭐⭐ HARD", Color(0xFFE91E63), "Deep cosmic purple crust sprinkled with golden stardust and glitter!")
    )

    fun getInfo(recipeId: String): RecipeLocalizedInfo {
        return metadataMap[recipeId] ?: RecipeLocalizedInfo(
            recipeId = recipeId,
            nameEn = "Delicious Pizza",
            difficultyTier = 1,
            difficultyLabel = "⭐ EASY",
            badgeColor = Color(0xFF4CAF50),
            descriptionEn = "A gourmet master pizza made by a talented little chef!"
        )
    }
}

@Composable
fun RecipeSelectionScreen(
    onRecipeSelected: (Recipe) -> Unit,
    onCreativeModeSelected: () -> Unit,
    onBackClicked: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf(RecipeFilterCategory.ALL) }
    var isAudioMuted by remember { mutableStateOf(SoundEffects.isMuted) }

    val allRecipes = remember { RecipeRepository.getAllRecipes() }

    val filteredRecipes = remember(selectedFilter, allRecipes) {
        when (selectedFilter) {
            RecipeFilterCategory.ALL -> allRecipes
            RecipeFilterCategory.EASY -> allRecipes.filter {
                RecipeMetadataRegistry.getInfo(it.recipeId).difficultyTier == 1
            }
            RecipeFilterCategory.MEDIUM -> allRecipes.filter {
                RecipeMetadataRegistry.getInfo(it.recipeId).difficultyTier == 2
            }
            RecipeFilterCategory.HARD -> allRecipes.filter {
                RecipeMetadataRegistry.getInfo(it.recipeId).difficultyTier == 3
            }
            RecipeFilterCategory.NO_BAKE -> allRecipes.filter { it.isNoBake }
        }
    }

    // Pulse animation for interactive buttons
    val infiniteTransition = rememberInfiniteTransition(label = "ButtonPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScale"
    )

    ResponsiveKitchenCanvas(ambientBackgroundColor = WarmKitchenWood) { widthDp, heightDp ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Full Pizzeria Wall & Kitchen Surface Background
            FullMenuBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                // 1. TOP HEADER BAR
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Back Button
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .shadow(6.dp, CircleShape)
                            .clip(CircleShape)
                            .background(WarmTomatoRed)
                            .border(3.dp, CreamDoughWhite, CircleShape)
                            .clickable {
                                SoundEffects.playPop()
                                onBackClicked()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = CreamDoughWhite,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Main Title Banner
                    Box(
                        modifier = Modifier
                            .shadow(8.dp, RoundedCornerShape(20.dp))
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(WarmTomatoRed, GoldenCrust)
                                )
                            )
                            .border(3.dp, CreamDoughWhite, RoundedCornerShape(20.dp))
                            .padding(horizontal = 24.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "🍕 CHOOSE YOUR PIZZA — 20 MASTER RECIPES 🍕",
                                color = CreamDoughWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    // Sound Toggle Button
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .shadow(6.dp, CircleShape)
                            .clip(CircleShape)
                            .background(if (isAudioMuted) DarkCharcoal else GentleBlue)
                            .border(3.dp, CreamDoughWhite, CircleShape)
                            .clickable {
                                SoundEffects.isMuted = !SoundEffects.isMuted
                                isAudioMuted = SoundEffects.isMuted
                                if (!isAudioMuted) SoundEffects.playPop()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isAudioMuted) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
                            contentDescription = "Sound",
                            tint = CreamDoughWhite,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 2. DIFFICULTY & CATEGORY FILTER TABS
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RecipeFilterCategory.values().forEach { category ->
                        val isSelected = selectedFilter == category
                        val tabBg = if (isSelected) GoldenCrust else CreamDoughWhite.copy(alpha = 0.85f)
                        val textColor = if (isSelected) CreamDoughWhite else DarkCharcoal
                        val borderColor = if (isSelected) CrustBrownDark else Color.Transparent

                        Box(
                            modifier = Modifier
                                .padding(horizontal = 6.dp)
                                .shadow(if (isSelected) 6.dp else 2.dp, RoundedCornerShape(16.dp))
                                .clip(RoundedCornerShape(16.dp))
                                .background(tabBg)
                                .border(2.dp, borderColor, RoundedCornerShape(16.dp))
                                .clickable {
                                    SoundEffects.playPop()
                                    selectedFilter = category
                                }
                                .padding(horizontal = 14.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${category.icon} ${category.labelVi}",
                                    color = textColor,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 3. RECIPES HORIZONTAL CAROUSEL
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    LazyRow(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(filteredRecipes, key = { it.recipeId }) { recipe ->
                            RecipeMenuCard(
                                recipe = recipe,
                                pulseScale = pulseScale,
                                onSelect = {
                                    SoundEffects.playFanfare()
                                    onRecipeSelected(recipe)
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 4. BOTTOM BAR: FREEFORM CREATIVE MODE SHORTCUT
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Helper Prompt Speech
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White.copy(alpha = 0.85f))
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "💡 Pick your favorite pizza recipe to start cooking!",
                            color = DarkCharcoal,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Big Creative Mode Rainbow Button
                    Box(
                        modifier = Modifier
                            .scale(pulseScale)
                            .shadow(6.dp, RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(MagicViolet, PlayfulCyan)
                                )
                            )
                            .border(3.dp, CreamDoughWhite, RoundedCornerShape(16.dp))
                            .clickable {
                                SoundEffects.playPop()
                                onCreativeModeSelected()
                            }
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🎨 CREATIVE FREE PLAY",
                            color = CreamDoughWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}

/**
 * Individual Recipe Card with Cartoon Art, Difficulty Badge, and Cook Button
 */
@Composable
private fun RecipeMenuCard(
    recipe: Recipe,
    pulseScale: Float,
    onSelect: () -> Unit
) {
    val info = remember(recipe.recipeId) { RecipeMetadataRegistry.getInfo(recipe.recipeId) }

    Box(
        modifier = Modifier
            .width(230.dp)
            .fillMaxHeight(0.96f)
            .shadow(10.dp, RoundedCornerShape(22.dp))
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White)
            .border(3.dp, CrustBrownDark.copy(alpha = 0.25f), RoundedCornerShape(22.dp))
            .clickable { onSelect() }
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Row: Difficulty Badge & No-Bake tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Difficulty Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(info.badgeColor)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = info.difficultyLabelVi,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Special tag (No-Bake or Recipe ID)
                if (recipe.isNoBake) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFE91E63))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "🍓 NO-BAKE",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Text(
                        text = recipe.recipeId,
                        color = DarkCharcoal.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Appetizing 2D Cartoon Pizza Thumbnail
            Box(
                modifier = Modifier
                    .size(92.dp)
                    .padding(vertical = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                CartoonPizzaThumbnail(recipeId = recipe.recipeId, size = 92.dp)
            }

            // Recipe Name
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = info.nameEn,
                    color = DarkCharcoal,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Short Description
            Text(
                text = info.descriptionEn,
                color = DarkCharcoal.copy(alpha = 0.75f),
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                lineHeight = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            // Key Toppings Preview Row
            Row(
                modifier = Modifier.padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                recipe.toppingIds.take(3).forEach { toppingId ->
                    CartoonToppingItem(ingredientId = toppingId, size = 22.dp)
                }
            }

            // Big Green "COOK NOW! 🍕" Button (with 3D bevel)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
                    .shadow(4.dp, RoundedCornerShape(14.dp))
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF66BB6A), Color(0xFF388E3C))
                        )
                    )
                    .border(2.dp, CreamDoughWhite, RoundedCornerShape(14.dp))
                    .clickable { onSelect() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "COOK NOW! 🍕",
                    color = CreamDoughWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

/**
 * 2D Cartoon Italian Pizzeria Wall & Counter Background for Menu
 */
@Composable
private fun FullMenuBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // 1. Upper Wall: Warm Tuscan Cream Brick
        drawRect(
            color = Color(0xFFFFF8E1),
            topLeft = Offset(0f, 0f),
            size = androidx.compose.ui.geometry.Size(w, h * 0.52f)
        )

        // Brick grout lines
        val tileW = 60f
        val tileH = 28f
        val numRows = (h * 0.52f / tileH).toInt() + 1
        val numCols = (w / tileW).toInt() + 1
        for (r in 0..numRows) {
            val y = r * tileH
            drawLine(
                color = Color.White.copy(alpha = 0.6f),
                start = Offset(0f, y),
                end = Offset(w, y),
                strokeWidth = 2f
            )
        }

        // Hanging Garlic & Pepper Garland on Top Right
        drawCircle(color = Color.White, radius = 12f, center = Offset(w * 0.88f, 28f))
        drawCircle(color = Color.White, radius = 10f, center = Offset(w * 0.88f, 44f))
        drawCircle(color = Color(0xFFE53935), radius = 8f, center = Offset(w * 0.91f, 34f))
        drawCircle(color = Color(0xFFE53935), radius = 8f, center = Offset(w * 0.91f, 48f))

        // Hanging Copper Pan on Top Left
        drawCircle(color = Color(0xFFB87333), radius = 18f, center = Offset(w * 0.12f, 32f))
        drawCircle(color = Color(0xFFCD7F32), radius = 15f, center = Offset(w * 0.12f, 32f))
        drawLine(color = Color(0xFF8D5524), start = Offset(w * 0.12f, 14f), end = Offset(w * 0.12f, 0f), strokeWidth = 4f)

        // 2. Kitchen Wooden Prep Counter Base
        drawRect(
            color = WarmKitchenWood,
            topLeft = Offset(0f, h * 0.52f),
            size = androidx.compose.ui.geometry.Size(w, h * 0.48f)
        )

        // 3. Italian Red-and-White Checkered Tablecloth Border
        val checkSize = 18f
        val numChecks = (w / checkSize).toInt() + 1
        for (c in 0..numChecks) {
            val checkColor = if (c % 2 == 0) WarmTomatoRed else CreamDoughWhite
            drawRect(
                color = checkColor,
                topLeft = Offset(c * checkSize, h * 0.52f - checkSize),
                size = androidx.compose.ui.geometry.Size(checkSize, checkSize)
            )
        }

        // 4. Polished Counter Edge Lip
        drawRect(
            color = PolishedOakWood,
            topLeft = Offset(0f, h * 0.52f),
            size = androidx.compose.ui.geometry.Size(w, 14f)
        )
        drawLine(
            color = Color.White.copy(alpha = 0.35f),
            start = Offset(0f, h * 0.52f + 2f),
            end = Offset(w, h * 0.52f + 2f),
            strokeWidth = 3f
        )
    }
}
