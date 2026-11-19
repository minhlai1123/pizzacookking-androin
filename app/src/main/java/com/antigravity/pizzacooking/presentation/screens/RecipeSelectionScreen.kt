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
enum class RecipeFilterCategory(val labelVi: String, val icon: String) {
    ALL("Tất Cả (20)", "🌟"),
    EASY("Dễ (6)", "⭐"),
    MEDIUM("Vừa (9)", "⭐⭐"),
    HARD("Thử Thách (5)", "⭐⭐⭐"),
    NO_BAKE("Không Cần Nướng (2)", "🍓")
}

/**
 * Localized Presentation Metadata for 20 Recipes
 */
data class RecipeLocalizedInfo(
    val recipeId: String,
    val nameVi: String,
    val nameEn: String,
    val difficultyTier: Int, // 1 = Easy, 2 = Medium, 3 = Hard
    val difficultyLabelVi: String,
    val badgeColor: Color,
    val descriptionVi: String
)

object RecipeMetadataRegistry {
    private val metadataMap = mapOf(
        "R-01" to RecipeLocalizedInfo("R-01", "Margherita Cổ Điển", "Classic Margherita", 1, "⭐ DỄ", Color(0xFF4CAF50), "Xốt cà chua tươi, phô mai dẻo dai và lá húng quế thơm phức."),
        "R-02" to RecipeLocalizedInfo("R-02", "Xúc Xích Pepperoni", "Pepperoni Supreme", 1, "⭐ DỄ", Color(0xFF4CAF50), "Xúc xích cay giòn, ớt chuông xanh và ô-liu đen thơm ngon."),
        "R-03" to RecipeLocalizedInfo("R-03", "Dưa Hấu Mát Lạnh", "Summer Watermelon", 1, "⭐ DỄ", Color(0xFF4CAF50), "Đế dưa hấu mát ngọt, xốt dừa cầu vồng, tráng miệng không cần nướng!"),
        "R-04" to RecipeLocalizedInfo("R-04", "Kẹo Dẻo & Kẹo Bông", "Sweet Candy & Marshmallow", 1, "⭐ DỄ", Color(0xFF4CAF50), "Bánh quy sô-cô-la đen, kẹo gấu dẻo và kẹo bông xốp ngọt ngào."),
        "R-05" to RecipeLocalizedInfo("R-05", "4 Vị Phô Mai Béo", "Four Cheese Formaggi", 1, "⭐ DỄ", Color(0xFF4CAF50), "Hòa quyện 4 loại phô mai thơm lừng, kéo sợi cực đã!"),
        "R-06" to RecipeLocalizedInfo("R-06", "Dứa Hawaii Nhiệt Đới", "Tropical Hawaiian Breeze", 1, "⭐ DỄ", Color(0xFF4CAF50), "Thịt nguội hồng hào cùng những lát dứa vàng tươi chua ngọt."),
        "R-07" to RecipeLocalizedInfo("R-07", "Kho Báu Cướp Biển", "Ahoy Pirate Treasure", 2, "⭐⭐ VỪA", Color(0xFFFF9800), "Truy tìm đồng tiền vàng sô-cô-la trên chiếc pizza cướp biển kỳ bí!"),
        "R-08" to RecipeLocalizedInfo("R-08", "Halloween Dracula", "Spooky Halloween Vampire", 2, "⭐⭐ VỪA", Color(0xFFFF9800), "Đế đen huyền bí, xốt đỏ ma mị và những chú dơi ngộ nghĩnh."),
        "R-09" to RecipeLocalizedInfo("R-09", "Kỳ Lân Cầu Vồng", "Magical Rainbow Unicorn", 2, "⭐⭐ VỪA", Color(0xFFFF9800), "Xốt dâu tây pastel, ngôi sao lấp lánh và sừng kỳ lân diệu kỳ!"),
        "R-10" to RecipeLocalizedInfo("R-10", "Giáng Sinh Tuyết Trắng", "Winter Wonderland Christmas", 2, "⭐⭐ VỪA", Color(0xFFFF9800), "Phô mai tuyết trắng muốt, cây thông xanh và quả châu đỏ rực."),
        "R-11" to RecipeLocalizedInfo("R-11", "Gà Nướng Sốt BBQ", "Gourmet BBQ Chicken", 2, "⭐⭐ VỪA", Color(0xFFFF9800), "Thịt gà nướng mềm ngậy cùng dòng xốt BBQ đậm đà thơm lừng."),
        "R-12" to RecipeLocalizedInfo("R-12", "Bạch Tuộc Takoyaki", "Japanese Takoyaki", 3, "⭐⭐⭐ THỬ THÁCH", Color(0xFFE91E63), "Phong cách Nhật Bản với bạch tuộc giòn sần sật và rong biển thơm."),
        "R-13" to RecipeLocalizedInfo("R-13", "Bò Taco Mexico", "Fiesta Mexican Taco", 2, "⭐⭐ VỪA", Color(0xFFFF9800), "Thịt bò băm đậm vị cay nhẹ, bánh ngô giòn tan và xốt salsa."),
        "R-14" to RecipeLocalizedInfo("R-14", "Bữa Sáng Trứng Ốp La", "Sunny Breakfast Egg & Bacon", 2, "⭐⭐ VỪA", Color(0xFFFF9800), "Trứng lòng đào vàng ươm cùng thịt xông khói giòn rụm bổ dưỡng."),
        "R-15" to RecipeLocalizedInfo("R-15", "Nấm Rừng & Truffle", "Wild Forest Mushroom", 2, "⭐⭐ VỪA", Color(0xFFFF9800), "Nấm rừng tươi ngon kết hợp hương dầu nấm Truffle sang trọng."),
        "R-16" to RecipeLocalizedInfo("R-16", "Lễ Hội Xiếc Rực Rỡ", "Circus Fun Carnival", 3, "⭐⭐⭐ THỬ THÁCH", Color(0xFFE91E63), "Bỏng ngô ngọt giòn, kẹo rực rỡ sắc màu như đoàn xiếc vui nhộn!"),
        "R-17" to RecipeLocalizedInfo("R-17", "Tart Dâu Trái Cây", "Berry Berry Fruit Tart", 2, "⭐⭐ VỪA", Color(0xFFFF9800), "Đế bánh tart mềm, kem ngọt, ngập tràn dâu tây tươi mát lạnh!"),
        "R-18" to RecipeLocalizedInfo("R-18", "Núi Lửa Cay Bùng Nổ", "Volcano Spicy Pepper", 3, "⭐⭐⭐ THỬ THÁCH", Color(0xFFE91E63), "Ớt chuông lửa bốc khói cay nồng dành cho các đầu bếp dũng cảm!"),
        "R-19" to RecipeLocalizedInfo("R-19", "Vườn Hoa Rau Củ", "Garden Veggie Bloom", 3, "⭐⭐⭐ THỬ THÁCH", Color(0xFFE91E63), "Xếp bí ngòi, cà chua bi và ớt chuông thành đóa hoa tuyệt đẹp."),
        "R-20" to RecipeLocalizedInfo("R-20", "Thiên Hà Vũ Trụ", "Royal Galaxy Cosmic", 3, "⭐⭐⭐ THỬ THÁCH", Color(0xFFE91E63), "Đế thiên hà màu tím thẫm, rắc bụi sao vàng và bụi vũ trụ lấp lánh!")
    )

