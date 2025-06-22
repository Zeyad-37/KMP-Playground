package com.zeyadgasser.playground.badhabits.sharedpresentation

import com.zeyadgasser.playground.badhabits.domain.BadHabit
import com.zeyadgasser.playground.badhabits.domain.BadHabitRating

object BadHabitsPresentationMapper {
    // Domain → Presentation
    fun mapToPresentation(badHabit: BadHabit): BadHabitPM = BadHabitPM(
        id = badHabit.id,
        name = badHabit.name,
        description = badHabit.description,
        frequency = badHabit.frequency,
        reminders = badHabit.reminders,
        creationDate = badHabit.creationDate,
        ratings = badHabit.ratings.map { BadHabitRatingPM(it.id, it.ratingValue, it.date) },
        currentRating = badHabit.ratings.lastOrNull()?.ratingValue ?: 0,
        currentStreak = badHabit.currentStreak,
        longestStreak = badHabit.longestStreak
    )

    // Presentation → Domain
    fun mapFromPresentation(badHabitPM: BadHabitPM): BadHabit = BadHabit(
        id = badHabitPM.id,
        name = badHabitPM.name,
        description = badHabitPM.description,
        frequency = badHabitPM.frequency,
        reminders = badHabitPM.reminders,
        creationDate = badHabitPM.creationDate,
        ratings = badHabitPM.ratings.map { BadHabitRating(it.id, it.ratingValue, it.date) },
        currentStreak = badHabitPM.currentStreak,
        longestStreak = badHabitPM.longestStreak
    )

    // List mappings
    fun mapToPresentationList(badHabits: List<BadHabit>): List<BadHabitPM> =
        badHabits.map { mapToPresentation(it) }

    fun mapFromPresentationList(pmList: List<BadHabitPM>): List<BadHabit> =
        pmList.map { pm -> mapFromPresentation(pm) }
}