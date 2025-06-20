package com.zeyadgasser.playground.badhabits.list.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BadHabitItem(
    name: String,
    frequency: String,
    currentCount: String,
    onClick: () -> Unit,
    onCountChanged: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Habit Name and Frequency
        Column(modifier = Modifier.weight(1f)) {
            Text(text = name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = frequency, fontSize = 14.sp, color = Color.Gray)
        }

        Icon(
            imageVector = Icons.Default.Remove,
            contentDescription = null,
            Modifier.clickable { onCountChanged(currentCount.toInt() - 1) }
        )
        Text(
            text = currentCount,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            Modifier.padding(start = 8.dp).clickable { onCountChanged(currentCount.toInt() + 1) })
    }
}
