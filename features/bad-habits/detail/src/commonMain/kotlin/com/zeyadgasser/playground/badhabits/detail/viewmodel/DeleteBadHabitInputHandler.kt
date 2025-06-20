package com.zeyadgasser.playground.badhabits.detail.viewmodel

import com.zeyadgasser.playground.architecture.presentation.InputHandler
import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.badhabits.domain.BadHabitsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteBadHabitInputHandler(private val routineRepository: BadHabitsRepository) :
    InputHandler<DeleteBadHabitDetailInput, BadHabitDetailState> {
    override fun invoke(input: DeleteBadHabitDetailInput, state: BadHabitDetailState): Flow<Result> =
        flow {
            routineRepository.deleteBadHabitById(state.id)
            emit(NavToEffect(true))
//            else emit(ErrorEffect("Could not delete routine!"))
        }
}
