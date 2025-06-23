package com.zeyadgasser.playground.badhabits.domain

import io.github.aakira.napier.Napier
import kotlinx.datetime.LocalDate

object GetGoodDayDatesUseCase {
    /**
     * Helper function to filter, parse, sort, and distinct "good" day dates from a list of ratings,
     * respecting the habit's creation date.
     *
     * @param ratings A list of [BadHabitRating] objects.
     * @param goodDayThreshold The maximum rating value to consider a day "good".
     * @param habitCreationDateString The creation date of the bad habit, in "yyyy-MM-dd" format.
     * @return A sorted, distinct list of [LocalDate] objects representing "good" days on or after the creation date.
     */
    fun invoke(
        ratings: List<BadHabitRating>,
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
}
