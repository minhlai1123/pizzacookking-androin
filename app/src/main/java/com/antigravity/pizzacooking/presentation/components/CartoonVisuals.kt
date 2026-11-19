package com.antigravity.pizzacooking.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 2D Cartoon Food & Kitchen Art Renderer.
 * Produces vibrant, child-appealing, cel-shaded graphics with bold outlines.
 */
@Composable
fun CartoonToppingItem(
    ingredientId: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val w = this.size.width
            val h = this.size.height
            val center = Offset(w / 2f, h / 2f)

            when {
                // Sauce Bowl / Jar (ING-011 .. ING-020)
                ingredientId.startsWith("ING-01") -> {
                    val sauceColor = when (ingredientId) {
                        "ING-012" -> Color(0xFFFFF9C4) // Alfredo White
                        "ING-013" -> Color(0xFFFF80AB) // Pink Glaze
                        "ING-014" -> Color(0xFF5D4037) // Chocolate Sauce
                        "ING-015" -> Color(0xFFFF7043) // Spicy Salsa
                        "ING-016" -> Color(0xFF8D6E63) // BBQ Sauce
                        "ING-017" -> Color(0xFF66BB6A) // Pesto Green
                        else -> Color(0xFFD32F2F)      // Classic Tomato Sauce
                    }
                    // Bowl base
                    drawArc(
                        color = Color(0xFFECEFF1),
                        startAngle = 0f,
                        sweepAngle = 180f,
                        useCenter = true,
                        topLeft = Offset(w * 0.15f, h * 0.25f),
                        size = Size(w * 0.7f, h * 0.65f)
                    )
                    drawArc(
                        color = Color(0xFFB0BEC5),
                        startAngle = 0f,
                        sweepAngle = 180f,
                        useCenter = true,
                        topLeft = Offset(w * 0.15f, h * 0.25f),
                        size = Size(w * 0.7f, h * 0.65f),
                        style = Stroke(width = w * 0.05f)
                    )
                    // Rich sauce inside bowl
                    drawOval(
                        color = sauceColor,
                        topLeft = Offset(w * 0.2f, h * 0.35f),
                        size = Size(w * 0.6f, h * 0.3f)
                    )
                    // Sauce shine
                    drawCircle(color = Color.White.copy(alpha = 0.5f), radius = w * 0.07f, center = Offset(w * 0.38f, h * 0.45f))
                }

                // Cheese Block / Shreds (ING-021 .. ING-029)
                ingredientId.startsWith("ING-02") -> {
                    val cheeseColor = if (ingredientId == "ING-022") Color(0xFFFFB300) else Color(0xFFFFF176)
                    val wedgePath = Path().apply {
                        moveTo(w * 0.15f, h * 0.75f)
                        lineTo(w * 0.85f, h * 0.75f)
                        lineTo(w * 0.5f, h * 0.2f)
                        close()
                    }
                    drawPath(path = wedgePath, color = Color(0xFFF57F17), style = Stroke(width = w * 0.06f))
                    drawPath(path = wedgePath, color = cheeseColor, style = Fill)
                    // Cheese holes
                    drawCircle(color = Color(0xFFFBC02D), radius = w * 0.08f, center = Offset(w * 0.42f, h * 0.55f))
                    drawCircle(color = Color(0xFFFBC02D), radius = w * 0.05f, center = Offset(w * 0.62f, h * 0.62f))
                    drawCircle(color = Color.White.copy(alpha = 0.6f), radius = w * 0.06f, center = Offset(w * 0.48f, h * 0.35f))
                }

                // Pepperoni (ING-040)
                ingredientId.contains("040") || ingredientId.contains("043") -> {
                    // Outer crust edge
                    drawCircle(color = Color(0xFFB71C1C), radius = w * 0.48f, center = center)
                    // Pepperoni meat
                    drawCircle(color = Color(0xFFD32F2F), radius = w * 0.44f, center = center)
                    // Savory fat specks
                    drawCircle(color = Color(0xFFFF8A80), radius = w * 0.08f, center = Offset(w * 0.35f, h * 0.35f))
                    drawCircle(color = Color(0xFFFF8A80), radius = w * 0.06f, center = Offset(w * 0.65f, h * 0.40f))
                    drawCircle(color = Color(0xFFFF8A80), radius = w * 0.07f, center = Offset(w * 0.45f, h * 0.65f))
                    // Subtle shine arc
                    drawArc(
                        color = Color.White.copy(alpha = 0.4f),
                        startAngle = 200f,
                        sweepAngle = 60f,
                        useCenter = false,
                        topLeft = Offset(w * 0.15f, h * 0.15f),
                        size = Size(w * 0.7f, h * 0.7f),
                        style = Stroke(width = w * 0.08f)
                    )
                }

                // Fresh Basil Leaf (ING-034)
                ingredientId.contains("034") || ingredientId.contains("035") -> {
                    val leafPath = Path().apply {
                        moveTo(w * 0.5f, h * 0.1f)
                        cubicTo(w * 0.9f, h * 0.3f, w * 0.85f, h * 0.85f, w * 0.5f, h * 0.9f)
                        cubicTo(w * 0.15f, h * 0.85f, w * 0.1f, h * 0.3f, w * 0.5f, h * 0.1f)
                        close()
                    }
                    drawPath(path = leafPath, color = Color(0xFF1B5E20), style = Stroke(width = w * 0.06f))
                    drawPath(path = leafPath, color = Color(0xFF43A047), style = Fill)
                    // Leaf vein
                    drawLine(
                        color = Color(0xFF2E7D32),
                        start = Offset(w * 0.5f, h * 0.15f),
                        end = Offset(w * 0.5f, h * 0.85f),
                        strokeWidth = w * 0.04f
                    )
                    // Leaf shine
                    drawCircle(color = Color.White.copy(alpha = 0.45f), radius = w * 0.1f, center = Offset(w * 0.4f, h * 0.35f))
                }

                // Button Mushroom (ING-031)
                ingredientId.contains("031") -> {
                    // Mushroom stem
                    drawRoundRect(
                        color = Color(0xFFE0E0E0),
                        topLeft = Offset(w * 0.38f, h * 0.45f),
                        size = Size(w * 0.24f, h * 0.45f),
                        cornerRadius = CornerRadius(w * 0.08f, w * 0.08f)
                    )
                    drawRoundRect(
                        color = Color(0xFF9E9E9E),
                        topLeft = Offset(w * 0.38f, h * 0.45f),
                        size = Size(w * 0.24f, h * 0.45f),
                        cornerRadius = CornerRadius(w * 0.08f, w * 0.08f),
                        style = Stroke(width = w * 0.05f)
                    )
                    // Mushroom cap
                    val capPath = Path().apply {
                        moveTo(w * 0.15f, h * 0.55f)
                        cubicTo(w * 0.1f, h * 0.1f, w * 0.9f, h * 0.1f, w * 0.85f, h * 0.55f)
                        close()
                    }
                    drawPath(path = capPath, color = Color(0xFF6D4C41), style = Stroke(width = w * 0.06f))
                    drawPath(path = capPath, color = Color(0xFFF5F5F5), style = Fill)
                    // Cap gill shadow
                    drawLine(color = Color(0xFFBDBDBD), start = Offset(w * 0.2f, h * 0.52f), end = Offset(w * 0.8f, h * 0.52f), strokeWidth = w * 0.04f)
                }

                // Black Olive Ring (ING-033)
                ingredientId.contains("033") -> {
                    drawCircle(color = Color(0xFF212121), radius = w * 0.46f, center = center)
                    drawCircle(color = Color(0xFF424242), radius = w * 0.40f, center = center)
                    // Center hole
                    drawCircle(color = Color(0xFFD7CCC8), radius = w * 0.18f, center = center)
                    // Glossy shine
                    drawCircle(color = Color.White.copy(alpha = 0.5f), radius = w * 0.08f, center = Offset(w * 0.35f, h * 0.32f))
                }

                // Bell Pepper Ring (ING-030, ING-036)
                ingredientId.contains("030") || ingredientId.contains("036") || ingredientId.contains("032") -> {
                    val pepperColor = if (ingredientId.contains("030")) Color(0xFF2E7D32) else Color(0xFFD84315)
                    val lightPepper = if (ingredientId.contains("030")) Color(0xFF4CAF50) else Color(0xFFFF5722)
                    drawArc(
                        color = pepperColor,
                        startAngle = 30f,
                        sweepAngle = 280f,
                        useCenter = false,
                        topLeft = Offset(w * 0.12f, h * 0.12f),
                        size = Size(w * 0.76f, h * 0.76f),
                        style = Stroke(width = w * 0.22f)
                    )
                    drawArc(
                        color = lightPepper,
                        startAngle = 40f,
                        sweepAngle = 260f,
                        useCenter = false,
                        topLeft = Offset(w * 0.14f, h * 0.14f),
                        size = Size(w * 0.72f, h * 0.72f),
                        style = Stroke(width = w * 0.14f)
                    )
                }

                // Sweet Pineapple / Golden Corn (ING-039, ING-053)
                ingredientId.contains("039") || ingredientId.contains("053") || ingredientId.contains("037") -> {
                    val chunkPath = Path().apply {
                        moveTo(w * 0.2f, h * 0.3f)
                        lineTo(w * 0.8f, h * 0.2f)
                        lineTo(w * 0.7f, h * 0.8f)
                        lineTo(w * 0.25f, h * 0.75f)
                        close()
                    }
                    drawPath(path = chunkPath, color = Color(0xFFF57F17), style = Stroke(width = w * 0.06f))
                    drawPath(path = chunkPath, color = Color(0xFFFFD54F), style = Fill)
                    drawCircle(color = Color.White.copy(alpha = 0.6f), radius = w * 0.09f, center = Offset(w * 0.4f, h * 0.4f))
                }

                // Pink Bacon / Ham (ING-041, ING-042, ING-044)
                ingredientId.contains("041") || ingredientId.contains("042") || ingredientId.contains("044") -> {
                    drawRoundRect(
                        color = Color(0xFFC2185B),
                        topLeft = Offset(w * 0.12f, h * 0.25f),
                        size = Size(w * 0.76f, h * 0.5f),
                        cornerRadius = CornerRadius(w * 0.1f, w * 0.1f)
                    )
                    drawRoundRect(
                        color = Color(0xFFF06292),
                        topLeft = Offset(w * 0.15f, h * 0.28f),
                        size = Size(w * 0.7f, h * 0.44f),
                        cornerRadius = CornerRadius(w * 0.08f, w * 0.08f)
                    )
                    // White savory fat stripe
                    drawLine(
                        color = Color.White.copy(alpha = 0.8f),
                        start = Offset(w * 0.2f, h * 0.5f),
                        end = Offset(w * 0.8f, h * 0.5f),
                        strokeWidth = w * 0.08f
                    )
                }

                // Shrimp (ING-046)
                ingredientId.contains("046") -> {
                    drawArc(
                        color = Color(0xFFFF7043),
                        startAngle = 45f,
                        sweepAngle = 260f,
                        useCenter = false,
                        topLeft = Offset(w * 0.15f, h * 0.15f),
                        size = Size(w * 0.7f, h * 0.7f),
                        style = Stroke(width = w * 0.24f)
                    )
                    drawCircle(color = Color(0xFFD84315), radius = w * 0.14f, center = Offset(w * 0.75f, h * 0.45f))
                    drawCircle(color = Color.White, radius = w * 0.05f, center = Offset(w * 0.76f, h * 0.42f))
                }

                // Fruit / Strawberry (ING-050)
                ingredientId.contains("050") -> {
                    val berryPath = Path().apply {
                        moveTo(w * 0.5f, h * 0.88f)
                        cubicTo(w * 0.1f, h * 0.6f, w * 0.2f, h * 0.25f, w * 0.5f, h * 0.25f)
                        cubicTo(w * 0.8f, h * 0.25f, w * 0.9f, h * 0.6f, w * 0.5f, h * 0.88f)
                        close()
                    }
                    drawPath(path = berryPath, color = Color(0xFFD32F2F), style = Fill)
                    // Green stem leaves
                    drawCircle(color = Color(0xFF388E3C), radius = w * 0.12f, center = Offset(w * 0.5f, h * 0.2f))
                    // Yellow seeds
                    drawCircle(color = Color(0xFFFFF59D), radius = w * 0.04f, center = Offset(w * 0.4f, h * 0.5f))
                    drawCircle(color = Color(0xFFFFF59D), radius = w * 0.04f, center = Offset(w * 0.6f, h * 0.6f))
                }

                // Sweet Candy / Fantasy Star (ING-060..122)
                else -> {
                    // Star shape
                    val starPath = Path().apply {
                        val numPoints = 5
                        val outerR = w * 0.45f
                        val innerR = w * 0.22f
                        for (i in 0 until (numPoints * 2)) {
                            val r = if (i % 2 == 0) outerR else innerR
                            val angle = i * Math.PI / numPoints - Math.PI / 2.0
                            val px = (center.x + r * Math.cos(angle)).toFloat()
                            val py = (center.y + r * Math.sin(angle)).toFloat()
                            if (i == 0) moveTo(px, py) else lineTo(px, py)
                        }
                        close()
                    }
                    drawPath(path = starPath, color = Color(0xFFF57F17), style = Stroke(width = w * 0.06f))
                    drawPath(path = starPath, color = Color(0xFFFFD54F), style = Fill)
                    drawCircle(color = Color.White.copy(alpha = 0.6f), radius = w * 0.1f, center = center)
                }
            }
        }
    }
}

