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

    override suspend fun invoke(input: RoutineRatedInput, currentState: RoutineListState): Flow<Result> =
        flow {
            emit(LoadingResult(true))
            checkRoutineUseCase.invoke(
                taskPresentationMapper.fromPresentation(
                    input.routine.copy(
                        ratings = input.routine.ratings.plus(RoutineRatingPM(input.rating, getCurrentDate()))
                    )
                )
            ).let { pair ->
                if (pair.first.success)
                    emitAll(loadRoutineListInputHandler.invoke(LoadRoutineListInput, currentState))
            }
            emit(LoadingResult(false))
        }

    private fun getCurrentDate(): String {
        val time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        return "${time.date.dayOfMonth}, ${time.date.month.number}, ${time.date.year}"
    }
}
