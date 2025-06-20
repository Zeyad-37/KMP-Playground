package com.zeyadgasser.playground.routine.detail.viewmodel

import com.zeyadgasser.playground.architecture.presentation.InputHandler
import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.routine.detail.viewmodel.RoutineDetailState.SuccessState
import com.zeyadgasser.playground.routine.domain.RoutineRepository
import com.zeyadgasser.playground.routine.sharedpresentation.RoutinePresentationMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoadRoutineDetailInputHandler(
    private val routineRepository: RoutineRepository,
    private val mapper: RoutinePresentationMapper = RoutinePresentationMapper,
) : InputHandler<LoadRoutineDetailInput, RoutineDetailState> {
    override fun invoke(input: LoadRoutineDetailInput, state: RoutineDetailState): Flow<Result> =
        flow {
            emit(
                SuccessState(
                    mapper.toPresentation(routineRepository.getRoutineById(input.routineId)), input.routineId
                )
            )
        }
}
