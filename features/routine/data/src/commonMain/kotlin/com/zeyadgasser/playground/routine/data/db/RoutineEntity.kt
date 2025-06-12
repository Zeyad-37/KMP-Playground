package com.zeyadgasser.playground.routine.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Routines")
data class RoutineEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val type: String, // MORNING/NIGHT or map to enum in Room
    val startTime: String,
    val endTime: String,
    val description: String,
    val category: String,
    val completed: Boolean = false,
    val remindersEnabled: Boolean = false,
    val rating: Int? = null,
)
