package com.zeyadgasser.playground.badhabits.list.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeyadgasser.playground.badhabits.list.resources.Res
import org.jetbrains.compose.resources.painterResource

@Composable
fun NoBadHabitsTrackedScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Title
        Text(
            text = "Bad Habits",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        // Illustration
        Image(
            painter = painterResource(id = Res.drawable.illustration_no_habits),
            contentDescription = "No Bad Habits Tracked",
            modifier = Modifier
                .size(200.dp)
                .padding(vertical = 16.dp)
        )

        // Subtitle
        Text(
            text = "No Bad Habits Tracked",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Description
        Text(
            text = "It seems you're not tracking any bad habits yet. Start by adding the first one to gain insights and break free.",
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        // Button
        Button(
            onClick = { /* Handle add habit action */ },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(0.7f),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.LightGray,
                contentColor = Color.Black
            )
        ) {
            Text(text = "Add Your First Bad Habit", fontSize = 18.sp)
        }
    }
}