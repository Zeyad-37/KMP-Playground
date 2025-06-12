package com.zeyadgasser.playground.routine.list.viewmodel

import com.zeyadgasser.playground.architecture.presentation.InputHandler
import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.routine.domain.CheckRoutineUseCase
import com.zeyadgasser.playground.routine.sharedpresentation.RoutinePresentationMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class RateRoutineInputHandler (
    private val checkRoutineUseCase: CheckRoutineUseCase,
    private val taskPresentationMapper: RoutinePresentationMapper,
    private val loadRoutineListInputHandler: LoadRoutineListInputHandler,
) : InputHandler<RoutineRatedInput, RoutineListState> {

    override suspend fun invoke(input: RoutineRatedInput, currentState: RoutineListState): Flow<Result> =
        flow {
            emit(LoadingResult(true))
            checkRoutineUseCase
                .invoke(taskPresentationMapper.fromPresentation(input.routine.copy(rating = input.rating)))
                .let { pair ->
//                    if (pair.first.success) emitAll(loadRoutineListInputHandler.invoke(input, currentState))
                }
            emit(LoadingResult(false))
        }
}
