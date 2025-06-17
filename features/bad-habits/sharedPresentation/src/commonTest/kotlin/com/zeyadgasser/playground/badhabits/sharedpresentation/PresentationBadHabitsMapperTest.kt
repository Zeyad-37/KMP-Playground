package com.zeyadgasser.playground.badhabits.sharedpresentation

import com.zeyadgasser.playground.badhabits.domain.BadHabit
import com.zeyadgasser.playground.badhabits.sharedpresentation.BadHabitsPresentationMapper
import kotlin.test.Test
import kotlin.test.assertEquals

class PresentationBadHabitsMapperTest {

    private val mapper = BadHabitsPresentationMapper()

    @Test
    fun `mapToPresentation converts domain to PM`() {
        val habit = BadHabit(
            id = 1,
            name = "Smoking",
            description = "Don't smoke",
            frequency = "Daily",
            reminders = "Morning"
        )

        val pm = mapper.mapToPresentation(habit)

        assertEquals(habit.name, pm.name)
        assertEquals(habit.description, pm.description)
        assertEquals(habit.frequency, pm.frequency)
        assertEquals(habit.reminders, pm.reminders)
    }

    @Test
    fun `mapFromPresentation converts PM to domain with default ID`() {
        val pm = BadHabitPM(
            id = 0L,
            name = "Late Night TV",
            description = "Stop watching TV after 10 PM",
            frequency = "Daily",
            reminders = "Evening"
        )

        val habit = mapper.mapFromPresentation(pm)

        assertEquals(0, habit.id)
        assertEquals(pm.name, habit.name)
        assertEquals(pm.description, habit.description)
        assertEquals(pm.frequency, habit.frequency)
        assertEquals(pm.reminders, habit.reminders)
    }

    @Test
    fun `mapFromPresentation converts PM to domain with provided ID`() {
        val pm = BadHabitPM(
            id = 5L,
            name = "Coffee Addiction",
            description = "Reduce coffee intake",
            frequency = "Daily",
            reminders = "Morning"
        )

        val habit = mapper.mapFromPresentation(pm, id = 5)

        assertEquals(5, habit.id)
        assertEquals(pm.name, habit.name)
    }
}
