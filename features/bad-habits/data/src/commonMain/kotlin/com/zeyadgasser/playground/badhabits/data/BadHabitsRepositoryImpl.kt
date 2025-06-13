package com.zeyadgasser.playground.badhabits.data

import com.zeyadgasser.playground.badhabits.domain.BadHabit
import com.zeyadgasser.playground.badhabits.domain.BadHabitsRepository

class BadHabitsRepositoryImpl(
    private val dao: BadHabitsDao,
    private val dataMapper: DataBadHabitsMapper,
) : BadHabitsRepository {

    override suspend fun getBadHabits(): List<BadHabit> = dao.getAll().map { dataMapper.mapToDomain(it) }

    override suspend fun addBadHabit(badHabit: BadHabit) = dao.insert(dataMapper.mapFromDomain(badHabit))

    override suspend fun deleteBadHabitById(id: Long) = dao.deleteById(id)

    override suspend fun getBadHabitById(id: Long): BadHabit? =
        dao.getBadHabitById(id)?.let { dataMapper.mapToDomain(it) }
}
