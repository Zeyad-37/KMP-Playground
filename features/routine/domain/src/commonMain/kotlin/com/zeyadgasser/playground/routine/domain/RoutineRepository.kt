package com.zeyadgasser.playground.routine.domain

import com.zeyadgasser.playground.architecture.domain.Repository
import com.zeyadgasser.playground.routine.domain.model.Routine

interface RoutineRepository : Repository { // TODO Use kotlin Result
    suspend fun getAllRoutines(): List<Routine>

    suspend fun getRoutineById(id: Long): Routine

    suspend fun insertReplaceRoutine(routine: Routine)

    suspend fun insertRoutineWithRatings(routine: Routine)

    suspend fun deleteRoutine(id: Long): Int
}
