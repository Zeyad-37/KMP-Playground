package com.zeyadgasser.playground.badhabits.sharedpresentation

data class BadHabitPM(
    val id: Long,
    val name: String,
    val description: String,
    val frequency: String,
    val reminders: Boolean,
    val ratings: List<BadHabitRatingPM> = emptyList(),
    val currentRating: Int = 0,
)
