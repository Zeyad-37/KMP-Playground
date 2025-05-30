package com.zeyadgasser.playground.routine.list.viewmodel

import com.zeyadgasser.playground.architecture.presentation.Reducer
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListState.EmptyState
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListState.ErrorState
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListState.InitialState
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListState.SuccessState

object RoutinesReducer : Reducer<RoutineListResult, RoutineListState> {

    override fun reduce(result: RoutineListResult, state: RoutineListState): RoutineListState =
        when (state) {
            is InitialState -> when (result) {
                is ErrorResult -> errorState(result.message, state.isLoading)
                is LoadingResult -> state.copy(isLoading = result.isLoading)
                is LoadRoutineListResult -> createSuccessState(result.routine, state.isLoading)
            }

            is ErrorState -> when (result) {
                is ErrorResult -> state.copy(message = result.message, state.isLoading)
                is LoadingResult -> state.copy(isLoading = result.isLoading)
                is LoadRoutineListResult -> createSuccessState(result.routine, state.isLoading)
            }

            is SuccessState -> when (result) {
                is ErrorResult -> errorState(result.message, state.isLoading)
                is LoadingResult -> state.copy(isLoading = result.isLoading)
                is LoadRoutineListResult -> createSuccessState(result.routine, state.isLoading)
            }

            EmptyState -> when (result) {
                is ErrorResult -> errorState(result.message, state.isLoading)
                is LoadingResult -> EmptyState
                is LoadRoutineListResult -> createSuccessState(result.routine, state.isLoading)
            }
        }

    private fun errorState(message: String, isLoading: Boolean) = ErrorState(message, isLoading)

    private fun createSuccessState(allRoutine: List<CategorisedRoutinePM>, isLoading: Boolean) =
        SuccessState(allRoutine, isLoading)
}
