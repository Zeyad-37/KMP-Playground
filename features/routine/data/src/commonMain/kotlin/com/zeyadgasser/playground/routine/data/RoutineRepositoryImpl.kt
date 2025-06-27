package com.zeyadgasser.playground.routine.data

import com.zeyadgasser.playground.routine.data.db.RoutineDao
import com.zeyadgasser.playground.routine.data.db.RoutineEntity
import com.zeyadgasser.playground.routine.data.db.RoutineRatingDao
import com.zeyadgasser.playground.routine.data.db.RoutineRatingEntity
import com.zeyadgasser.playground.routine.domain.RoutineRepository
import com.zeyadgasser.playground.routine.domain.model.Routine
import kotlinx.coroutines.CoroutineDispatcher

class RoutineRepositoryImpl(
    private val routineDao: RoutineDao,
    private val ratingDao: RoutineRatingDao,
    override val ioDispatcher: CoroutineDispatcher,
) : RoutineRepository {

    private val routineMapper: RoutineMapper = RoutineMapper

    override suspend fun getAllRoutines(): List<Routine> =
        routineMapper.toDomainList(routineDao.getAllRoutines())

    override suspend fun getRoutineById(id: Long): Routine =
        routineMapper.toDomain(routineDao.getRoutineWithRatingsById(id))

    override suspend fun insertReplaceRoutine(routine: Routine) =
        routineDao.insertReplace(routineMapper.toEntity(routine))

    override suspend fun insertRoutineWithRatings(routine: Routine) {
        // Step 1: Map Routine to RoutineEntity
        val routineEntity = RoutineEntity(
            id = routine.id,
            name = routine.name,
            type = routine.type,
            startTime = routine.startTime,
            endTime = routine.endTime,
            description = routine.description,
            category = routine.category,
            completed = routine.completed,
            remindersEnabled = routine.remindersEnabled
        )
        // Step 2: Insert the routine
        routineDao.insertReplace(routineEntity)
        // Step 3: Map ratings and insert them
        val ratingEntities = routine.ratings.map { rating ->
            RoutineRatingEntity(
                routineId = routine.id,
                ratingValue = rating.ratingValue,
                date = rating.date
            )
        }
        ratingEntities.forEach { ratingDao.insertRating(it) }
    }

    override suspend fun deleteRoutine(id: Long): Int = routineDao.deleteById(id)
}
