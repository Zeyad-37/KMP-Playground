package com.zeyadgasser.playground.routine.list.viewmodel

import com.zeyadgasser.playground.architecture.presentation.Effect
import com.zeyadgasser.playground.architecture.presentation.Input
import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.architecture.presentation.State
import com.zeyadgasser.playground.routine.sharedpresentation.RoutinePM

sealed class RoutineListInput : Input
data object CreateRoutineInput : RoutineListInput()
data object LoadRoutineListInput : RoutineListInput()
data class RoutineCheckedInput(val routine: RoutinePM) : RoutineListInput()
data class RoutineClickedInput(val routine: RoutinePM) : RoutineListInput()
data class RoutineRatedInput(val routine: RoutinePM, val rating: Int) : RoutineListInput()
data object HideDialogInput : RoutineListInput()

sealed class RoutineListResult : Result
data class LoadRoutineListResult(
    val routine: List<CategorisedRoutinePM>, val date: String,
) : RoutineListResult()

data class ErrorResult(val message: String) : RoutineListResult()
data class LoadingResult(val isLoading: Boolean) : RoutineListResult()

sealed class RoutineListEffect : Effect
data class GoToRoutineDetailsEffect(val routineId: Long) : RoutineListEffect()
data object GoToCreateRoutineEffect : RoutineListEffect()
data object HideDialogEffect : RoutineListEffect()
data class ShowRatingDialogEffect(val routine: RoutinePM) : RoutineListEffect()

sealed class RoutineListState(
    open val isLoading: Boolean, open val routine: List<CategorisedRoutinePM>,
) : State {
    data class InitialState(override val isLoading: Boolean) : RoutineListState(isLoading, emptyList())
    data object EmptyState : RoutineListState(false, emptyList())

    data class ErrorState(
        val message: String, override val isLoading: Boolean,
    ) : RoutineListState(isLoading, emptyList())

    data class SuccessState(
        override val routine: List<CategorisedRoutinePM>, val date: String, override val isLoading: Boolean,
    ) : RoutineListState(isLoading, routine)
}

data class CategorisedRoutinePM(val category: String, val routine: List<RoutinePM>)