/**
 * 2D Cartoon Pizza Thumbnail Renderer for 20 master recipes.
 * Renders appetizing, unique, colorful cartoon pizza art for each recipe.
 */
@Composable
fun CartoonPizzaThumbnail(
    recipeId: String,
    modifier: Modifier = Modifier,
    size: Dp = 80.dp
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val w = this.size.width
            val h = this.size.height
            val center = Offset(w / 2f, h / 2f)
            val outerRadius = w * 0.46f

            // 1. BASE CRUST & RIM
            when (recipeId) {
                // Watermelon: Green rind
                "R-03" -> {
                    drawCircle(color = Color(0xFF2E7D32), radius = outerRadius, center = center)
                    drawCircle(color = Color(0xFF81C784), radius = outerRadius * 0.90f, center = center)
                    drawCircle(color = Color(0xFFE53935), radius = outerRadius * 0.82f, center = center)
                }
                // Chocolate Cookie Crust
                "R-04" -> {
                    drawCircle(color = Color(0xFF3E2723), radius = outerRadius, center = center)
                    drawCircle(color = Color(0xFF4E342E), radius = outerRadius * 0.90f, center = center)
                    drawCircle(color = Color(0xFF5D4037), radius = outerRadius * 0.82f, center = center)
                }
                // Spooky Vampire Dark Crust
                "R-08" -> {
                    drawCircle(color = Color(0xFF212121), radius = outerRadius, center = center)
                    drawCircle(color = Color(0xFF311B92), radius = outerRadius * 0.90f, center = center)
                    drawCircle(color = Color(0xFFB71C1C), radius = outerRadius * 0.82f, center = center)
                }
                // Rainbow Unicorn Crust
                "R-09" -> {
                    drawCircle(color = Color(0xFFBA68C8), radius = outerRadius, center = center)
                    drawCircle(color = Color(0xFF80DEEA), radius = outerRadius * 0.90f, center = center)
                    drawCircle(color = Color(0xFFF8BBD0), radius = outerRadius * 0.82f, center = center)
                }
                // Galaxy Cosmic Crust
                "R-20" -> {
                    drawCircle(color = Color(0xFF1A237E), radius = outerRadius, center = center)
                    drawCircle(color = Color(0xFF311B92), radius = outerRadius * 0.90f, center = center)
                    drawCircle(color = Color(0xFF0D47A1), radius = outerRadius * 0.82f, center = center)
                }
                // Volcano Fire Crust
                "R-18" -> {
                    drawCircle(color = Color(0xFFBF360C), radius = outerRadius, center = center)
                    drawCircle(color = Color(0xFFE64A19), radius = outerRadius * 0.90f, center = center)
                    drawCircle(color = Color(0xFFFF5722), radius = outerRadius * 0.82f, center = center)
                }
                // Golden Standard Crust
                else -> {
                    drawCircle(color = Color(0xFFBCAAA4), radius = outerRadius, center = center)
                    drawCircle(color = Color(0xFFD7CCC8), radius = outerRadius * 0.92f, center = center)
                    drawCircle(color = Color(0xFFF4A261), radius = outerRadius * 0.88f, center = center)
                    // Golden cheese surface
                    drawCircle(color = Color(0xFFFFD54F), radius = outerRadius * 0.78f, center = center)
                }
            }

            // 2. RECIPE SPECIFIC TOPPINGS & ACCENTS
            when (recipeId) {
                // R-01 Classic Margherita: Tomato red pools, mozzarella blobs, green basil leaves
                "R-01" -> {
                    drawCircle(color = Color(0xFFE53935), radius = outerRadius * 0.75f, center = center)
                    // Mozzarella pools
                    drawCircle(color = Color(0xFFFFFDE7), radius = w * 0.12f, center = Offset(w * 0.38f, h * 0.40f))
                    drawCircle(color = Color(0xFFFFFDE7), radius = w * 0.11f, center = Offset(w * 0.62f, h * 0.45f))
                    drawCircle(color = Color(0xFFFFFDE7), radius = w * 0.10f, center = Offset(w * 0.48f, h * 0.65f))
                    // Green basil
                    drawCircle(color = Color(0xFF2E7D32), radius = w * 0.07f, center = Offset(w * 0.50f, h * 0.35f))
                    drawCircle(color = Color(0xFF388E3C), radius = w * 0.06f, center = Offset(w * 0.36f, h * 0.58f))
                    drawCircle(color = Color(0xFF388E3C), radius = w * 0.06f, center = Offset(w * 0.64f, h * 0.60f))
                }

                // R-02 Pepperoni Supreme: Red pepperoni slices with green pepper and black olives
                "R-02" -> {
                    val pPos = listOf(
                        Offset(w * 0.35f, h * 0.35f),
                        Offset(w * 0.65f, h * 0.35f),
                        Offset(w * 0.50f, h * 0.52f),
                        Offset(w * 0.34f, h * 0.65f),
                        Offset(w * 0.66f, h * 0.65f)
                    )
                    pPos.forEach { pos ->
                        drawCircle(color = Color(0xFFD32F2F), radius = w * 0.10f, center = pos)
                        drawCircle(color = Color(0xFFFF8A80), radius = w * 0.03f, center = pos + Offset(-2f, -2f))
                    }
                    // Bell pepper slivers
                    drawCircle(color = Color(0xFF2E7D32), radius = w * 0.05f, center = Offset(w * 0.50f, h * 0.34f))
                    drawCircle(color = Color(0xFF212121), radius = w * 0.04f, center = Offset(w * 0.48f, h * 0.70f))
                }

                // R-03 Summer Watermelon: Black watermelon teardrop seeds
                "R-03" -> {
                    val seeds = listOf(
                        Offset(w * 0.38f, h * 0.38f),
                        Offset(w * 0.62f, h * 0.38f),
                        Offset(w * 0.50f, h * 0.50f),
                        Offset(w * 0.35f, h * 0.60f),
                        Offset(w * 0.65f, h * 0.60f)
                    )
                    seeds.forEach { pos ->
                        drawOval(color = Color(0xFF212121), topLeft = pos - Offset(4f, 6f), size = Size(8f, 12f))
                    }
                    // Mint leaf
                    drawCircle(color = Color(0xFF00E676), radius = w * 0.06f, center = Offset(w * 0.5f, h * 0.32f))
                }

                // R-04 Sweet Candy & Marshmallow: White marshmallows and gummy candies
                "R-04" -> {
                    // White fluffy marshmallows
                    drawRoundRect(color = Color.White, topLeft = Offset(w * 0.35f, h * 0.35f), size = Size(w * 0.14f, h * 0.14f), cornerRadius = CornerRadius(4f, 4f))
                    drawRoundRect(color = Color.White, topLeft = Offset(w * 0.55f, h * 0.50f), size = Size(w * 0.14f, h * 0.14f), cornerRadius = CornerRadius(4f, 4f))
                    // Gummy bears
                    drawCircle(color = Color(0xFFFF4081), radius = w * 0.06f, center = Offset(w * 0.58f, h * 0.36f))
                    drawCircle(color = Color(0xFF00E5FF), radius = w * 0.06f, center = Offset(w * 0.38f, h * 0.58f))
                    drawCircle(color = Color(0xFFFFD600), radius = w * 0.06f, center = Offset(w * 0.48f, h * 0.46f))
                }

                // R-05 Four Cheese Formaggi: Swirls of cheeses and herbs
                "R-05" -> {
                    drawCircle(color = Color(0xFFFFE082), radius = outerRadius * 0.70f, center = center)
                    drawCircle(color = Color(0xFFFFF9C4), radius = w * 0.15f, center = Offset(w * 0.42f, h * 0.45f))
                    drawCircle(color = Color(0xFFFFB74D), radius = w * 0.12f, center = Offset(w * 0.58f, h * 0.52f))
                    // Oregano herbs
                    for (i in 0..6) {
                        val hx = w * (0.35f + (i % 3) * 0.15f)
                        val hy = h * (0.35f + (i / 3) * 0.15f)
                        drawCircle(color = Color(0xFF33691E), radius = 3f, center = Offset(hx, hy))
                    }
                }

                // R-06 Tropical Hawaiian Breeze: Pink ham and yellow pineapple wedges
                "R-06" -> {
                    // Ham squares
                    drawRoundRect(color = Color(0xFFF48FB1), topLeft = Offset(w * 0.32f, h * 0.35f), size = Size(w * 0.14f, h * 0.12f), cornerRadius = CornerRadius(4f, 4f))
                    drawRoundRect(color = Color(0xFFF48FB1), topLeft = Offset(w * 0.55f, h * 0.55f), size = Size(w * 0.14f, h * 0.12f), cornerRadius = CornerRadius(4f, 4f))
                    // Pineapple chunks
                    drawCircle(color = Color(0xFFFFD54F), radius = w * 0.08f, center = Offset(w * 0.58f, h * 0.36f))
                    drawCircle(color = Color(0xFFFFD54F), radius = w * 0.08f, center = Offset(w * 0.38f, h * 0.55f))
                    drawCircle(color = Color(0xFFFFD54F), radius = w * 0.07f, center = Offset(w * 0.48f, h * 0.45f))
                }

                // R-07 Ahoy Pirate Treasure: Gold chocolate coins & skull mark
                "R-07" -> {
                    drawCircle(color = Color(0xFFFFD700), radius = w * 0.09f, center = Offset(w * 0.38f, h * 0.40f))
                    drawCircle(color = Color(0xFFFFD700), radius = w * 0.09f, center = Offset(w * 0.62f, h * 0.42f))
                    drawCircle(color = Color(0xFFFFD700), radius = w * 0.09f, center = Offset(w * 0.50f, h * 0.62f))
                    // Skull crossbones / treasure badge in center
                    drawCircle(color = Color.White, radius = w * 0.06f, center = center)
                    drawCircle(color = Color(0xFF212121), radius = 3f, center = center + Offset(-3f, -2f))
                    drawCircle(color = Color(0xFF212121), radius = 3f, center = center + Offset(3f, -2f))
                }

                // R-08 Spooky Halloween Vampire: Black bats and red vampire sauce
                "R-08" -> {
                    // Black bat silhouettes
                    drawOval(color = Color(0xFF212121), topLeft = Offset(w * 0.32f, h * 0.35f), size = Size(w * 0.18f, h * 0.10f))
                    drawOval(color = Color(0xFF212121), topLeft = Offset(w * 0.52f, h * 0.52f), size = Size(w * 0.18f, h * 0.10f))
                    // Orange pumpkin eyes
                    drawCircle(color = Color(0xFFFF6D00), radius = w * 0.04f, center = Offset(w * 0.50f, h * 0.38f))
                }

                // R-09 Magical Rainbow Unicorn: Rainbow stars and pastel sparkles
                "R-09" -> {
                    drawCircle(color = Color(0xFFFFD54F), radius = w * 0.07f, center = Offset(w * 0.38f, h * 0.40f))
                    drawCircle(color = Color(0xFF00E5FF), radius = w * 0.06f, center = Offset(w * 0.62f, h * 0.42f))
                    drawCircle(color = Color(0xFFFF4081), radius = w * 0.07f, center = Offset(w * 0.48f, h * 0.60f))
                    // Unicorn sparkle
                    drawCircle(color = Color.White, radius = 5f, center = center)
                }

                // R-10 Winter Wonderland Christmas: Green pine trees and red berries
                "R-10" -> {
                    drawCircle(color = Color(0xFFECEFF1), radius = outerRadius * 0.75f, center = center)
                    // Pine tree greens
                    drawCircle(color = Color(0xFF2E7D32), radius = w * 0.08f, center = Offset(w * 0.40f, h * 0.42f))
                    drawCircle(color = Color(0xFF2E7D32), radius = w * 0.08f, center = Offset(w * 0.60f, h * 0.42f))
                    // Red ornaments
                    drawCircle(color = Color(0xFFD50000), radius = w * 0.04f, center = Offset(w * 0.50f, h * 0.35f))
                    drawCircle(color = Color(0xFFD50000), radius = w * 0.04f, center = Offset(w * 0.50f, h * 0.62f))
                }

                // R-11 Gourmet BBQ Chicken: Brown BBQ drizzle and chicken cubes
                "R-11" -> {
                    // BBQ swirl
                    drawArc(color = Color(0xFF4E342E), startAngle = 30f, sweepAngle = 200f, useCenter = false, topLeft = Offset(w * 0.25f, h * 0.25f), size = Size(w * 0.5f, h * 0.5f), style = Stroke(width = 6f))
                    // Chicken cubes
                    drawRoundRect(color = Color(0xFFFFE082), topLeft = Offset(w * 0.38f, h * 0.40f), size = Size(w * 0.10f, h * 0.10f), cornerRadius = CornerRadius(2f, 2f))
                    drawRoundRect(color = Color(0xFFFFE082), topLeft = Offset(w * 0.55f, h * 0.48f), size = Size(w * 0.10f, h * 0.10f), cornerRadius = CornerRadius(2f, 2f))
                    // Purple onions
                    drawCircle(color = Color(0xFF7B1FA2), radius = w * 0.05f, center = Offset(w * 0.48f, h * 0.35f))
                }

                // R-12 Japanese Takoyaki: Takoyaki octopus balls and seaweed specks
                "R-12" -> {
                    // Brown takoyaki balls
                    drawCircle(color = Color(0xFF8D6E63), radius = w * 0.10f, center = Offset(w * 0.38f, h * 0.42f))
                    drawCircle(color = Color(0xFF8D6E63), radius = w * 0.10f, center = Offset(w * 0.62f, h * 0.42f))
                    drawCircle(color = Color(0xFF8D6E63), radius = w * 0.10f, center = Offset(w * 0.50f, h * 0.60f))
                    // White mayo & brown sauce drizzle
                    drawLine(color = Color(0xFFFFF9C4), start = Offset(w * 0.3f, h * 0.35f), end = Offset(w * 0.7f, h * 0.65f), strokeWidth = 4f)
                    // Green seaweed flakes
                    drawCircle(color = Color(0xFF1B5E20), radius = 3f, center = Offset(w * 0.45f, h * 0.50f))
                }

                // R-13 Fiesta Mexican Taco: Ground beef, salsa and jalapenos
                "R-13" -> {
                    drawCircle(color = Color(0xFF5D4037), radius = w * 0.06f, center = Offset(w * 0.38f, h * 0.38f))
                    drawCircle(color = Color(0xFF5D4037), radius = w * 0.06f, center = Offset(w * 0.62f, h * 0.38f))
                    drawCircle(color = Color(0xFFE53935), radius = w * 0.05f, center = Offset(w * 0.50f, h * 0.45f))
                    // Jalapeno ring
                    drawCircle(color = Color(0xFF2E7D32), radius = w * 0.06f, center = Offset(w * 0.42f, h * 0.58f))
                }

                // R-14 Sunny Breakfast Egg & Bacon: Bacon strips and egg with sunny yellow yolk
                "R-14" -> {
                    // Bacon strip
                    drawRoundRect(color = Color(0xFFC2185B), topLeft = Offset(w * 0.28f, h * 0.58f), size = Size(w * 0.45f, h * 0.10f), cornerRadius = CornerRadius(4f, 4f))
                    // Sunny fried egg white
                    drawCircle(color = Color.White, radius = w * 0.16f, center = Offset(w * 0.50f, h * 0.42f))
                    // Bright egg yolk
                    drawCircle(color = Color(0xFFFFB300), radius = w * 0.09f, center = Offset(w * 0.50f, h * 0.42f))
                }

                // R-15 Wild Forest Mushroom Truffle: Sliced forest mushrooms
                "R-15" -> {
                    val mPos = listOf(Offset(w * 0.38f, h * 0.40f), Offset(w * 0.62f, h * 0.42f), Offset(w * 0.50f, h * 0.60f))
                    mPos.forEach { pos ->
                        drawCircle(color = Color(0xFF6D4C41), radius = w * 0.08f, center = pos)
                        drawCircle(color = Color(0xFFD7CCC8), radius = w * 0.04f, center = pos)
                    }
                }

                // R-16 Circus Fun Carnival: Multicolored candy popcorn
                "R-16" -> {
                    val candies = listOf(Color(0xFFFF1744), Color(0xFFFFEA00), Color(0xFF00E676), Color(0xFF2979FF), Color(0xFFAA00FF))
                    candies.forEachIndexed { i, col ->
                        val angle = i * 2.0 * Math.PI / candies.size
                        val r = w * 0.20f
                        val cx = (w * 0.5f + r * Math.cos(angle)).toFloat()
                        val cy = (h * 0.5f + r * Math.sin(angle)).toFloat()
                        drawCircle(color = col, radius = w * 0.06f, center = Offset(cx, cy))
                    }
                }

                // R-17 Berry Berry Fruit Tart: Strawberries & blueberries
                "R-17" -> {
                    drawCircle(color = Color(0xFFFFFDE7), radius = outerRadius * 0.75f, center = center)
                    // Strawberries
                    drawCircle(color = Color(0xFFD50000), radius = w * 0.09f, center = Offset(w * 0.38f, h * 0.42f))
                    drawCircle(color = Color(0xFFD50000), radius = w * 0.09f, center = Offset(w * 0.62f, h * 0.42f))
                    // Blueberries
                    drawCircle(color = Color(0xFF1A237E), radius = w * 0.06f, center = Offset(w * 0.50f, h * 0.35f))
                    drawCircle(color = Color(0xFF1A237E), radius = w * 0.06f, center = Offset(w * 0.50f, h * 0.60f))
                }

                // R-18 Volcano Spicy Pepper Blast: Flaming red chili peppers
                "R-18" -> {
                    drawCircle(color = Color(0xFFD50000), radius = w * 0.08f, center = Offset(w * 0.38f, h * 0.42f))
                    drawCircle(color = Color(0xFFFF6D00), radius = w * 0.07f, center = Offset(w * 0.62f, h * 0.42f))
                    drawCircle(color = Color(0xFFFFD600), radius = w * 0.06f, center = Offset(w * 0.50f, h * 0.60f))
                    // Fire spark
                    drawCircle(color = Color.White, radius = 4f, center = center)
                }

                // R-19 Garden Veggie Bloom: Veggie petals flower
                "R-19" -> {
                    // Center cherry tomato
                    drawCircle(color = Color(0xFFD50000), radius = w * 0.08f, center = center)
                    // Green petals around
                    for (i in 0..5) {
                        val angle = i * 2.0 * Math.PI / 6
                        val r = w * 0.22f
                        val cx = (w * 0.5f + r * Math.cos(angle)).toFloat()
                        val cy = (h * 0.5f + r * Math.sin(angle)).toFloat()
                        drawCircle(color = Color(0xFF43A047), radius = w * 0.06f, center = Offset(cx, cy))
                    }
                }

                // R-20 Royal Galaxy Cosmic: Stars and nebula swirl
                "R-20" -> {
                    // Nebula pink/purple glow
                    drawCircle(color = Color(0xFF8E24AA).copy(alpha = 0.6f), radius = w * 0.25f, center = center)
                    // Golden & silver stars
                    drawCircle(color = Color(0xFFFFD700), radius = w * 0.05f, center = Offset(w * 0.35f, h * 0.35f))
                    drawCircle(color = Color.White, radius = w * 0.06f, center = Offset(w * 0.65f, h * 0.40f))
                    drawCircle(color = Color(0xFF80D8FF), radius = w * 0.05f, center = Offset(w * 0.45f, h * 0.62f))
                    drawCircle(color = Color(0xFFFF80AB), radius = 4f, center = Offset(w * 0.55f, h * 0.30f))
                }
            }

            // Outer Cel-Shaded Crisp Outline
            drawCircle(
                color = Color(0xFF424242).copy(alpha = 0.4f),
                radius = outerRadius,
                center = center,
                style = Stroke(width = 4f)
            )
        }
    }
}

