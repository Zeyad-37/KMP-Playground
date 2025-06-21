package com.zeyadgasser.playground.badhabits.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BadHabitRatingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRating(rating: BadHabitRatingEntity)

    @Query("SELECT * FROM BadHabitRatings WHERE badHabitId = :badHabitId ORDER BY date DESC")
    suspend fun getRatingsForBadHabit(badHabitId: Long): List<BadHabitRatingEntity>

    // Optional: Function to delete a rating by habit ID and date (useful if ID is unknown)
    @Query("DELETE FROM BadHabitRatings WHERE badHabitId = :badHabitId AND date = :date")
    suspend fun deleteRatingByHabitIdAndDate(badHabitId: Long, date: String)
}
