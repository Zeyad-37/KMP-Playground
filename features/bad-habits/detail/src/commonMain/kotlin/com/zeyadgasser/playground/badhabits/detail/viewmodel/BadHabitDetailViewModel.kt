package com.zeyadgasser.playground.badhabits.detail.viewmodel

import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.architecture.presentation.ViewModel
import com.zeyadgasser.playground.badhabits.detail.viewmodel.NavigationInput.EditBadHabitDetailInput
import com.zeyadgasser.playground.badhabits.detail.viewmodel.NavigationInput.GoBackInput
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class BadHabitDetailViewModel(
    private val loadBadHabitDetailInputHandler: LoadBadHabitDetailInputHandler,
    private val deleteBadHabitDetailInputHandler: DeleteBadHabitInputHandler,
    private val navigationInputHandler: NavigationInputHandler,
    initialState: BadHabitDetailState,
    dispatcher: CoroutineDispatcher,
) : ViewModel<BadHabitDetailInput, Result, BadHabitDetailState, BadHabitDetailEffect>(
    initialState, dispatcher = dispatcher
) {
    override suspend fun resolve(input: BadHabitDetailInput, state: BadHabitDetailState): Flow<Result> =
        when (input) {
            EditBadHabitDetailInput -> navigationInputHandler.invoke(input as EditBadHabitDetailInput, state)
            GoBackInput -> navigationInputHandler.invoke(input as GoBackInput, state)
            is LoadBadHabitDetailInput -> loadBadHabitDetailInputHandler.invoke(input, state)
            is DeleteBadHabitDetailInput -> deleteBadHabitDetailInputHandler.invoke(input, state)
        }
}
