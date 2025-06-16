package com.zeyadgasser.playground.badhabits.data

import androidx.room.Embedded
import androidx.room.Relation

data class BadHabitWithRatings(
    @Embedded
    val badHabit: BadHabitEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "routineId"
    )
    val ratings: List<BadHabitRatingEntity>,
)