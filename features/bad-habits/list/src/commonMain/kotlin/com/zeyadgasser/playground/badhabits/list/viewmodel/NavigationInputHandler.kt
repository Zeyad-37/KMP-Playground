package com.zeyadgasser.playground.badhabits.list.viewmodel

import com.zeyadgasser.playground.architecture.presentation.InputHandler
import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.badhabits.list.viewmodel.NavigationInput.BadHabitClickedInput
import com.zeyadgasser.playground.badhabits.list.viewmodel.NavigationInput.CreateBadHabitInput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

object NavigationInputHandler : InputHandler<NavigationInput, BadHabitListState> {
    override fun invoke(input: NavigationInput, currentState: BadHabitListState): Flow<Result> =
        when (input) {
            is BadHabitClickedInput -> flowOf(GoToBadHabitDetailsEffect(input.badHabit.id))
            CreateBadHabitInput -> flowOf(GoToCreateBadHabitEffect)
        }
}
