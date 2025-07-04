package com.zeyadgasser.playground.badhabits.data

import com.zeyadgasser.playground.badhabits.domain.BadHabit
import com.zeyadgasser.playground.badhabits.domain.BadHabitRating

object DataBadHabitsMapper {

    fun mapToDomainList(entities: List<BadHabitWithRatings>): List<BadHabit> =
        entities.map { mapToDomain(it) }

    fun mapToDomain(entity: BadHabitWithRatings): BadHabit = BadHabit(
        id = entity.badHabit.id,
        name = entity.badHabit.name,
        description = entity.badHabit.description,
        frequency = entity.badHabit.frequency,
        reminders = entity.badHabit.reminders,
        creationDate = entity.badHabit.creationDate,
        ratings = entity.ratings.map { BadHabitRating(it.id, it.ratingValue, it.date) }
    )

    fun mapToDomain(entity: BadHabitEntity): BadHabit = BadHabit(
        id = entity.id,
        name = entity.name,
        description = entity.description,
        frequency = entity.frequency,
        reminders = entity.reminders,
        creationDate = entity.creationDate,
    )

    fun mapFromDomain(badHabit: BadHabit): BadHabitEntity = BadHabitEntity(
        id = badHabit.id,
        name = badHabit.name,
        description = badHabit.description,
        frequency = badHabit.frequency,
        reminders = badHabit.reminders,
        creationDate = badHabit.creationDate,
    )
}
