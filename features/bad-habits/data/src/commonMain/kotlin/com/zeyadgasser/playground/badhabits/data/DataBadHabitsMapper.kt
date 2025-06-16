package com.zeyadgasser.playground.badhabits.data

import com.zeyadgasser.playground.badhabits.domain.BadHabit

object DataBadHabitsMapper {
    fun mapToDomain(entity: BadHabitEntity): BadHabit = BadHabit(
        id = entity.id,
        name = entity.name,
        description = entity.description,
        frequency = entity.frequency,
        reminders = entity.reminders
    )

    fun mapFromDomain(badHabit: BadHabit): BadHabitEntity = BadHabitEntity(
        id = badHabit.id,
        name = badHabit.name,
        description = badHabit.description,
        frequency = badHabit.frequency,
        reminders = badHabit.reminders,
    )
}
