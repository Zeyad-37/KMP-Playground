package com.zeyadgasser.playground.routine.detail.viewmodel

import com.zeyadgasser.playground.architecture.presentation.InputHandler
import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.routine.domain.RoutineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class EditRoutineDetailInputHandler : InputHandler<EditRoutineDetailInput, RoutineDetailState> {
    override suspend fun invoke(
        input: EditRoutineDetailInput, state: RoutineDetailState,
    ): Flow<Result> = flowOf(NavToEffect(false))
}

class GoBackInputHandler : InputHandler<GoBackInput, RoutineDetailState> {
    override suspend fun invoke(
        input: GoBackInput, state: RoutineDetailState,
    ): Flow<Result> = flowOf(NavToEffect(true))
}

class LoadRoutineDetailInputHandler(private val routineRepository: RoutineRepository) : InputHandler<LoadRoutineDetailInput, RoutineDetailState> {
    override suspend fun invoke(
        input: LoadRoutineDetailInput, state: RoutineDetailState,
    ): Flow<Result> = flowOf(NavToEffect(true))
}