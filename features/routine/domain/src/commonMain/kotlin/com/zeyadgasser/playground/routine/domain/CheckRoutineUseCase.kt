package com.zeyadgasser.playground.routine.domain

import com.zeyadgasser.playground.architecture.domain.Operation
import com.zeyadgasser.playground.architecture.domain.Value
import com.zeyadgasser.playground.architecture.utils.OpenForMokkery
import com.zeyadgasser.playground.routine.domain.model.Routine

@OpenForMokkery
class CheckRoutineUseCase(private val taskRepository: RoutineRepository) {

    suspend fun invoke(routine: Routine): Pair<Operation, Value> {
        return try {
            taskRepository.insertRoutineWithRatings(routine.copy(completed = routine.completed))
            Operation(true) to Value(routine.completed)
        } catch (e: Exception) {
            Operation(false) to Value(routine.completed)
        }
    }
}
