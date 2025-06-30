package com.zeyadgasser.playground.routine.list.viewmodel

import com.zeyadgasser.playground.architecture.presentation.InputHandler
import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.routine.domain.CheckRoutineUseCase
import com.zeyadgasser.playground.routine.sharedpresentation.RoutinePresentationMapper
import com.zeyadgasser.playground.utils.TimeService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class RateRoutineInputHandler(
    private val checkRoutineUseCase: CheckRoutineUseCase,
    private val taskPresentationMapper: RoutinePresentationMapper,
    private val loadRoutineListInputHandler: LoadRoutineListInputHandler,
    private val timeService: TimeService,
) : InputHandler<RoutineRatedInput, RoutineListState> {

    override fun invoke(input: RoutineRatedInput, state: RoutineListState): Flow<Result> = flow {
        emit(LoadingResult(true))
        checkRoutineUseCase.invoke(
            taskPresentationMapper.fromPresentation(
                input.routine.copy(
                    ratings = input.routine.ratings
                        .map {
                            if (it.date == timeService.getCurrentDateFormatted())
                                it.copy(ratingValue = input.rating)
                            else it
                        }
                )
            )
        ).let { pair ->
            if (pair.first.success)
                emitAll(loadRoutineListInputHandler.invoke(LoadRoutineListInput, state))
        }
        emit(LoadingResult(false))
    }
}
