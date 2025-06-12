package com.zeyadgasser.playground.routine.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RoutineRatingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRating(rating: RoutineRatingEntity)

    @Query("SELECT * FROM RoutineRatings WHERE routineId = :routineId ORDER BY date DESC")
    suspend fun getRatingsForRoutine(routineId: Long): List<RoutineRatingEntity>
}