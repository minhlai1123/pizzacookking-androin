package com.antigravity.pizzacooking.presentation.screens

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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.antigravity.pizzacooking.presentation.components.CartoonPizzaThumbnail
import com.antigravity.pizzacooking.presentation.components.ResponsiveKitchenCanvas
import com.antigravity.pizzacooking.presentation.theme.CreamDoughWhite
import com.antigravity.pizzacooking.presentation.theme.CrustBrownDark
import com.antigravity.pizzacooking.presentation.theme.DarkCharcoal
import com.antigravity.pizzacooking.presentation.theme.GentleBlue
import com.antigravity.pizzacooking.presentation.theme.GoldenCrust
import com.antigravity.pizzacooking.presentation.theme.PolishedOakWood
import com.antigravity.pizzacooking.presentation.theme.SoundEffects
import com.antigravity.pizzacooking.presentation.theme.WarmKitchenWood
import com.antigravity.pizzacooking.presentation.theme.WarmTomatoRed

@Composable
fun MainMenuScreen(
    onPlayClicked: () -> Unit,
    onGrownUpsClicked: () -> Unit
) {
    var isMuted by remember { mutableStateOf(SoundEffects.isMuted) }

    val infiniteTransition = rememberInfiniteTransition(label = "PlayPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.07f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScale"
    )

    val floatingOffset by infiniteTransition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "FloatingOffset"
    )

    ResponsiveKitchenCanvas(ambientBackgroundColor = WarmKitchenWood) { widthDp, heightDp ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // 1. CARTOON ITALIAN PIZZERIA ENVIRONMENT BACKGROUND
            PizzeriaShopfrontBackground()

            // 2. TOP CORNER CONTROLS
            // Left: Sound Toggle
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(20.dp)
                    .size(52.dp)
                    .shadow(6.dp, CircleShape)
                    .clip(CircleShape)
                    .background(if (isMuted) DarkCharcoal else GentleBlue)
                    .border(3.dp, CreamDoughWhite, CircleShape)
                    .clickable {
                        SoundEffects.isMuted = !SoundEffects.isMuted
                        isMuted = SoundEffects.isMuted
                        if (!isMuted) SoundEffects.playPop()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isMuted) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
                    contentDescription = "Sound",
                    tint = CreamDoughWhite,
                    modifier = Modifier.size(26.dp)
                )
            }

            // Right: Grown-Ups Settings Button
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(20.dp)
                    .size(52.dp)
                    .shadow(6.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(GentleBlue)
                    .border(3.dp, CreamDoughWhite, RoundedCornerShape(16.dp))
                    .clickable {
                        SoundEffects.playPop()
                        onGrownUpsClicked()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Grown-Ups Settings",
                    tint = CreamDoughWhite,
                    modifier = Modifier.size(26.dp)
                )
            }

            // 3. MAIN TITLE BANNER
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
                    .shadow(10.dp, RoundedCornerShape(24.dp))
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(WarmTomatoRed, GoldenCrust, WarmTomatoRed)
                        )
                    )
                    .border(4.dp, CreamDoughWhite, RoundedCornerShape(24.dp))
                    .padding(horizontal = 36.dp, vertical = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "🍕 PIZZA COOKING WORLD 👨‍🍳",
                        color = CreamDoughWhite,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            // 4. CENTER MAIN ACTION (Decorated Cartoon Chef & Big 3D Play Button)
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 80.dp, bottom = 40.dp, start = 60.dp, end = 60.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Cartoon Chef Badge & Rotating Pizza
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .scale(pulseScale)
                            .size(150.dp)
                            .shadow(12.dp, CircleShape)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(6.dp, GoldenCrust, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        CartoonPizzaThumbnail(recipeId = "R-02", size = 135.dp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(WarmTomatoRed)
                            .border(2.dp, CreamDoughWhite, RoundedCornerShape(12.dp))
                            .padding(horizontal = 14.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "20 MASTER RECIPES 🌟",
                            color = CreamDoughWhite,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                // Right: Big 3D Bouncy PLAY Button (Child-Friendly Min 120dp)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .scale(pulseScale)
                            .size(136.dp)
                            .shadow(16.dp, CircleShape)
                            .clip(CircleShape)
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF81C784), Color(0xFF2E7D32))
                                )
                            )
                            .border(6.dp, CreamDoughWhite, CircleShape)
                            .clickable {
                                SoundEffects.playPop()
                                onPlayClicked()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play Game",
                                tint = CreamDoughWhite,
                                modifier = Modifier.size(68.dp)
                            )
                            Text(
                                text = "PLAY NOW!",
                                color = CreamDoughWhite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Tap to start baking pizza!",
                        color = DarkCharcoal,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * 2D Cartoon Italian Pizzeria Shopfront Background
 */
@Composable
private fun PizzeriaShopfrontBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // 1. Warm Sunny Pastel Sky
        drawRect(
            color = Color(0xFFFFE0B2),
            topLeft = Offset(0f, 0f),
            size = Size(w, h * 0.45f)
        )

        // Floating fluffy cartoon clouds
        drawCircle(color = Color.White.copy(alpha = 0.65f), radius = 55f, center = Offset(w * 0.20f, h * 0.16f))
        drawCircle(color = Color.White.copy(alpha = 0.65f), radius = 40f, center = Offset(w * 0.25f, h * 0.14f))
        drawCircle(color = Color.White.copy(alpha = 0.65f), radius = 60f, center = Offset(w * 0.80f, h * 0.18f))
        drawCircle(color = Color.White.copy(alpha = 0.65f), radius = 45f, center = Offset(w * 0.74f, h * 0.16f))

        // 2. Red & White Striped Pizzeria Awning
        val awningHeight = h * 0.22f
        val stripeWidth = w / 16f
        for (i in 0..16) {
            val color = if (i % 2 == 0) WarmTomatoRed else CreamDoughWhite
            drawRect(
                color = color,
                topLeft = Offset(i * stripeWidth, 0f),
                size = Size(stripeWidth + 1f, awningHeight)
            )
            // Scallop curved bottom
            drawArc(
                color = color,
                startAngle = 0f,
                sweepAngle = 180f,
                useCenter = true,
                topLeft = Offset(i * stripeWidth, awningHeight - 16f),
                size = Size(stripeWidth + 1f, 32f)
            )
        }
        // Awning shadow
        drawLine(
            color = Color.Black.copy(alpha = 0.2f),
            start = Offset(0f, awningHeight + 16f),
            end = Offset(w, awningHeight + 16f),
            strokeWidth = 6f
        )

        // 3. Cozy Kitchen Counter / Stone Wall
        drawRect(
            color = Color(0xFFFFF3E0),
            topLeft = Offset(0f, awningHeight + 16f),
            size = Size(w, h * 0.45f)
        )

        // Subway brick grout lines
        val brickH = 36f
        val brickW = 72f
        val numBrickRows = ((h * 0.45f) / brickH).toInt() + 1
        val numBrickCols = (w / brickW).toInt() + 1
        for (row in 0..numBrickRows) {
            val y = awningHeight + 16f + row * brickH
            drawLine(
                color = Color.White.copy(alpha = 0.8f),
                start = Offset(0f, y),
                end = Offset(w, y),
                strokeWidth = 3f
            )
        }

        // 4. Polished Wooden Oak Counter Base
        drawRect(
            color = WarmKitchenWood,
            topLeft = Offset(0f, h * 0.68f),
            size = Size(w, h * 0.32f)
        )
        // Oak Counter Edge Lip
        drawRect(
            color = PolishedOakWood,
            topLeft = Offset(0f, h * 0.68f - 14f),
            size = Size(w, 20f)
        )
        drawLine(
            color = Color.White.copy(alpha = 0.4f),
            start = Offset(0f, h * 0.68f - 12f),
            end = Offset(w, h * 0.68f - 12f),
            strokeWidth = 3f
        )
    }
}
