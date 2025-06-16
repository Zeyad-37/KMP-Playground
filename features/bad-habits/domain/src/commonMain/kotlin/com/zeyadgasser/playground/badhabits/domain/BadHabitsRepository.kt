package com.zeyadgasser.playground.badhabits.domain

interface BadHabitsRepository {

    suspend fun getBadHabits(): List<BadHabit>

    suspend fun addBadHabit(badHabit: BadHabit)

    suspend fun insertBadHabitWithRatings(badHabit: BadHabit)

    suspend fun deleteBadHabitById(id: Long)

    suspend fun getBadHabitById(id: Long): BadHabit?
}