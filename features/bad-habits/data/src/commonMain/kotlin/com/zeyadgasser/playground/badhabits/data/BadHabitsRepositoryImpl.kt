package com.zeyadgasser.playground.badhabits.data

import com.zeyadgasser.playground.badhabits.domain.BadHabit
import com.zeyadgasser.playground.badhabits.domain.BadHabitsRepository

class BadHabitsRepositoryImpl(
    private val badHabitsDao: BadHabitsDao,
    private val badHabitRatingDao: BadHabitRatingDao,
    private val dataMapper: DataBadHabitsMapper,
) : BadHabitsRepository {

    override suspend fun getBadHabits(): List<BadHabit> = badHabitsDao.getAll().map { dataMapper.mapToDomain(it) }

    override suspend fun addBadHabit(badHabit: BadHabit) = badHabitsDao.insert(dataMapper.mapFromDomain(badHabit))

    override suspend fun insertBadHabitWithRatings(badHabit: BadHabit) {
        badHabitsDao.insert(dataMapper.mapFromDomain(badHabit))
        // Step 3: Map ratings and insert them
        val ratingEntities = badHabit.ratings.map { rating ->
            BadHabitRatingEntity(
                badHabitId = badHabit.id,
                ratingValue = rating.ratingValue,
                date = rating.date
            )
        }
        ratingEntities.forEach { badHabitRatingDao.insertRating(it) }
    }

    override suspend fun deleteBadHabitById(id: Long) = badHabitsDao.deleteById(id)

    override suspend fun getBadHabitById(id: Long): BadHabit? =
        badHabitsDao.getBadHabitById(id)?.let { dataMapper.mapToDomain(it) }
}
