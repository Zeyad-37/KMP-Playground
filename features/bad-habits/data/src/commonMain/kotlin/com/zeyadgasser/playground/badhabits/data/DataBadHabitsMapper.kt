package com.zeyadgasser.playground.badhabits.data

import com.zeyadgasser.playground.badhabits.domain.BadHabit
import com.zeyadgasser.playground.badhabits.domain.BadHabitRating
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.toInstant

object DataBadHabitsMapper {

    fun mapToDomainList(entities: List<BadHabitWithRatings>): List<BadHabit> =
        entities.map { mapToDomain(it) }

    fun mapToDomain(entity: BadHabitWithRatings): BadHabit = BadHabit(
        id = entity.badHabit.id,
        name = entity.badHabit.name,
        description = entity.badHabit.description,
        frequency = entity.badHabit.frequency,
        reminders = entity.badHabit.reminders,
        ratings = entity.ratings.map { BadHabitRating(it.id, it.ratingValue, it.date) }
    )

//    private fun mapdate(date: Long): String {
//        val inputFormat = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss")
//        val localDateTime = LocalDateTime.parse(date, inputFormat)
//        val instant = localDateTime.toInstant(TimeZone.UTC)
//        // Format the output as desired
//        val outputFormat = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss")
//        return outputFormat.format(instant)
//    }

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
