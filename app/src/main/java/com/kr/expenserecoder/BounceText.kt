package com.kr.expenserecoder

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin


@Composable
fun WavyBounceText(
    text: String,
    amplitude: Dp = 3.dp,
    waveSpeedMillis: Int = 10000,
    wavePhaseShift: Float = 0.1f // Controls the "tightness" of the wave
) {
    // 1. Setup the Infinite Transition
    val infiniteTransition = rememberInfiniteTransition(label = "WavyBounceTransition")

    // 2. Animate a single float value from 0f to 1f continuously
    // This represents the current progress of one wave cycle (0% to 100%).
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = waveSpeedMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "AnimationProgress"
    )

    Row (modifier = Modifier.padding(16.dp)){
        text.forEachIndexed { index, char ->

            val sineInput = (progress + index * wavePhaseShift) * 2f * PI
            val yOffset = sin(sineInput.toFloat()) * amplitude.value

            Text(
                text = char.toString(),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    // Apply the vertical offset calculated above
                    .offset(y = yOffset.dp)
            )
        }
    }
}