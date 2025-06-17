package com.zeyadgasser.playground.badhabits.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface BadHabitsDao {
    @Transaction
    @Query("SELECT * FROM Bad_Habits")
    fun getBadHabitWithRatings(): Flow<List<BadHabitWithRatings>>

    @Query("SELECT * FROM Bad_Habits WHERE id = :id")
    suspend fun getBadHabitById(id: Long): BadHabitEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habit: BadHabitEntity)

    @Query("DELETE FROM Bad_Habits WHERE id = :id")
    suspend fun deleteById(id: Long)
}