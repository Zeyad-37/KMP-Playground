package com.zeyadgasser.playground.badhabits.list.viewmodel

import com.zeyadgasser.playground.architecture.presentation.Effect
import com.zeyadgasser.playground.architecture.presentation.Input
import com.zeyadgasser.playground.architecture.presentation.State
import com.zeyadgasser.playground.badhabits.sharedpresentation.BadHabitPM

sealed class BadHabitListInput : Input
data object LoadBadHabitListInput : BadHabitListInput()
data class BadHabitRatedInput(val badHabit: BadHabitPM, val rating: Int) : BadHabitListInput()
sealed class NavigationInput : BadHabitListInput() {
    data object CreateBadHabitInput : NavigationInput()
    data class BadHabitClickedInput(val badHabit: BadHabitPM) : NavigationInput()
}

sealed class BadHabitListEffect : Effect
data class GoToBadHabitDetailsEffect(val badHabitId: Long) : BadHabitListEffect()
data object GoToCreateBadHabitEffect : BadHabitListEffect()

sealed class BadHabitListState(
    open val isLoading: Boolean, open val badHabit: List<BadHabitPM>,
) : State {
    data class InitialState(override val isLoading: Boolean) : BadHabitListState(isLoading, emptyList())
    data object EmptyState : BadHabitListState(false, emptyList())

    data class ErrorState(
        val message: String, override val isLoading: Boolean,
    ) : BadHabitListState(isLoading, emptyList())

    data class SuccessState(
        override val badHabit: List<BadHabitPM>, val date: String, override val isLoading: Boolean,
    ) : BadHabitListState(isLoading, badHabit)
}



