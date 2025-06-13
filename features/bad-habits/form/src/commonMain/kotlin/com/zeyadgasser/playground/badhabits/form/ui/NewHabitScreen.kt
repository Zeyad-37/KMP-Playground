package com.zeyadgasser.playground.badhabits.form.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NewHabitScreen(onSave: (String, String, String, String) -> Unit) {
    // State to hold user input
    var habitName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("Daily") }
    var reminders by remember { mutableStateOf("None") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Handle close action */ }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.Gray
                )
            }
            Text(
                text = "New Habit",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }

        // Habit Name
        OutlinedTextField(
            value = habitName,
            onValueChange = { habitName = it },
            label = { Text(text = "Habit Name") },
            placeholder = { Text(text = "e.g., Smoking") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )

        // Description
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(text = "Description") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(100.dp),
            maxLines = 5
        )

        // Frequency
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Frequency",
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            DropdownMenuExample(
                selectedOption = frequency,
                onOptionSelected = { frequency = it },
                options = listOf("Daily", "Weekly", "Monthly")
            )
        }

        // Reminders
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Reminders",
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            DropdownMenuExample(
                selectedOption = reminders,
                onOptionSelected = { reminders = it },
                options = listOf("None", "Morning", "Evening", "Custom")
            )
        }

        // Save Button
        Button(
            onClick = {
                onSave(habitName, description, frequency, reminders)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text(text = "Save", fontSize = 18.sp)
        }
    }
}

