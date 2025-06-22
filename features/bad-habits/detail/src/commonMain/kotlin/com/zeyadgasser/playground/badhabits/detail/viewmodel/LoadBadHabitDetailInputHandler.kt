package com.zeyadgasser.playground.badhabits.detail.viewmodel

import com.zeyadgasser.playground.architecture.presentation.InputHandler
import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.badhabits.detail.viewmodel.BadHabitDetailState.SuccessState
import com.zeyadgasser.playground.badhabits.domain.BadHabitsRepository
import com.zeyadgasser.playground.badhabits.domain.GetCurrentStreakUseCase
import com.zeyadgasser.playground.badhabits.domain.GetLongestStreakUseCase
import com.zeyadgasser.playground.badhabits.sharedpresentation.BadHabitsPresentationMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoadBadHabitDetailInputHandler(
    private val routineRepository: BadHabitsRepository,
    private val getLongestStreakUseCase: GetLongestStreakUseCase,
    private val getCurrentStreakUseCase: GetCurrentStreakUseCase,
    private val mapper: BadHabitsPresentationMapper = BadHabitsPresentationMapper,
) : InputHandler<LoadBadHabitDetailInput, BadHabitDetailState> {

    override fun invoke(input: LoadBadHabitDetailInput, state: BadHabitDetailState): Flow<Result> =
        flow {
            emit(
                SuccessState(
                    mapper.mapToPresentation(
                        routineRepository.getBadHabitById(input.badHabitId).let {
                            it.copy(
                                longestStreak = getLongestStreakUseCase.invoke(it.ratings, it.creationDate),
                                currentStreak = getCurrentStreakUseCase.invoke(it.ratings, it.creationDate),
                            )
                        }),
                    input.badHabitId
                )
            )
        }
}
