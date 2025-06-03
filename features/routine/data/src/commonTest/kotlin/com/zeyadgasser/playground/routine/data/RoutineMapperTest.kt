package com.zeyadgasser.playground.routine.data

import com.zeyadgasser.playground.routine.data.db.RoutineEntity
import com.zeyadgasser.playground.routine.domain.model.Routine
import kotlin.test.Test
import kotlin.test.assertEquals

class RoutineMapperTest {

    @Test
    fun `toEntity should correctly map Routine to RoutineEntity when times are Strings`() {
        // Arrange
        val domainRoutine = Routine(
            id = 1,
            name = "Morning Jog",
            type = "Exercise",
            startTime = "07:00",
            endTime = "08:00",
            description = "A refreshing morning jog.",
            completed = false,
            rating = 0,
            category = "Fitness"
        )

        // Act
        val entity = RoutineMapper.toEntity(domainRoutine)

        // Assert
        assertEquals(domainRoutine.id, entity.id)
        assertEquals(domainRoutine.name, entity.name)
        assertEquals(domainRoutine.type, entity.type)
        assertEquals(domainRoutine.startTime, entity.startTime) // Direct String comparison
        assertEquals(domainRoutine.endTime, entity.endTime)     // Direct String comparison
        assertEquals(domainRoutine.description, entity.description)
        assertEquals(domainRoutine.completed, entity.completed)
        assertEquals(domainRoutine.rating, entity.rating)
        assertEquals(domainRoutine.category, entity.category)
    }

    @Test
    fun `toDomain should correctly map RoutineEntity to Routine when times are Strings`() {
        // Arrange
        val entity = RoutineEntity(
            id = 2,
            name = "Read Book",
            type = "Leisure",
            startTime = "20:00", // startTime is now String
            endTime = "21:00",   // endTime is now String
            description = "Read a chapter of a good book.",
            completed = true,
            rating = 5,
            category = "Personal Growth"
        )

        // Act
        val domainRoutine = RoutineMapper.toDomain(entity)

        // Assert
        assertEquals(entity.id, domainRoutine.id)
        assertEquals(entity.name, domainRoutine.name)
        assertEquals(entity.type, domainRoutine.type)
        assertEquals(entity.startTime, domainRoutine.startTime) // entity.startTime.toString() is just entity.startTime
        assertEquals(entity.endTime, domainRoutine.endTime)     // entity.endTime.toString() is just entity.endTime
        assertEquals(entity.description, domainRoutine.description)
        assertEquals(entity.completed, domainRoutine.completed)
        assertEquals(entity.rating, domainRoutine.rating)
        assertEquals(entity.category, domainRoutine.category)
    }

    @Test
    fun `toDomainList should correctly map a list of RoutineEntity to a list of Routine when times are Strings`() {
        // Arrange
        val entities = listOf(
            RoutineEntity(
                id = 3,
                name = "Work Project",
                type = "Work",
                startTime = "09:00", // String
                endTime = "17:00",   // String
                description = "Focus on project tasks.",
                completed = false,
                rating = 0,
                category = "Productivity"
            ),
            RoutineEntity(
                id = 4,
                name = "Cook Dinner",
                type = "Chore",
                startTime = "18:30", // String
                endTime = "19:30",   // String
                description = "Prepare a healthy meal.",
                completed = true,
                rating = 4,
                category = "Home"
            )
        )

        // Act
        val domainList = RoutineMapper.toDomainList(entities)

        // Assert
        assertEquals(entities.size, domainList.size)
        for (i in entities.indices) {
            assertEquals(entities[i].id, domainList[i].id)
            assertEquals(entities[i].name, domainList[i].name)
            assertEquals(entities[i].type, domainList[i].type)
            assertEquals(entities[i].startTime, domainList[i].startTime)
            assertEquals(entities[i].endTime, domainList[i].endTime)
            assertEquals(entities[i].description, domainList[i].description)
            assertEquals(entities[i].completed, domainList[i].completed)
            assertEquals(entities[i].rating, domainList[i].rating)
            assertEquals(entities[i].category, domainList[i].category)
        }
    }

    @Test
    fun `toEntityList should correctly map a list of Routine to a list of RoutineEntity when times are Strings`() {
        // Arrange
        val domains = listOf(
            Routine(
                id = 5,
                name = "Meditation",
                type = "Wellness",
                startTime = "06:30",
                endTime = "06:45",
                description = "Morning meditation session.",
                completed = true,
                rating = 5,
                category = "Mindfulness"
            ),
            Routine(
                id = 6,
                name = "Learn Kotlin",
                type = "Learning",
                startTime = "14:00",
                endTime = "15:00",
                description = "Study new Kotlin features.",
                completed = false,
                rating = 0,
                category = "Development"
            )
        )

        // Act
        val entityList = RoutineMapper.toEntityList(domains)

        // Assert
        assertEquals(domains.size, entityList.size)
        for (i in domains.indices) {
            assertEquals(domains[i].id, entityList[i].id)
            assertEquals(domains[i].name, entityList[i].name)
            assertEquals(domains[i].type, entityList[i].type)
            assertEquals(domains[i].startTime, entityList[i].startTime) // Direct String comparison
            assertEquals(domains[i].endTime, entityList[i].endTime)     // Direct String comparison
            assertEquals(domains[i].description, entityList[i].description)
            assertEquals(domains[i].completed, entityList[i].completed)
            assertEquals(domains[i].rating, entityList[i].rating)
            assertEquals(domains[i].category, entityList[i].category)
        }
    }

    @Test
    fun `toDomainList should return an empty list if input is empty`() {
        // Arrange
        val emptyEntities = emptyList<RoutineEntity>()

        // Act
        val domainList = RoutineMapper.toDomainList(emptyEntities)

        // Assert
        assertEquals(0, domainList.size)
    }

    @Test
    fun `toEntityList should return an empty list if input is empty`() {
        // Arrange
        val emptyDomains = emptyList<Routine>()

        // Act
        val entityList = RoutineMapper.toEntityList(emptyDomains)

        // Assert
        assertEquals(0, entityList.size)
    }
}
