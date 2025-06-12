package com.zeyadgasser.playground.routine.sharedpresentation

import com.zeyadgasser.playground.routine.data.db.RoutineEntity
import com.zeyadgasser.playground.routine.domain.model.Routine
import com.zeyadgasser.playground.routine.domain.model.RoutineRating

object RoutinePresentationMapper {

    // 1. Data → Domain
    fun toDomain(entity: RoutineEntity): Routine {
        return Routine(
            id = entity.id,
            name = entity.name,
            type = entity.type,
            startTime = entity.startTime.toString(),
            endTime = entity.endTime.toString(),
            description = entity.description,
            completed = entity.completed,
            ratings = emptyList(),
            category = entity.category,
        )
    }

    // 2. Domain → Presentation
    fun toPresentation(domain: Routine): RoutinePM {
        return RoutinePM(
            id = domain.id,
            name = domain.name,
            type = domain.type,
            startTime = domain.startTime,
            endTime = domain.endTime,
            description = domain.description,
            completed = domain.completed,
            ratings = domain.ratings.map { RoutineRatingPM(it.ratingValue, it.date) },
            category = domain.category,
        )
    }

    // 3. Presentation → Domain
    fun fromPresentation(presentation: RoutinePM): Routine {
        return Routine(
            id = presentation.id,
            name = presentation.name,
            type = presentation.type,
            startTime = presentation.startTime,
            endTime = presentation.endTime,
            description = presentation.description,
            completed = presentation.completed,
            ratings = presentation.ratings.map { RoutineRating(it.ratingValue, it.date) },
            category = presentation.category,
        )
    }

    // 4. Domain → Data
    fun fromDomain(domain: Routine): RoutineEntity {
        return RoutineEntity(
            id = domain.id,
            name = domain.name,
            type = domain.type,
            startTime = domain.startTime,
            endTime = domain.endTime,
            description = domain.description,
            completed = domain.completed,
            category = domain.category
        )
    }

    // === 4. Data Layer ↔ Presentation Layer ===

    // Data → Presentation
    fun toPresentationFromData(entity: RoutineEntity): RoutinePM {
        return RoutinePM(
            id = entity.id,
            name = entity.name,
            type = entity.type,
            startTime = entity.startTime.toString(),
            endTime = entity.endTime.toString(),
            description = entity.description,
            completed = entity.completed,
            ratings = emptyList(),
            category = entity.category,
        )
    }

    // Presentation → Data
    fun fromPresentationToData(presentation: RoutinePM): RoutineEntity {
        return RoutineEntity(
            id = presentation.id,
            name = presentation.name,
            type = presentation.type,
            startTime = presentation.startTime,
            endTime = presentation.endTime,
            description = presentation.description,
            completed = presentation.completed,
            category = presentation.category
        )
    }

    // List helpers

    fun toDomainList(entities: List<RoutineEntity>): List<Routine> =
        entities.map { toDomain(it) }

    fun toPresentationList(domains: List<Routine>): List<RoutinePM> =
        domains.map { toPresentation(it) }

    fun fromPresentationList(presentations: List<RoutinePM>): List<Routine> =
        presentations.map { fromPresentation(it) }

    fun fromDomainList(domains: List<Routine>): List<RoutineEntity> =
        domains.map { fromDomain(it) }

    fun toPresentationListFromData(entities: List<RoutineEntity>): List<RoutinePM> =
        entities.map { toPresentationFromData(it) }

    fun fromPresentationListToData(presentations: List<RoutinePM>): List<RoutineEntity> =
        presentations.map { fromPresentationToData(it) }
}
