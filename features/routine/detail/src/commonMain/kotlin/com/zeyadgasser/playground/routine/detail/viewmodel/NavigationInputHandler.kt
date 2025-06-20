package com.zeyadgasser.playground.routine.detail.viewmodel

import com.zeyadgasser.playground.architecture.presentation.InputHandler
import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.routine.detail.viewmodel.NavigationInput.GoBackInput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

object NavigationInputHandler : InputHandler<NavigationInput, RoutineDetailState> {
    override fun invoke(input: NavigationInput, state: RoutineDetailState): Flow<Result> =
        flowOf(NavToEffect(input is GoBackInput))
}
