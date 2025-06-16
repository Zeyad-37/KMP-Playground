package com.zeyadgasser.playground.badhabits.domain

data class BadHabit(
    val id: Long,
    val name: String,
    val description: String,
    val frequency: String,
    val reminders: String,
    val ratings: List<BadHabitRating> = emptyList(),
    val currentRating: Int = 0,
)
