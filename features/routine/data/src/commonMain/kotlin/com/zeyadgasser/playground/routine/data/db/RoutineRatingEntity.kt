package com.zeyadgasser.playground.routine.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "RoutineRatings",
    foreignKeys = [
        ForeignKey(
            entity = RoutineEntity::class,
            parentColumns = ["id"],
            childColumns = ["routineId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("routineId")]
)
data class RoutineRatingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val routineId: Long, // Foreign key to Routines
    val ratingValue: Int, // e.g., 1-5 scale
    val date: String
)