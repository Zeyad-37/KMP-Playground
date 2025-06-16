package com.zeyadgasser.playground.badhabits.list.viewmodel

import com.zeyadgasser.playground.architecture.presentation.InputHandler
import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.badhabits.domain.BadHabitsRepository
import com.zeyadgasser.playground.badhabits.sharedpresentation.BadHabitRatingPM
import com.zeyadgasser.playground.badhabits.sharedpresentation.BadHabitsPresentationMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime

class RateBadHabitInputHandler(
    private val repository: BadHabitsRepository,
    private val badHabitsPresentationMapper: BadHabitsPresentationMapper,
    private val loadBadHabitListInputHandler: LoadBadHabitListInputHandler,
) : InputHandler<BadHabitRatedInput, BadHabitListState> {

    override suspend fun invoke(input: BadHabitRatedInput, currentState: BadHabitListState): Flow<Result> =
        flow {
            repository.insertBadHabitWithRatings(
                badHabitsPresentationMapper.mapFromPresentation(
                    input.badHabit.copy(
                        ratings = input.badHabit.ratings
                            .plus(BadHabitRatingPM(input.rating, getCurrentDate()))
                    )
                )
            )
            emitAll(loadBadHabitListInputHandler.invoke(LoadBadHabitListInput, currentState))
        }

    private fun getCurrentDate(): String { // todo centralise in a use-case
        val time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        return "${time.date.dayOfMonth}, ${time.date.month.number}, ${time.date.year}"
    }
}
