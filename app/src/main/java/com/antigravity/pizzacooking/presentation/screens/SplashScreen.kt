package com.antigravity.pizzacooking.presentation.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.antigravity.pizzacooking.presentation.theme.CreamDoughWhite
import com.antigravity.pizzacooking.presentation.theme.GoldenCrust
import com.antigravity.pizzacooking.presentation.theme.WarmKitchenWood
import com.antigravity.pizzacooking.presentation.theme.WarmTomatoRed
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    val bounceAnim = remember { Animatable(-200f) }

    LaunchedEffect(Unit) {
        bounceAnim.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )
        delay(1000)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmKitchenWood),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .offset(y = bounceAnim.value.dp)
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(GoldenCrust),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(CreamDoughWhite)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "PIZZA COOKING WORLD",
                color = CreamDoughWhite,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Loading Chef Kitchen...",
                color = WarmTomatoRed,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
