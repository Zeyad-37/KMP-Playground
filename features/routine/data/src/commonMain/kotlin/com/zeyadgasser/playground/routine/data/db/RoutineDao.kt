package com.zeyadgasser.playground.routine.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RoutineDao {
    @Query("SELECT * FROM Routines ORDER BY startTime ASC")
    suspend fun getAllRoutines(): List<RoutineEntity>

    @Query("SELECT * FROM Routines WHERE id = :id")
    suspend fun getRoutineById(id: Long): RoutineEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReplace(routine: RoutineEntity): Unit

    @Delete
    suspend fun delete(routine: RoutineEntity)

    @Query("DELETE FROM Routines WHERE id = :id")
    suspend fun deleteById(id: Long): Int // Returns the number of rows affected
}