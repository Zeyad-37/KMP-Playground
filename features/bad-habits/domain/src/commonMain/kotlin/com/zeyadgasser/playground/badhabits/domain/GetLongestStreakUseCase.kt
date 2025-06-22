package com.zeyadgasser.playground.badhabits.domain

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.until
import kotlin.time.Duration.Companion.days

class GetLongestStreakUseCase(private val getGoodDayDates: GetGoodDayDatesUseCase) {

    /**
     * Calculates the longest streak for a bad habit based on its ratings and creation date.
     *
     * @param ratings A list of [BadHabitRating] objects for a specific bad habit.
     * @param habitCreationDateString The creation date of the bad habit, in "yyyy-MM-dd" format.
     * @param goodDayThreshold The maximum rating value to consider a day "good".
     * Defaults to 0 (meaning only rating 0 is a good day, i.e., the habit was completely avoided).
     * @return The longest streak in days.
     */
    fun invoke(
        ratings: List<BadHabitRating>,
        habitCreationDateString: String, // New parameter
        goodDayThreshold: Int = 0,
    ): String {
        val goodDayDates = getGoodDayDates.invoke(ratings, goodDayThreshold, habitCreationDateString)

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