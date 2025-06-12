package com.zeyadgasser.playground.routine.data

import com.zeyadgasser.playground.routine.data.db.RoutineEntity
import com.zeyadgasser.playground.routine.domain.model.Routine

object RoutineMapper {

    // Domain → Entity (for saving/updating in Room)
    fun toEntity(domain: Routine): RoutineEntity = RoutineEntity(
        id = domain.id,
        name = domain.name,
        type = domain.type,
        startTime = domain.startTime,
        endTime = domain.endTime,
        description = domain.description,
        completed = domain.completed,
        rating = domain.rating,
        category = domain.category
    )

    // Entity → Domain (for passing to UseCases / UI)
    fun toDomain(entity: RoutineEntity): Routine = Routine(
        id = entity.id,
        name = entity.name,
        type = entity.type,
        startTime = entity.startTime.toString(),
        endTime = entity.endTime.toString(),
        description = entity.description,
        completed = entity.completed,
        rating = entity.rating,
        category = entity.category
    )

    // List<Entity> → List<Domain>
    fun toDomainList(entities: List<RoutineEntity>): List<Routine> = entities.map { toDomain(it) }

    // List<Domain> → List<Entity>
    fun toEntityList(domains: List<Routine>): List<RoutineEntity> = domains.map { toEntity(it) }
}
