package com.zeyadgasser.playground.badhabits.list.viewmodel

import com.zeyadgasser.playground.architecture.presentation.InputHandler
import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.badhabits.domain.BadHabitsRepository
import com.zeyadgasser.playground.badhabits.sharedpresentation.BadHabitRatingPM
import com.zeyadgasser.playground.badhabits.sharedpresentation.BadHabitsPresentationMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime

class RateBadHabitInputHandler(
    private val repository: BadHabitsRepository,
    private val badHabitsPresentationMapper: BadHabitsPresentationMapper,
) : InputHandler<BadHabitRatedInput, BadHabitListState> {

    override fun invoke(input: BadHabitRatedInput, state: BadHabitListState): Flow<Result> = flow {
        if (input.rating >= 0) {
            val currentData = getCurrentDate()
            input.badHabit.copy(
                ratings = if (input.badHabit.ratings.firstOrNull { it.date == currentData } != null) {
                    input.badHabit.ratings
                        .map { if (it.date == currentData) it.copy(ratingValue = input.rating) else it }
                } else input.badHabit.ratings.plus(BadHabitRatingPM(0, input.rating, getCurrentDate()))
            ).let { repository.insertBadHabitWithRatings(badHabitsPresentationMapper.mapFromPresentation(it)) }
        } else emit(ErrorEffect("Can not rate a bad habit with a negative rating"))
    }

    private fun getCurrentDate(): String = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        .date.format(LocalDate.Format {
            dayOfMonth()
            char('/')
            monthNumber()
            char('/')
            year()
        })
}