/**
 * 2D Cartoon Kitchen Tool Renderer.
 */
@Composable
fun CartoonToolItem(
    toolId: String,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val w = this.size.width
            val h = this.size.height

            when {
                // Rolling Pin (TOOL-001)
                toolId == "TOOL-001" -> {
                    // Central cylinder body
                    drawRoundRect(
                        color = Color(0xFF8D6E63),
                        topLeft = Offset(w * 0.22f, h * 0.3f),
                        size = Size(w * 0.56f, h * 0.4f),
                        cornerRadius = CornerRadius(w * 0.08f, w * 0.08f)
                    )
                    drawRoundRect(
                        color = Color(0xFFD7CCC8),
                        topLeft = Offset(w * 0.25f, h * 0.35f),
                        size = Size(w * 0.50f, h * 0.15f),
                        cornerRadius = CornerRadius(w * 0.04f, w * 0.04f)
                    )
                    // Left handle
                    drawRoundRect(
                        color = Color(0xFF5D4037),
                        topLeft = Offset(w * 0.06f, h * 0.42f),
                        size = Size(w * 0.16f, h * 0.16f),
                        cornerRadius = CornerRadius(w * 0.06f, w * 0.06f)
                    )
                    // Right handle
                    drawRoundRect(
                        color = Color(0xFF5D4037),
                        topLeft = Offset(w * 0.78f, h * 0.42f),
                        size = Size(w * 0.16f, h * 0.16f),
                        cornerRadius = CornerRadius(w * 0.06f, w * 0.06f)
                    )
                }

                // Sauce Ladle (TOOL-006)
                toolId == "TOOL-006" -> {
                    // Long handle
                    drawLine(
                        color = Color(0xFF90A4AE),
                        start = Offset(w * 0.8f, h * 0.15f),
                        end = Offset(w * 0.4f, h * 0.65f),
                        strokeWidth = w * 0.08f
                    )
                    // Ladle cup
                    drawCircle(color = Color(0xFFCFD8DC), radius = w * 0.26f, center = Offset(w * 0.35f, h * 0.7f))
                    drawCircle(color = Color(0xFF90A4AE), radius = w * 0.26f, center = Offset(w * 0.35f, h * 0.7f), style = Stroke(width = w * 0.04f))
                    // Red sauce pool inside ladle
                    drawCircle(color = Color(0xFFE53935), radius = w * 0.18f, center = Offset(w * 0.35f, h * 0.7f))
                }

                // Box Grater (TOOL-005)
                toolId == "TOOL-005" -> {
                    // Grater body
                    val graterPath = Path().apply {
                        moveTo(w * 0.3f, h * 0.3f)
                        lineTo(w * 0.7f, h * 0.3f)
                        lineTo(w * 0.8f, h * 0.88f)
                        lineTo(w * 0.2f, h * 0.88f)
                        close()
                    }
                    drawPath(path = graterPath, color = Color(0xFFCFD8DC), style = Fill)
                    drawPath(path = graterPath, color = Color(0xFF78909C), style = Stroke(width = w * 0.05f))
                    // Handle
                    drawArc(
                        color = Color(0xFF37474F),
                        startAngle = 180f,
                        sweepAngle = 180f,
                        useCenter = false,
                        topLeft = Offset(w * 0.35f, h * 0.12f),
                        size = Size(w * 0.3f, h * 0.25f),
                        style = Stroke(width = w * 0.07f)
                    )
                    // Grater holes
                    for (row in 0..2) {
                        for (col in 0..2) {
                            val hx = w * (0.35f + col * 0.15f)
                            val hy = h * (0.45f + row * 0.13f)
                            drawCircle(color = Color(0xFF546E7A), radius = w * 0.035f, center = Offset(hx, hy))
                        }
                    }
                }

                // Pizza Cutter (TOOL-008)
                toolId == "TOOL-008" -> {
                    // Red handle
                    drawRoundRect(
                        color = Color(0xFFE53935),
                        topLeft = Offset(w * 0.45f, h * 0.1f),
                        size = Size(w * 0.18f, h * 0.45f),
                        cornerRadius = CornerRadius(w * 0.08f, w * 0.08f)
                    )
                    // Cutter fork
                    drawLine(color = Color(0xFF78909C), start = Offset(w * 0.54f, h * 0.5f), end = Offset(w * 0.54f, h * 0.65f), strokeWidth = w * 0.08f)
                    // Spinning silver wheel
                    drawCircle(color = Color(0xFFECEFF1), radius = w * 0.26f, center = Offset(w * 0.54f, h * 0.72f))
                    drawCircle(color = Color(0xFF90A4AE), radius = w * 0.26f, center = Offset(w * 0.54f, h * 0.72f), style = Stroke(width = w * 0.04f))
                    drawCircle(color = Color(0xFF37474F), radius = w * 0.06f, center = Offset(w * 0.54f, h * 0.72f))
                }

                // Blender / Peel / Default
                else -> {
                    // Wooden peel paddle
                    drawCircle(color = Color(0xFFD7CCC8), radius = w * 0.36f, center = Offset(w * 0.5f, h * 0.45f))
                    drawCircle(color = Color(0xFF8D6E63), radius = w * 0.36f, center = Offset(w * 0.5f, h * 0.45f), style = Stroke(width = w * 0.04f))
                    drawRoundRect(
                        color = Color(0xFF5D4037),
                        topLeft = Offset(w * 0.45f, h * 0.65f),
                        size = Size(w * 0.10f, h * 0.3f),
                        cornerRadius = CornerRadius(w * 0.04f, w * 0.04f)
                    )
                }
            }
        }
    }
}
