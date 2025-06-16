package com.zeyadgasser.playground.badhabits.list.viewmodel

import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.architecture.presentation.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class BadHabitListViewModel(
    private val loadBadHabitListInputHandler: LoadBadHabitListInputHandler,
    private val rateBadHabitInputHandler: RateBadHabitInputHandler,
    initialState: BadHabitListState,
    dispatcher: CoroutineDispatcher = Default,
) : ViewModel<BadHabitListInput, Result, BadHabitListState, BadHabitListEffect>(
    initialState, dispatcher = dispatcher
) {
    override suspend fun resolve(input: BadHabitListInput, state: BadHabitListState): Flow<Result> =
        when (input) {
            is LoadBadHabitListInput -> loadBadHabitListInputHandler.invoke(input, state)
            CreateBadHabitInput -> flowOf(GoToCreateBadHabitEffect)
            is BadHabitClickedInput -> flowOf(GoToBadHabitDetailsEffect(input.badHabit.id))
            is BadHabitRatedInput -> rateBadHabitInputHandler.invoke(input, state)
        }
}
