package com.zeyadgasser.playground.routine.detail.viewmodel

import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.architecture.presentation.ViewModel
import com.zeyadgasser.playground.routine.detail.viewmodel.RoutineDetailState.SuccessState
import com.zeyadgasser.playground.routine.domain.RoutineRepository
import com.zeyadgasser.playground.routine.sharedpresentation.RoutinePresentationMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class RoutineDetailViewModel(
    private val routineRepository: RoutineRepository,
    initialState: RoutineDetailState,
    dispatcher: CoroutineDispatcher,
) : ViewModel<RoutineDetailInput, Result, RoutineDetailState, RoutineDetailEffect>(
    initialState,
    dispatcher = dispatcher
) {

    private val mapper = RoutinePresentationMapper

    override suspend fun resolve(input: RoutineDetailInput, state: RoutineDetailState): Flow<Result> =
        when (input) {
            EditRoutineDetailInput -> flowOf(NavToEffect(false))
            GoBackInput -> flowOf(NavToEffect(true))
            is LoadRoutineDetailInput -> onLoadRoutine(input)
            DeleteRoutineDetailInput -> onDeleteRoutine(state)
        }


    private fun onLoadRoutine(input: LoadRoutineDetailInput): Flow<Result> = flow {
        emit(
            SuccessState(
                mapper.toPresentation(routineRepository.getRoutineById(input.routineId)), input.routineId
            )
        )
    }

    private fun onDeleteRoutine(state: RoutineDetailState): Flow<Result> = flow {
        if (routineRepository.deleteRoutine(state.id) == 1) emit(NavToEffect(true))
        else emit(ErrorEffect("Could not delete routine!"))
    }
}
