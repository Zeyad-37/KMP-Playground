package com.zeyadgasser.playground.routine.detail.viewmodel

import com.zeyadgasser.playground.architecture.presentation.Effect
import com.zeyadgasser.playground.architecture.presentation.Input
import com.zeyadgasser.playground.architecture.presentation.State
import com.zeyadgasser.playground.routine.sharedpresentation.RoutinePM

sealed class RoutineDetailInput : Input
data object DeleteRoutineDetailInput : RoutineDetailInput()
data class LoadRoutineDetailInput(val routineId: Long) : RoutineDetailInput()
sealed class NavigationInput : RoutineDetailInput() {
    data object GoBackInput : NavigationInput()
    data object EditRoutineDetailInput : NavigationInput()
}

sealed class RoutineDetailEffect : Effect
data class NavToEffect(val isUp: Boolean) : RoutineDetailEffect() // true to list, false to edit
data class ErrorEffect(val message: String) : RoutineDetailEffect()

sealed class RoutineDetailState(open val id: Long) : State {
    data object InitialState : RoutineDetailState(0)
    data class SuccessState(val routine: RoutinePM, override val id: Long) : RoutineDetailState(id)
}
