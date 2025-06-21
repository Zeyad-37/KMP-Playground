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
        badHabit.ratings.map {
            BadHabitRatingEntity(badHabitId = badHabitId, ratingValue = it.ratingValue, date = it.date)
        }.forEach {
            if (it.ratingValue == 0) {
                // If the rating value is 0, we want to remove it from the DB.
                // Only attempt to delete if it's an existing rating (has a non-zero ID).
                if (it.id != 0L) badHabitRatingDao.deleteRatingByHabitIdAndDate(it.id, it.date)
                // If rating.id is 0L (new rating) and value is 0, we simply do not insert it.
            } else {
                // If rating value is not 0, insert/update it in the DB.
                // The DataMapper is crucial here to convert the domain model to the entity.
                badHabitRatingDao.insertRating(it)
            }
        }
    }

    override suspend fun deleteBadHabitById(id: Long) = badHabitsDao.deleteById(id)

    override suspend fun getBadHabitById(id: Long): BadHabit =
        dataMapper.mapToDomain(badHabitsDao.getBadHabitById(id))
}
