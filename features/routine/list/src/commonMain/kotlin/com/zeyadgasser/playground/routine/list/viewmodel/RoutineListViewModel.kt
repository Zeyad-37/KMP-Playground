package com.zeyadgasser.playground.routine.list.viewmodel

import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.architecture.presentation.ViewModel
import com.zeyadgasser.playground.routine.domain.CheckRoutineUseCase
import com.zeyadgasser.playground.routine.domain.RoutineRepository
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListState.EmptyState
import com.zeyadgasser.playground.routine.sharedpresentation.RoutinePM
import com.zeyadgasser.playground.routine.sharedpresentation.RoutinePresentationMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.onStart
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.toLocalDateTime

class RoutineListViewModel(
    private val repository: RoutineRepository,
    private val taskPresentationMapper: RoutinePresentationMapper,
    private val checkRoutineUseCase: CheckRoutineUseCase,
    initialState: RoutineListState,
    reducer: RoutinesReducer,
    dispatcher: CoroutineDispatcher = Default,
) : ViewModel<RoutineListInput, RoutineListResult, RoutineListState, RoutineListEffect>(
    initialState, reducer, dispatcher
) {
    override suspend fun resolve(input: RoutineListInput, state: RoutineListState): Flow<Result> =
        when (input) {
            LoadRoutineListInput -> onLoadRoutine()
            CreateRoutineInput -> flowOf(GoToCreateRoutineEffect)
            is RoutineCheckedInput -> flowOf(ShowRatingDialogEffect(input.routine))
            is RoutineClickedInput -> flowOf(GoToRoutineDetailsEffect(input.routine.id))
            is RoutineRatedInput -> onRoutineRatedInput(input.routine, input.rating)
            HideDialogInput -> flowOf(HideDialogEffect)
        }

    private fun onLoadRoutine(): Flow<Result> = flow<Result> {
        val routine = taskPresentationMapper.toPresentationList(repository.getAllRoutines())
        emit(
            if (routine.isNotEmpty())
                LoadRoutineListResult(groupIntoCategories(routine), getCurrentDate())
            else EmptyState
        )
    }.onEmpty { emit(LoadingResult(false)) }.onStart { emit(LoadingResult(true)) }

    private fun getCurrentDate(): String {
        val time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        return "Today, ${time.date.dayOfMonth}, ${time.date.month.name}, ${time.date.year}"
    }

    private fun groupIntoCategories(list: List<RoutinePM>): List<CategorisedRoutinePM> =
        list.groupBy { it.category }.toMap()
            .map { (category, articles) -> CategorisedRoutinePM(category, articles) }

    private fun onRoutineRatedInput(routine: RoutinePM, rating: Int): Flow<Result> = flow {
        emit(LoadingResult(true))
        checkRoutineUseCase.invoke(taskPresentationMapper.fromPresentation(routine.copy(rating = rating)))
            .let { pair ->
                if (pair.first.success) {
                    emit(HideDialogEffect)
                    emitAll(onLoadRoutine())
                }
            }
        emit(LoadingResult(false))
    }
}
