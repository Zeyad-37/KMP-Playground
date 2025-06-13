package com.zeyadgasser.playground.badhabits.detail.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HabitDetailsScreen() {
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
            IconButton(onClick = { /* Handle back action */ }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Gray
                )
            }
            Text(
                text = "Habit Details",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }

        // Habit Title
        Text(
            text = "Late Night Snacking",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )

        // Habit Description
        Text(
            text = "Track your late-night snacking to improve your sleep and diet.",
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
        )

        // Performance Section
        Text(
            text = "Performance",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )

        // Weekly Snacking
        Text(
            text = "Weekly Snacking",
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )

        // Graph
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(start = 16.dp, end = 16.dp)
        ) {
            val points = listOf(
                Offset(0f, 50f), Offset(50f, 70f), Offset(100f, 30f),
                Offset(150f, 60f), Offset(200f, 40f), Offset(250f, 80f),
                Offset(300f, 30f)
            )
            drawLine(
                color = Color.Gray,
                start = points.first(),
                end = points.last(),
                strokeWidth = 2f,
                cap = StrokeCap.Round
            )
        }

        // Week Labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Week 1")
            Text(text = "Week 2")
            Text(text = "Week 3")
            Text(text = "Week 4")
        }

        // Streaks Section
        Text(
            text = "Streaks",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
        )

        // Current Streak and Longest Streak
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
                    .background(Color.LightGray, shape = MaterialTheme.shapes.medium)
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Current Streak", fontSize = 14.sp)
                    Text(text = "5 days", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.LightGray, shape = MaterialTheme.shapes.medium)
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Longest Streak", fontSize = 14.sp)
                    Text(text = "10 days", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}