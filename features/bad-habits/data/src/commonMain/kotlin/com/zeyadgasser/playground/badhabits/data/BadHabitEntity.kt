package com.zeyadgasser.playground.badhabits.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Bad_Habits")
data class BadHabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String,
    val frequency: String,
    val reminders: Boolean,
)
