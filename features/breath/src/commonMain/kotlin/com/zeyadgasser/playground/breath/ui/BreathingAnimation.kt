package com.zeyadgasser.playground.breath.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zeyadgasser.playground.breath.viewmodel.BreathingPhase

@Composable
fun BreathingAnimation(
    currentPhase: BreathingPhase,
    phaseProgress: Int,
    modifier: Modifier = Modifier,
    minSize: Dp = 150.dp,
    maxSize: Dp = 250.dp,
    animationDurationMillis: Int, // Smooth transition duration
) {
    // Determine target size based on phase
    val targetSize = when (currentPhase) {
        BreathingPhase.INHALE -> maxSize
        BreathingPhase.HOLD_IN -> maxSize
        BreathingPhase.EXHALE -> minSize
        BreathingPhase.HOLD_OUT -> minSize
        BreathingPhase.IDLE -> minSize
    }

    // Determine target color based on phase
    val targetColor = when (currentPhase) {
        BreathingPhase.INHALE -> MaterialTheme.colorScheme.primary // Example color
        BreathingPhase.HOLD_IN -> MaterialTheme.colorScheme.secondary // Example color
        BreathingPhase.EXHALE -> MaterialTheme.colorScheme.tertiary // Example color
        BreathingPhase.HOLD_OUT -> MaterialTheme.colorScheme.surfaceVariant // Example color
        BreathingPhase.IDLE -> MaterialTheme.colorScheme.surfaceVariant
    }

    // Animate size changes smoothly
    val animatedSize by animateDpAsState(
        targetValue = targetSize,
        animationSpec = tween(durationMillis = animationDurationMillis),
        label = "BreathingCircleSize"
    )

    // Animate color changes smoothly
    val animatedColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = animationDurationMillis),
        label = "BreathingCircleColor"
    )

    // Determine text label for the phase
    val phaseText = when (currentPhase) {
        BreathingPhase.INHALE -> "Inhale\n$phaseProgress"
        BreathingPhase.HOLD_IN -> "Hold In\n$phaseProgress"
        BreathingPhase.EXHALE -> "Exhale\n$phaseProgress"
        BreathingPhase.HOLD_OUT -> "Hold Out\n$phaseProgress"
        BreathingPhase.IDLE -> ""
    }

    Box(
        modifier = modifier.size(maxSize), // Allocate max size for layout stability
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(animatedSize)) {
            drawCircle(color = animatedColor)
        }
        // Display phase text inside the circle (optional)
        if (phaseText.isNotEmpty()) {
            Text(
                text = phaseText,
                textAlign = Center,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimary // Adjust color for contrast if needed
            )
        }
    }
}