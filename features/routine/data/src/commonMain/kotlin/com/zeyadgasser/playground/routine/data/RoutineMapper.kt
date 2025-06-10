package com.zeyadgasser.playground.routine.data

import com.zeyadgasser.playground.routine.data.db.RoutineEntity
import com.zeyadgasser.playground.routine.data.db.RoutineWithRatings
import com.zeyadgasser.playground.routine.domain.model.Routine
import com.zeyadgasser.playground.routine.domain.model.RoutineRating

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
        ratings = emptyList(),
        category = entity.category
    )

    // Entity → Domain (for passing to UseCases / UI)
    fun toDomain(entity: RoutineWithRatings): Routine = with(entity) {
        Routine(
            id = routine.id,
            name = routine.name,
            type = routine.type,
            startTime = routine.startTime.toString(),
            endTime = routine.endTime.toString(),
            description = routine.description,
            completed = routine.completed,
            ratings = ratings.map { rating -> RoutineRating(rating.ratingValue, rating.date) },
            category = routine.category
        )
    }

    // List<Entity> → List<Domain>
    fun toDomainList(entities: List<RoutineEntity>): List<Routine> = entities.map { toDomain(it) }

    // List<Domain> → List<Entity>
    fun toEntityList(domains: List<Routine>): List<RoutineEntity> = domains.map { toEntity(it) }
}
