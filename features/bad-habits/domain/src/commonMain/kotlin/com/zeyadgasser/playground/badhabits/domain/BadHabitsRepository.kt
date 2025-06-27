package com.zeyadgasser.playground.badhabits.domain

import kotlinx.coroutines.flow.Flow

interface BadHabitsRepository {

    fun getBadHabits(): Flow<List<BadHabit>>

    suspend fun saveBadHabit(badHabit: BadHabit): Long

    suspend fun insertBadHabitWithRatings(badHabit: BadHabit)

    suspend fun deleteBadHabitById(id: Long)

    suspend fun getBadHabitById(id: Long): BadHabit
}