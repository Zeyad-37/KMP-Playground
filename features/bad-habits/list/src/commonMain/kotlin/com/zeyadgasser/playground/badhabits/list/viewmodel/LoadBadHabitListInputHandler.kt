package com.zeyadgasser.playground.badhabits.list.viewmodel

import com.zeyadgasser.playground.architecture.presentation.InputHandler
import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.badhabits.domain.BadHabitsRepository
import com.zeyadgasser.playground.badhabits.list.viewmodel.BadHabitListState.EmptyState
import com.zeyadgasser.playground.badhabits.list.viewmodel.BadHabitListState.SuccessState
import com.zeyadgasser.playground.badhabits.sharedpresentation.BadHabitsPresentationMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class LoadBadHabitListInputHandler(
    private val repository: BadHabitsRepository,
    private val taskPresentationMapper: BadHabitsPresentationMapper,
) : InputHandler<LoadBadHabitListInput, BadHabitListState> {

    override suspend fun invoke(input: LoadBadHabitListInput, currentState: BadHabitListState): Flow<Result> =
        flow {
            val badHabit = taskPresentationMapper.mapToPresentationList(repository.getBadHabits())
            emit(
                if (badHabit.isNotEmpty())
                    SuccessState(badHabit, getCurrentDate(), false)
                else EmptyState
            )
        }.onStart { emit(SuccessState(emptyList(), getCurrentDate(), true)) }

    private fun getCurrentDate(): String {
        val time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        return "Today, ${time.date.dayOfMonth}, ${time.date.month.name}, ${time.date.year}"
    }
}