    fun getInfo(recipeId: String): RecipeLocalizedInfo {
        return metadataMap[recipeId] ?: RecipeLocalizedInfo(
            recipeId = recipeId,
            nameVi = "Pizza Thơm Ngon",
            nameEn = "Delicious Pizza",
            difficultyTier = 1,
            difficultyLabelVi = "⭐ DỄ",
            badgeColor = Color(0xFF4CAF50),
            descriptionVi = "Món pizza hảo hạng dành cho bé trổ tài đầu bếp nhí!"
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
                            contentDescription = "Quay lại",
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
                                text = "🍕 CHỌN MÓN PIZZA CỦA BÉ — 20 MÓN ĐẶC SẮC 🍕",
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
                            contentDescription = "Âm thanh",
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
                            text = "💡 Bé hãy chọn một chiếc pizza yêu thích để bắt đầu nấu nhé!",
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
                            text = "🎨 CHẾ ĐỘ SÁNG TẠO TỰ DO",
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
                            text = "🍓 KHÔNG NƯỚNG",
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

            // Recipe Name in Vietnamese (bold) & English (subtitle)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = info.nameVi,
                    color = DarkCharcoal,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = info.nameEn,
                    color = CrustBrownDark.copy(alpha = 0.7f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Short Description
            Text(
                text = info.descriptionVi,
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

            // Big Green "NẤU NGAY 🍕" Button (with 3D bevel)
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
                    text = "NẤU NGAY! 🍕",
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
