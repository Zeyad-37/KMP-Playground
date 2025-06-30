package com.zeyadgasser.playground

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.MoodBad
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object RoutineList : BottomNavItem("RoutineList", "Routines", Icons.Default.Mood)
    object BadHabitList : BottomNavItem("BadHabitList", "Bad Habits", Icons.Default.MoodBad)
    object Profile : BottomNavItem("Profile", "Profile", Icons.Default.AccountCircle)
}
