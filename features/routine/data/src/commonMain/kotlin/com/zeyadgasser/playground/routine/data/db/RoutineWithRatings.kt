package com.zeyadgasser.playground.routine.data.db

import androidx.room.Embedded
import androidx.room.Relation

data class RoutineWithRatings(
    @Embedded
    val routine: RoutineEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "routineId"
    )
    val ratings: List<RoutineRatingEntity>,
)