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

    override suspend fun saveBadHabit(badHabit: BadHabit): Long {
        val badHabitEntity = dataMapper.mapFromDomain(badHabit)
        return if (badHabitEntity.id == 0L) { // New habit: insert
            badHabitsDao.insert(badHabitEntity)
        } else { // Existing habit: update
            badHabitsDao.update(badHabitEntity)
            badHabitEntity.id // Return the existing ID for updates
        }
    }

    override suspend fun insertBadHabitWithRatings(badHabit: BadHabit) {
        val badHabitEntity = dataMapper.mapFromDomain(badHabit)
        val badHabitId = if (badHabitEntity.id == 0L) {
            badHabitsDao.insert(badHabitEntity) // Insert new habit
        } else {
            badHabitsDao.update(badHabitEntity) // Update existing habit
            badHabitEntity.id
        }
        badHabit.ratings.forEach { domainRating ->
            val existingRatingEntity =
                badHabitRatingDao.getRatingByBadHabitIdAndDate(badHabitId, domainRating.date)
            val ratingEntityToSave = BadHabitRatingEntity(
                id = existingRatingEntity?.id ?: 0L, // Use existing ID for update, or 0 for new insert
                badHabitId = badHabitId,
                ratingValue = domainRating.ratingValue,
                date = domainRating.date
            )
            badHabitRatingDao.insertRating(ratingEntityToSave)
        }
    }

    override suspend fun deleteBadHabitById(id: Long) = badHabitsDao.deleteById(id)

    override suspend fun getBadHabitById(id: Long): BadHabit =
        dataMapper.mapToDomain(badHabitsDao.getBadHabitById(id))
}
