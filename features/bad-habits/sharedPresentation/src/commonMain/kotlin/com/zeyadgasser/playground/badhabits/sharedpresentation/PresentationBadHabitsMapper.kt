package com.zeyadgasser.playground.badhabits.sharedpresentation

import com.zeyadgasser.playground.badhabits.domain.BadHabit

class PresentationBadHabitsMapper {
    // Domain → Presentation
    fun mapToPresentation(badHabit: BadHabit): BadHabitPM = BadHabitPM(
        id = badHabit.id,
        name = badHabit.name,
        description = badHabit.description,
        frequency = badHabit.frequency,
        reminders = badHabit.reminders
    )

    // Presentation → Domain
    fun mapFromPresentation(badHabitPM: BadHabitPM, id: Long = 0): BadHabit = BadHabit(
        id = id,
        name = badHabitPM.name,
        description = badHabitPM.description,
        frequency = badHabitPM.frequency,
        reminders = badHabitPM.reminders
    )
}