package com.zeyadgasser.playground.badhabits.detail.viewmodel

import com.zeyadgasser.playground.architecture.presentation.InputHandler
import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.badhabits.detail.viewmodel.BadHabitDetailState.SuccessState
import com.zeyadgasser.playground.badhabits.domain.BadHabitsRepository
import com.zeyadgasser.playground.badhabits.sharedpresentation.BadHabitsPresentationMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoadBadHabitDetailInputHandler(
    private val routineRepository: BadHabitsRepository,
    private val mapper: BadHabitsPresentationMapper = BadHabitsPresentationMapper,
) : InputHandler<LoadBadHabitDetailInput, BadHabitDetailState> {
    override suspend fun invoke(input: LoadBadHabitDetailInput, state: BadHabitDetailState): Flow<Result> =
        flow {
            emit(
                SuccessState(
                    mapper.mapToPresentation(routineRepository.getBadHabitById(input.badHabitId)),
                    input.badHabitId
                )
            )
        }
}
