package com.zeyadgasser.playground.routine.list.viewmodel

import com.zeyadgasser.playground.architecture.presentation.InputHandler
import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.routine.domain.CheckRoutineUseCase
import com.zeyadgasser.playground.routine.sharedpresentation.RoutinePresentationMapper
import com.zeyadgasser.playground.routine.sharedpresentation.RoutineRatingPM
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime

class RateRoutineInputHandler(
    private val checkRoutineUseCase: CheckRoutineUseCase,
    private val taskPresentationMapper: RoutinePresentationMapper,
    private val loadRoutineListInputHandler: LoadRoutineListInputHandler,
) : InputHandler<RoutineRatedInput, RoutineListState> {

    override fun invoke(input: RoutineRatedInput, currentState: RoutineListState): Flow<Result> = flow {
        emit(LoadingResult(true))
        val currentData = getCurrentDate()
        checkRoutineUseCase.invoke(
            taskPresentationMapper.fromPresentation(
                input.routine.copy(
                    ratings = input.routine.ratings
                        .map { if (it.date == currentData) it.copy(ratingValue = input.rating) else it }
                )
            )
        ).let { pair ->
            if (pair.first.success)
                emitAll(loadRoutineListInputHandler.invoke(LoadRoutineListInput, currentState))
        }
        emit(LoadingResult(false))
    }

    private fun getCurrentDate(): String = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        .let { "${it.date.dayOfMonth}/${it.date.month.number}/${it.date.year}" }// todo centralise in a use-case
}
