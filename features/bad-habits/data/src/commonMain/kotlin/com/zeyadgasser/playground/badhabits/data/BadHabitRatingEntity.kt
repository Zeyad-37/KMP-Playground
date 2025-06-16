package com.zeyadgasser.playground.badhabits.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "BadHabitRatings",
    foreignKeys = [
        ForeignKey(
            entity = BadHabitEntity::class,
            parentColumns = ["id"],
            childColumns = ["badHabitId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("badHabitId")]
)
data class BadHabitRatingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val badHabitId: Long, // Foreign key to Routines
    val ratingValue: Int, // e.g., 1-5 scale
    val date: String
)