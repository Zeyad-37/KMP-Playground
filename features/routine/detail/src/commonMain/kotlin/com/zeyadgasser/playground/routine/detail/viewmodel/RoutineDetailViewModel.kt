package com.zeyadgasser.playground.routine.detail.viewmodel

import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.architecture.presentation.ViewModel
import com.zeyadgasser.playground.routine.detail.viewmodel.NavigationInput.EditRoutineDetailInput
import com.zeyadgasser.playground.routine.detail.viewmodel.NavigationInput.GoBackInput
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class RoutineDetailViewModel(
    private val loadRoutineDetailInputHandler: LoadRoutineDetailInputHandler,
    private val deleteRoutineDetailInputHandler: DeleteRoutineDetailInputHandler,
    private val navigationInputHandler: NavigationInputHandler,
    initialState: RoutineDetailState,
    dispatcher: CoroutineDispatcher,
) : ViewModel<RoutineDetailInput, Result, RoutineDetailState, RoutineDetailEffect>(
    initialState, dispatcher = dispatcher
) {
    override suspend fun resolve(input: RoutineDetailInput, state: RoutineDetailState): Flow<Result> =
        when (input) {
            EditRoutineDetailInput -> navigationInputHandler.invoke(input as EditRoutineDetailInput, state)
            GoBackInput -> navigationInputHandler.invoke(input as GoBackInput, state)
            is LoadRoutineDetailInput -> loadRoutineDetailInputHandler.invoke(input, state)
            is DeleteRoutineDetailInput -> deleteRoutineDetailInputHandler.invoke(input, state)
        }
}
