package com.zeyadgasser.playground.badhabits.domain

import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn

class GetCurrentStreakUseCase(private val getGoodDayDates: GetGoodDayDatesUseCase) {

    /**
     * Calculates the current streak for a bad habit based on its ratings and creation date.
     *
     * @param ratings A list of [BadHabitRating] objects for a specific bad habit.
     * @param habitCreationDateString The creation date of the bad habit, in "yyyy-MM-dd" format.
     * @param goodDayThreshold The maximum rating value to consider a day "good".
     * Defaults to 0 (meaning only rating 0 is a good day, i.e., the habit was completely avoided).
     * @return The current streak in days.
     */
    fun invoke(
        ratings: List<BadHabitRating>,
        habitCreationDateString: String, // New parameter
        goodDayThreshold: Int = 0,
    ): String {
        val goodDayDates = getGoodDayDates.invoke(ratings, goodDayThreshold, habitCreationDateString)

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
}