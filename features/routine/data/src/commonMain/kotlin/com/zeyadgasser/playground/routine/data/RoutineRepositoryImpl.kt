package com.zeyadgasser.playground.routine.data

import com.zeyadgasser.playground.routine.data.db.RoutineDao
import com.zeyadgasser.playground.routine.domain.RoutineRepository
import com.zeyadgasser.playground.routine.domain.model.Routine
import kotlinx.coroutines.CoroutineDispatcher

class RoutineRepositoryImpl(
    private val routineDao: RoutineDao,
    override val ioDispatcher: CoroutineDispatcher,
) : RoutineRepository {

    private val routineMapper: RoutineMapper = RoutineMapper

    override suspend fun getAllRoutines(): List<Routine> =
        routineMapper.toDomainList(routineDao.getAllRoutines())

    override suspend fun getRoutineById(id: Long): Routine =
        routineMapper.toDomain(routineDao.getRoutineById(id))

    override suspend fun insertReplaceRoutine(routine: Routine) =
        routineDao.insertReplace(routineMapper.toEntity(routine))

    override suspend fun deleteRoutine(id: Long): Int =
        routineDao.deleteById(id)
}
