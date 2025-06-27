package com.zeyadgasser.playground.badhabits.detail.viewmodel

import com.zeyadgasser.playground.architecture.presentation.Effect
import com.zeyadgasser.playground.architecture.presentation.Input
import com.zeyadgasser.playground.architecture.presentation.State
import com.zeyadgasser.playground.badhabits.sharedpresentation.BadHabitPM

sealed class BadHabitDetailInput : Input
data object DeleteBadHabitDetailInput : BadHabitDetailInput()
data class LoadBadHabitDetailInput(val badHabitId: Long) : BadHabitDetailInput()
sealed class NavigationInput : BadHabitDetailInput() {
    data object GoBackInput : NavigationInput()
    data object EditBadHabitDetailInput : NavigationInput()
}

sealed class BadHabitDetailEffect : Effect
data class NavToEffect(val isUp: Boolean) : BadHabitDetailEffect() // true to list, false to edit
data class ErrorEffect(val message: String) : BadHabitDetailEffect()

sealed class BadHabitDetailState(open val id: Long) : State {
    data object InitialState : BadHabitDetailState(0)
    data class SuccessState(val badHabit: BadHabitPM, override val id: Long) : BadHabitDetailState(id)
}


