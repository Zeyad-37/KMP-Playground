package com.zeyadgasser.playground.breath.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zeyadgasser.playground.breath.viewmodel.BreathingCoachScreenState.ExerciseDetail
import com.zeyadgasser.playground.breath.viewmodel.BreathingPhase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailScreen(
    state: ExerciseDetail,
    onPlayPauseClicked: () -> Unit,
    onStopClicked: () -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.exercise.name) },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Breathing Animation Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.9f), // Allow animation to take available space
                contentAlignment = Alignment.Center
            ) {
                // Only show animation when playing or paused mid-session
                if (state.currentPhase != BreathingPhase.IDLE || state.isPlaying) {
                    BreathingAnimation(
                        currentPhase = state.currentPhase,
                        state.phaseProgress,
                        animationDurationMillis = state.phaseDuration
                    )
                } else {
                    // Optionally show placeholder text/image when idle before starting
                    Text("Ready to start?", style = MaterialTheme.typography.headlineMedium)
                }
            }
            // Instructions and Benefits Area (Scrollable if needed)
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
                    .weight(1f), // Adjust weight as needed
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Benefits",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = state.exercise.benefits,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Instructions",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = state.exercise.instructions,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Controls Area
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Optional: Progress Indicator for overall audio
                if (state.isPlaying || state.playbackProgress > 0f) {
                    LinearProgressIndicator(
                        progress = { state.playbackProgress },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(state.playbackProgressString)
                        Text(state.playbackDuration)
                    }
                }
                Row {
                    Button(
                        onClick = onPlayPauseClicked,
                        modifier = Modifier.fillMaxWidth(0.6f)
                    ) {
                        Icon(
                            imageVector = if (state.isPlaying) Icons.Filled.Close else Icons.Filled.PlayArrow,
                            contentDescription = if (state.isPlaying) "Pause" else "Play"
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(if (state.isPlaying) "Pause" else "Play 5 Min Guide")
                    }
                    if (state.isPlaying)
                        Button(onClick = onStopClicked, modifier.padding(horizontal = 8.dp)) {
                            Icon(Icons.Filled.Close, contentDescription = "Stop")
                        }
                }
            }
        }
    }
}
