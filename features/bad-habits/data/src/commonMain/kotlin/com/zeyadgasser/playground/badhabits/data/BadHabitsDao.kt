package com.zeyadgasser.playground.badhabits.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BadHabitsDao {
    @Query("SELECT * FROM Bad_Habits")
    suspend fun getAll(): List<BadHabitEntity>

    @Query("SELECT * FROM Bad_Habits WHERE id = :id")
    suspend fun getBadHabitById(id: Long): BadHabitEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habit: BadHabitEntity)

    @Query("DELETE FROM Bad_Habits WHERE id = :id")
    suspend fun deleteById(id: Long)
}