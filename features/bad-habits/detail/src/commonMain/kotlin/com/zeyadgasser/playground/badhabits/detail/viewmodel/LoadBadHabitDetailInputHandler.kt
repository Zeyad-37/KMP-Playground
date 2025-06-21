package com.zeyadgasser.playground.badhabits.detail.viewmodel

import com.zeyadgasser.playground.architecture.presentation.InputHandler
import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.badhabits.detail.viewmodel.BadHabitDetailState.SuccessState
import com.zeyadgasser.playground.badhabits.domain.BadHabitRating
import com.zeyadgasser.playground.badhabits.domain.BadHabitsRepository
import com.zeyadgasser.playground.badhabits.sharedpresentation.BadHabitRatingPM
import com.zeyadgasser.playground.badhabits.sharedpresentation.BadHabitsPresentationMapper
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn
import kotlinx.datetime.until
import kotlin.time.Duration.Companion.days

class LoadBadHabitDetailInputHandler(
    private val routineRepository: BadHabitsRepository,
    private val mapper: BadHabitsPresentationMapper = BadHabitsPresentationMapper,
) : InputHandler<LoadBadHabitDetailInput, BadHabitDetailState> {
    override fun invoke(input: LoadBadHabitDetailInput, state: BadHabitDetailState): Flow<Result> =
        flow {
            emit(
                SuccessState(
                    mapper.mapToPresentation(routineRepository.getBadHabitById(input.badHabitId))
                        .let {
                            it.copy(
                                longestStreak = calculateLongestStreak(it.ratings, it.creationDate),
                                currentStreak = calculateCurrentStreak(it.ratings, it.creationDate),
                            )
                        },
                    input.badHabitId
                )
            )
        }

    /**
     * Helper function to filter, parse, sort, and distinct "good" day dates from a list of ratings,
     * respecting the habit's creation date.
     *
     * @param ratings A list of [BadHabitRating] objects.
     * @param goodDayThreshold The maximum rating value to consider a day "good".
     * @param habitCreationDateString The creation date of the bad habit, in "yyyy-MM-dd" format.
     * @return A sorted, distinct list of [LocalDate] objects representing "good" days on or after the creation date.
     */
    private fun getGoodDayDates(
        ratings: List<BadHabitRatingPM>,
        goodDayThreshold: Int,
        habitCreationDateString: String, // New parameter for creationDate
    ): List<LocalDate> {
        if (ratings.isEmpty()) return emptyList()

        val creationDate = try {
            LocalDate.parse(habitCreationDateString)
        } catch (e: Exception) {
            Napier.e("Error parsing creation date '$habitCreationDateString': ${e.message}")
            // Fallback to an early date or throw an exception if creationDate is critical
            // For streak calculation, if creation date is invalid, it's safer to return empty or handle explicitly
            return emptyList()
        }

        return ratings
            .filter { it.ratingValue <= goodDayThreshold }
            .mapNotNull {
                try {
                    LocalDate.parse(it.date)
                } catch (e: Exception) {
                    Napier.e("Error parsing rating date '${it.date}': ${e.message}")
                    null
                }
            }
            .filter { it >= creationDate } // NEW: Filter out dates before the creationDate
            .sorted() // Sort dates in ascending order
            .distinct() // Remove duplicate dates if multiple good ratings on same day
    }

    /**
     * Calculates the current streak for a bad habit based on its ratings and creation date.
     *
     * @param ratings A list of [BadHabitRating] objects for a specific bad habit.
     * @param habitCreationDateString The creation date of the bad habit, in "yyyy-MM-dd" format.
     * @param goodDayThreshold The maximum rating value to consider a day "good".
     * Defaults to 0 (meaning only rating 0 is a good day, i.e., the habit was completely avoided).
     * @return The current streak in days.
     */
    fun calculateCurrentStreak( // TODO make use case
        ratings: List<BadHabitRatingPM>,
        habitCreationDateString: String, // New parameter
        goodDayThreshold: Int = 0,
    ): String {
        val goodDayDates = getGoodDayDates(ratings, goodDayThreshold, habitCreationDateString)

        if (goodDayDates.isEmpty()) return "0" // No good days found on or after creation date, so no streak

        var currentStreak = 0
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val yesterday = today.minus(DatePeriod(days = 1))

        var checkDate: LocalDate? = null
        if (goodDayDates.contains(today)) {
            checkDate = today
        } else if (goodDayDates.contains(yesterday)) {
            checkDate = yesterday
        }

        if (checkDate != null) {
            var tempCurrentStreak = 0
            var dayInSequence = checkDate
            val creationDate = LocalDate.parse(habitCreationDateString) // Re-parse creation date here for filtering
            while (goodDayDates.contains(checkDate) && checkDate >= creationDate) { // Ensure within creation boundary
                tempCurrentStreak++
                dayInSequence = dayInSequence?.minus(DatePeriod(days = 1))
            }
            currentStreak = tempCurrentStreak
        }
        return currentStreak.toString()
    }

    /**
     * Calculates the longest streak for a bad habit based on its ratings and creation date.
     *
     * @param ratings A list of [BadHabitRating] objects for a specific bad habit.
     * @param habitCreationDateString The creation date of the bad habit, in "yyyy-MM-dd" format.
     * @param goodDayThreshold The maximum rating value to consider a day "good".
     * Defaults to 0 (meaning only rating 0 is a good day, i.e., the habit was completely avoided).
     * @return The longest streak in days.
     */
    fun calculateLongestStreak( // TODO make use case
        ratings: List<BadHabitRatingPM>,
        habitCreationDateString: String, // New parameter
        goodDayThreshold: Int = 0,
    ): String {
        val goodDayDates = getGoodDayDates(ratings, goodDayThreshold, habitCreationDateString)

        if (goodDayDates.isEmpty()) return "0" // No good days found on or after creation date, so no streak

        var longestStreak = 0
        var currentConsecutiveStreak = 0
        var previousDate: LocalDate? = null

        for (date in goodDayDates) {
            if (previousDate == null) {
                currentConsecutiveStreak = 1
            } else {
                val daysBetween = previousDate.until(date, DateTimeUnit.DAY).days
                if (daysBetween.inWholeDays == 1L) {
                    currentConsecutiveStreak++
                } else {
                    currentConsecutiveStreak = 1
                }
            }
            longestStreak = maxOf(longestStreak, currentConsecutiveStreak)
            previousDate = date
        }
        return longestStreak.toString()
    }
}
