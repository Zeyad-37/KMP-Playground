package com.zeyadgasser.playground.badhabits.list.viewmodel

import com.zeyadgasser.playground.architecture.presentation.InputHandler
import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.badhabits.domain.BadHabitsRepository
import com.zeyadgasser.playground.badhabits.sharedpresentation.BadHabitRatingPM
import com.zeyadgasser.playground.badhabits.sharedpresentation.BadHabitsPresentationMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class RateBadHabitInputHandler(
    private val repository: BadHabitsRepository,
    private val badHabitsPresentationMapper: BadHabitsPresentationMapper,
) : InputHandler<BadHabitRatedInput, BadHabitListState> {

    override suspend fun invoke(input: BadHabitRatedInput, state: BadHabitListState): Flow<Result> = flow {
        if (input.rating >= 0)
            repository.insertBadHabitWithRatings(
                badHabitsPresentationMapper.mapFromPresentation(
                    input.badHabit.copy(
                        ratings = input.badHabit.ratings
                            .plus(BadHabitRatingPM(0, input.rating, getCurrentDate()))
                    )
                )
            )
        else emit(ErrorEffect("Can not rate a bad habit with a negative rating"))
    }

    private fun getCurrentDate(): String = // todo centralise in a use-case
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toString()
}
