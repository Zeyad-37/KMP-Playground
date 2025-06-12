package com.zeyadgasser.playground.routine.data

import com.zeyadgasser.playground.routine.data.db.RoutinesDatabase
import com.zeyadgasser.playground.routine.domain.RoutineRepository
import com.zeyadgasser.playground.routine.domain.model.Routine
import kotlinx.coroutines.CoroutineDispatcher

class RoutineRepositoryImpl(
    private val db: RoutinesDatabase,
    override val ioDispatcher: CoroutineDispatcher,
) : RoutineRepository {

    private val routineMapper: RoutineMapper = RoutineMapper

    override suspend fun getAllRoutines(): List<Routine> =
        routineMapper.toDomainList(db.routineDao().getAllRoutines())

    override suspend fun getRoutineById(id: Long): Routine =
        routineMapper.toDomain(db.routineDao().getRoutineById(id))

    override suspend fun insertReplaceRoutine(routine: Routine) =
        db.routineDao().insertReplace(routineMapper.toEntity(routine))

    override suspend fun deleteRoutine(id: Long): Int =
        db.routineDao().deleteById(id)
}
