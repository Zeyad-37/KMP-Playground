package com.zeyadgasser.playground.routine.domain.model

data class Routine(
    val id: Long = 0,
    val name: String,
    val type: String,
    val startTime: String,
    val endTime: String,
    val description: String,
    val category: String,
    val completed: Boolean = false,
    val remindersEnabled: Boolean = false,
    val ratings: List<RoutineRating> = emptyList(),
)
