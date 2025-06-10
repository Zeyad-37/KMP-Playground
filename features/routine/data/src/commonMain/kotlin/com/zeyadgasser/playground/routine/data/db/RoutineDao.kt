package com.zeyadgasser.playground.routine.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface RoutineDao {
    @Query("SELECT * FROM Routines ORDER BY startTime ASC")
    suspend fun getAllRoutines(): List<RoutineEntity>

    @Query("SELECT * FROM Routines WHERE id = :id")
    suspend fun getRoutineById(id: Long): RoutineEntity

    @Transaction
    @Query("SELECT * FROM Routines WHERE id = :id")
    suspend fun getRoutineWithRatingsById(id: Long): RoutineWithRatings

    @Insert(onConflict = REPLACE)
    suspend fun insertReplace(routine: RoutineEntity): Unit

    @Delete
    suspend fun delete(routine: RoutineEntity)

    @Query("DELETE FROM Routines WHERE id = :id")
    suspend fun deleteById(id: Long): Int // Returns the number of rows affected
}