package com.zeyadgasser.playground.badhabits.sharedpresentation

import com.zeyadgasser.playground.badhabits.domain.BadHabit

object BadHabitsPresentationMapper {
    // Domain → Presentation
    fun mapToPresentation(badHabit: BadHabit): BadHabitPM = BadHabitPM(
        id = badHabit.id,
        name = badHabit.name,
        description = badHabit.description,
        frequency = badHabit.frequency,
        reminders = badHabit.reminders
    )

    // Presentation → Domain
    fun mapFromPresentation(badHabitPM: BadHabitPM): BadHabit = BadHabit(
        id = badHabitPM.id,
        name = badHabitPM.name,
        description = badHabitPM.description,
        frequency = badHabitPM.frequency,
        reminders = badHabitPM.reminders
    )

    // List mappings
    fun mapToPresentationList(badHabits: List<BadHabit>): List<BadHabitPM> {
        return badHabits.map { mapToPresentation(it) }
    }

    fun mapFromPresentationList(pmList: List<BadHabitPM>): List<BadHabit> =
        pmList.map { pm -> mapFromPresentation(pm) }
}