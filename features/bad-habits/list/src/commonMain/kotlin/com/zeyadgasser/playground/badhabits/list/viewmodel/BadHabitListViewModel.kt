package com.zeyadgasser.playground.badhabits.list.viewmodel

import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.architecture.presentation.ViewModel
import com.zeyadgasser.playground.badhabits.list.viewmodel.NavigationInput.BadHabitClickedInput
import com.zeyadgasser.playground.badhabits.list.viewmodel.NavigationInput.CreateBadHabitInput
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.flow.Flow

class BadHabitListViewModel(
    private val loadBadHabitListInputHandler: LoadBadHabitListInputHandler,
    private val rateBadHabitInputHandler: RateBadHabitInputHandler,
    private val navigationInputHandler: NavigationInputHandler,
    initialState: BadHabitListState,
    dispatcher: CoroutineDispatcher = Default,
) : ViewModel<BadHabitListInput, Result, BadHabitListState, BadHabitListEffect>(
    initialState, dispatcher = dispatcher
) {
    override suspend fun resolve(input: BadHabitListInput, state: BadHabitListState): Flow<Result> =
        when (input) {
            is LoadBadHabitListInput -> loadBadHabitListInputHandler.invoke(input, state)
            is CreateBadHabitInput -> navigationInputHandler.invoke(input, state)
            is BadHabitClickedInput -> navigationInputHandler.invoke(input, state)
            is BadHabitRatedInput -> rateBadHabitInputHandler.invoke(input, state)
        }
}
