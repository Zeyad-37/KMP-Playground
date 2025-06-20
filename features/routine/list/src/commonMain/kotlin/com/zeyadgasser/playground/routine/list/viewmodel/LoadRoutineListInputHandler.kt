package com.zeyadgasser.playground.routine.list.viewmodel

import com.zeyadgasser.playground.architecture.presentation.InputHandler
import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.routine.domain.RoutineRepository
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListState.EmptyState
import com.zeyadgasser.playground.routine.sharedpresentation.RoutinePM
import com.zeyadgasser.playground.routine.sharedpresentation.RoutinePresentationMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.onStart
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class LoadRoutineListInputHandler(
    private val repository: RoutineRepository,
    private val taskPresentationMapper: RoutinePresentationMapper,
) : InputHandler<LoadRoutineListInput, RoutineListState> {

    override fun invoke(input: LoadRoutineListInput, currentState: RoutineListState): Flow<Result> =
        flow {
            val routine = taskPresentationMapper.toPresentationList(repository.getAllRoutines())
            emit(
                if (routine.isNotEmpty())
                    LoadRoutineListResult(groupIntoCategories(routine), getCurrentDate())
                else EmptyState
            )
        }.onEmpty { emit(LoadingResult(false)) }.onStart { emit(LoadingResult(true)) }

    private fun groupIntoCategories(list: List<RoutinePM>): List<CategorisedRoutinePM> =
        list.groupBy { it.category }.toMap()
            .map { (category, articles) -> CategorisedRoutinePM(category, articles) }

    private fun getCurrentDate(): String {
        val time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        return "Today, ${time.date.dayOfMonth}, ${time.date.month.name}, ${time.date.year}"
    }
}
