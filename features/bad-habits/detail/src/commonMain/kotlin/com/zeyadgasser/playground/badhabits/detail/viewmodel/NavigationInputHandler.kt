package com.zeyadgasser.playground.badhabits.detail.viewmodel

import com.zeyadgasser.playground.architecture.presentation.InputHandler
import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.badhabits.detail.viewmodel.NavigationInput.GoBackInput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

object NavigationInputHandler : InputHandler<NavigationInput, BadHabitDetailState> {
    override suspend fun invoke(input: NavigationInput, state: BadHabitDetailState): Flow<Result> =
        flowOf(NavToEffect(input is GoBackInput))
}
