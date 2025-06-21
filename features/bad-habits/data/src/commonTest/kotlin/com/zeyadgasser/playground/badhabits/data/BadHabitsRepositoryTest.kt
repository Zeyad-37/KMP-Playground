package com.zeyadgasser.playground.badhabits.data

import com.zeyadgasser.playground.badhabits.domain.BadHabit
import com.zeyadgasser.playground.badhabits.domain.BadHabitsRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class BadHabitsRepositoryTest {

    private val dao = mock<BadHabitsDao>()

    private val mapper = DataBadHabitsMapper()

    private lateinit var repository: BadHabitsRepository

    @BeforeTest
    fun setUp() {
        repository = BadHabitsRepositoryImpl(dao, mapper)
    }

    @Test
    fun `getBadHabits returns mapped domain models`() = runTest {
        // everySuspend
        val entity = BadHabitEntity(
            id = 1,
            name = "Smoking",
            description = "Don't smoke",
            frequency = "Daily",
            reminders = "Morning"
        )
        val expected = mapper.mapToDomain(entity)

        everySuspend { dao.getAll() } returns listOf(entity)

        // When
        val result = repository.getBadHabits()

        // Then
        assertEquals(listOf(expected), result)
    }

    @Test
    fun `addBadHabit inserts mapped entity into DAO`() = runTest {
        // everySuspend
        val habit = BadHabit(
            id = 1,
            name = "Smoking",
            description = "Don't smoke",
            frequency = "Daily",
            reminders = "Morning"
        )

        val expectedEntity = mapper.mapFromDomain(habit)

        everySuspend { dao.insert(expectedEntity) } returns Unit

        // When
        repository.saveBadHabit(habit)

        // Then
        verifySuspend { dao.insert(expectedEntity) }
    }

    @Test
    fun `deleteBadHabitById calls DAO delete method`() = runTest {
        val id = 1L

        everySuspend { dao.deleteById(id) } returns Unit

        repository.deleteBadHabitById(id)

        verifySuspend { dao.deleteById(id) }
    }

    @Test
    fun `getBadHabitById returns domain model if exists`() = runTest {
        val entity = BadHabitEntity(
            id = 1,
            name = "Smoking",
            description = "Don't smoke",
            frequency = "Daily",
            reminders = "Morning"
        )
        val expected = mapper.mapToDomain(entity)

        everySuspend { dao.getBadHabitById(1L) } returns entity

        val result = repository.getBadHabitById(1L)

        assertEquals(expected, result)
    }

    @Test
    fun `getBadHabitById returns null if not found`() = runTest {
        everySuspend { dao.getBadHabitById(1L) } returns null

        val result = repository.getBadHabitById(1L)

        assertNull(result)
    }
}