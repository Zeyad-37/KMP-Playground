package com.zeyadgasser.playground.badhabits.data

import com.zeyadgasser.playground.badhabits.domain.BadHabit
import kotlin.test.Test
import kotlin.test.assertEquals

class DataBadHabitsMapperTest {

    private val mapper = DataBadHabitsMapper()

    @Test
    fun `mapFromDomain maps BadHabit to BadHabitEntity`() {
        val habit = BadHabit(
            id = 1,
            name = "Smoking",
            description = "Avoid smoking",
            frequency = "Daily",
            reminders = "Morning"
        )

        val entity = mapper.mapFromDomain(habit)

        assertEquals(habit.id, entity.id)
        assertEquals(habit.name, entity.name)
        assertEquals(habit.description, entity.description)
        assertEquals(habit.frequency, entity.frequency)
        assertEquals(habit.reminders, entity.reminders)
    }

    @Test
    fun `mapToDomain maps BadHabitEntity to BadHabit`() {
        val entity = BadHabitEntity(
            id = 1,
            name = "Smoking",
            description = "Avoid smoking",
            frequency = "Daily",
            reminders = "Morning"
        )

        val habit = mapper.mapToDomain(entity)

        assertEquals(entity.id, habit.id)
        assertEquals(entity.name, habit.name)
        assertEquals(entity.description, habit.description)
        assertEquals(entity.frequency, habit.frequency)
        assertEquals(entity.reminders, habit.reminders)
    }
}