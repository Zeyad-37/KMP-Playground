package com.zeyadgasser.playground.badhabits.data

import com.zeyadgasser.playground.badhabits.domain.BadHabit
import com.zeyadgasser.playground.badhabits.domain.BadHabitsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BadHabitsRepositoryImpl(
    private val badHabitsDao: BadHabitsDao,
    private val badHabitRatingDao: BadHabitRatingDao,
    private val dataMapper: DataBadHabitsMapper,
) : BadHabitsRepository {

    override fun getBadHabits(): Flow<List<BadHabit>> =
        badHabitsDao.getBadHabitWithRatings().map { dataMapper.mapToDomainList(it) }

    override suspend fun addBadHabit(badHabit: BadHabit) =
        badHabitsDao.insert(dataMapper.mapFromDomain(badHabit))

    override suspend fun insertBadHabitWithRatings(badHabit: BadHabit) {
        badHabitsDao.insert(dataMapper.mapFromDomain(badHabit))
        badHabit.ratings.map {
            BadHabitRatingEntity(badHabitId = badHabit.id, ratingValue = it.ratingValue, date = it.date)
        }.forEach { badHabitRatingDao.insertRating(it) }
    }

    override suspend fun deleteBadHabitById(id: Long) = badHabitsDao.deleteById(id)

    override suspend fun getBadHabitById(id: Long): BadHabit =
        dataMapper.mapToDomain(badHabitsDao.getBadHabitById(id))
}
