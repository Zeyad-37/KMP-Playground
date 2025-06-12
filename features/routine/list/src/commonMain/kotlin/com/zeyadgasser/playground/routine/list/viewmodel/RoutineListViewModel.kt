package com.zeyadgasser.playground.routine.list.viewmodel

import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.architecture.presentation.ViewModel
import com.zeyadgasser.playground.routine.domain.CheckRoutineUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class RoutineListViewModel(
    private val loadRoutineListInputHandler: LoadRoutineListInputHandler,
    private val rateRoutineInputHandler: RateRoutineInputHandler,
    initialState: RoutineListState,
    reducer: RoutinesReducer,
    dispatcher: CoroutineDispatcher = Default,
) : ViewModel<RoutineListInput, RoutineListResult, RoutineListState, RoutineListEffect>(
    initialState, reducer, dispatcher
) {
    override suspend fun resolve(input: RoutineListInput, state: RoutineListState): Flow<Result> =
        when (input) {
            is LoadRoutineListInput -> loadRoutineListInputHandler.invoke(input, state)
            CreateRoutineInput -> flowOf(GoToCreateRoutineEffect)
            is RoutineCheckedInput -> flowOf(ShowRatingDialogEffect(input.routine))
            is RoutineClickedInput -> flowOf(GoToRoutineDetailsEffect(input.routine.id))
            is RoutineRatedInput -> rateRoutineInputHandler.invoke(input, state)
            HideDialogInput -> flowOf(HideDialogEffect)
        }
}
