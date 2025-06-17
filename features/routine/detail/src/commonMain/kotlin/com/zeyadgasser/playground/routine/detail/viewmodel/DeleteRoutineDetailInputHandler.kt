package com.zeyadgasser.playground.routine.detail.viewmodel

import com.zeyadgasser.playground.architecture.presentation.InputHandler
import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.routine.domain.RoutineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteRoutineDetailInputHandler(private val routineRepository: RoutineRepository) :
    InputHandler<DeleteRoutineDetailInput, RoutineDetailState> {
    override suspend fun invoke(input: DeleteRoutineDetailInput, state: RoutineDetailState): Flow<Result> =
        flow {
            if (routineRepository.deleteRoutine(state.id) == 1) emit(NavToEffect(true))
            else emit(ErrorEffect("Could not delete routine!"))
        }
}
