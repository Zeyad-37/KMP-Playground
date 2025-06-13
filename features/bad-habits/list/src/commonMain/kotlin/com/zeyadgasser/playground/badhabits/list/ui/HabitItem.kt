package com.zeyadgasser.playground.badhabits.list.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HabitItem(
    name: String,
    frequency: String,
    progress: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Habit Name and Frequency
        Column(modifier = Modifier.weight(1f)) {
            Text(text = name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = frequency, fontSize = 14.sp, color = Color.Gray)
        }

        // Progress Bar
        LinearProgressIndicator(
            progress = { progress / 100f },
            modifier = Modifier
                .width(100.dp)
                .height(8.dp),
            color = Color.Gray,
        )

        // Progress Value
        Text(
            text = progress.toString(),
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}