package com.zeyadgasser.playground.routine.domain

import com.zeyadgasser.playground.architecture.domain.Repository
import com.zeyadgasser.playground.routine.domain.model.Routine

interface RoutineRepository : Repository { // TODO Use kotlin Result - try an AI refactor
    suspend fun getAllRoutines(): List<Routine> // TODO Use flow

    suspend fun getRoutineById(id: Long): Routine

    suspend fun insertReplaceRoutine(routine: Routine)

    suspend fun insertRoutineWithRatings(routine: Routine)

    suspend fun deleteRoutine(id: Long): Int
}
