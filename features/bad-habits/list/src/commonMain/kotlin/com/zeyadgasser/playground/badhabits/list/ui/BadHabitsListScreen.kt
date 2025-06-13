package com.zeyadgasser.playground.badhabits.list.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BadHabitsListScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Bad Habits",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { /* Handle add habit action */ }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Habit",
                    tint = Color.Gray
                )
            }
        }

        // Habit List
        Column(modifier = Modifier.padding(16.dp)) {
            HabitItem(
                name = "Excessive Snacking",
                frequency = "Daily",
                progress = 10
            )
            HabitItem(
                name = "Late Night TV",
                frequency = "Weekly",
                progress = 30
            )
            HabitItem(
                name = "Excessive Coffee",
                frequency = "Daily",
                progress = 20
            )
            HabitItem(
                name = "Impulse Purchases",
                frequency = "Weekly",
                progress = 40
            )
        }
    }
}
