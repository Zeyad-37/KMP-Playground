package com.zeyadgasser.playground.badhabits.list.viewmodel

import com.zeyadgasser.playground.architecture.presentation.InputHandler
import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.badhabits.domain.BadHabitsRepository
import com.zeyadgasser.playground.badhabits.sharedpresentation.BadHabitRatingPM
import com.zeyadgasser.playground.badhabits.sharedpresentation.BadHabitsPresentationMapper
import com.zeyadgasser.playground.utils.TimeService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RateBadHabitInputHandler(
    private val repository: BadHabitsRepository,
    private val badHabitsPresentationMapper: BadHabitsPresentationMapper,
    private val timeService: TimeService,
) : InputHandler<BadHabitRatedInput, BadHabitListState> {

    override fun invoke(input: BadHabitRatedInput, state: BadHabitListState): Flow<Result> = flow {
        if (input.rating >= 0) {
            val currentData = timeService.getCurrentDateFormatted()
            input.badHabit.copy(
                ratings = if (input.badHabit.ratings.firstOrNull { it.date == currentData } != null) {
                    input.badHabit.ratings
                        .map { if (it.date == currentData) it.copy(ratingValue = input.rating) else it }
                } else
                    input.badHabit.ratings
                        .plus(BadHabitRatingPM(0, input.rating, timeService.getCurrentDateFormatted()))
            ).let { repository.insertBadHabitWithRatings(badHabitsPresentationMapper.mapFromPresentation(it)) }
        } else emit(ErrorEffect("Can not rate a bad habit with a negative rating"))
    }
}
