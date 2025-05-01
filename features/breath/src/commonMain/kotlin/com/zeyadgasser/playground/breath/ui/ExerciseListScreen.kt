package com.zeyadgasser.playground.breath.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zeyadgasser.playground.breath.model.BreathingExercise

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseListScreen(
    exercises: List<BreathingExercise>,
    onExerciseSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Breathing Exercises") }) },
        modifier = modifier
    ) { paddingValues: PaddingValues ->
        LazyColumn(
            modifier = modifier.padding(paddingValues).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp) // Add space between items [31]
        ) {
            items(
                items = exercises,
                key = { exercise -> exercise.id } // Provide a stable key [34]
            ) { exercise ->
                ExerciseListItem(
                    exercise = exercise,
                    onClick = { onExerciseSelected(exercise.id) }
                )
            }
        }
    }
}

@Composable
fun ExerciseListItem(
    exercise: BreathingExercise,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick) // Make the whole card clickable [35, 36]
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = exercise.name, style = MaterialTheme.typography.titleMedium)
            // Optionally add a short description or category here
        }
    }
}
