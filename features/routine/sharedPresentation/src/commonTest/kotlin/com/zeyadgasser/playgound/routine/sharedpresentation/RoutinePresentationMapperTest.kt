package com.zeyadgasser.playgound.routine.sharedpresentation

import com.zeyadgasser.playground.routine.data.db.RoutineEntity // Adjust import if needed
import com.zeyadgasser.playground.routine.domain.model.Routine   // Adjust import if needed
import com.zeyadgasser.playground.routine.sharedpresentation.RoutinePM
import com.zeyadgasser.playground.routine.sharedpresentation.RoutinePresentationMapper
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RoutinePresentationMapperTest {

    private val sampleEntity = RoutineEntity(
        id = 1L,
        name = "Morning Exercise",
        type = "Fitness",
        startTime = "07:00",
        endTime = "07:30",
        description = "Quick workout",
        completed = false,
        rating = null,
        category = "Health"
    )

    private val sampleDomain = Routine(
        id = 2L,
        name = "Read a Book",
        type = "Leisure",
        startTime = "21:00",
        endTime = "22:00",
        description = "Fiction novel",
        completed = true,
        rating = 5,
        category = "Personal Growth"
    )

    private val samplePM = RoutinePM(
        id = 3L,
        name = "Team Meeting",
        type = "Work",
        startTime = "10:00",
        endTime = "11:00",
        description = "Project sync-up",
        completed = false,
        ratings = null,
        category = "Productivity"
    )

    // --- Single Item Mappers ---

    @Test
    fun `toDomain should map RoutineEntity to Routine correctly`() {
        val domain = RoutinePresentationMapper.toDomain(sampleEntity)

        assertEquals(sampleEntity.id, domain.id)
        assertEquals(sampleEntity.name, domain.name)
        assertEquals(sampleEntity.type, domain.type)
        assertEquals(sampleEntity.startTime, domain.startTime) // .toString() on String is itself
        assertEquals(sampleEntity.endTime, domain.endTime)     // .toString() on String is itself
        assertEquals(sampleEntity.description, domain.description)
        assertEquals(sampleEntity.completed, domain.completed)
        assertEquals(sampleEntity.rating, domain.rating)
        assertEquals(sampleEntity.category, domain.category)
    }

    @Test
    fun `toPresentation should map Routine to RoutinePM correctly`() {
        val presentation = RoutinePresentationMapper.toPresentation(sampleDomain)

        assertEquals(sampleDomain.id, presentation.id)
        assertEquals(sampleDomain.name, presentation.name)
        assertEquals(sampleDomain.type, presentation.type)
        assertEquals(sampleDomain.startTime, presentation.startTime)
        assertEquals(sampleDomain.endTime, presentation.endTime)
        assertEquals(sampleDomain.description, presentation.description)
        assertEquals(sampleDomain.completed, presentation.completed)
        assertEquals(sampleDomain.rating, presentation.ratings)
        assertEquals(sampleDomain.category, presentation.category)
    }

    @Test
    fun `fromPresentation should map RoutinePM to Routine correctly`() {
        val domain = RoutinePresentationMapper.fromPresentation(samplePM)

        assertEquals(samplePM.id, domain.id)
        assertEquals(samplePM.name, domain.name)
        assertEquals(samplePM.type, domain.type)
        assertEquals(samplePM.startTime, domain.startTime)
        assertEquals(samplePM.endTime, domain.endTime)
        assertEquals(samplePM.description, domain.description)
        assertEquals(samplePM.completed, domain.completed)
        assertEquals(samplePM.ratings, domain.rating)
        assertEquals(samplePM.category, domain.category)
    }

    @Test
    fun `fromDomain should map Routine to RoutineEntity correctly`() {
        val entity = RoutinePresentationMapper.fromDomain(sampleDomain)

        assertEquals(sampleDomain.id, entity.id)
        assertEquals(sampleDomain.name, entity.name)
        assertEquals(sampleDomain.type, entity.type)
        assertEquals(sampleDomain.startTime, entity.startTime)
        assertEquals(sampleDomain.endTime, entity.endTime)
        assertEquals(sampleDomain.description, entity.description)
        assertEquals(sampleDomain.completed, entity.completed)
        assertEquals(sampleDomain.rating, entity.rating)
        assertEquals(sampleDomain.category, entity.category)
    }

    @Test
    fun `toPresentationFromData should map RoutineEntity to RoutinePM correctly`() {
        val presentation = RoutinePresentationMapper.toPresentationFromData(sampleEntity)

        assertEquals(sampleEntity.id, presentation.id)
        assertEquals(sampleEntity.name, presentation.name)
        assertEquals(sampleEntity.type, presentation.type)
        assertEquals(sampleEntity.startTime, presentation.startTime) // .toString() on String is itself
        assertEquals(sampleEntity.endTime, presentation.endTime)     // .toString() on String is itself
        assertEquals(sampleEntity.description, presentation.description)
        assertEquals(sampleEntity.completed, presentation.completed)
        assertEquals(sampleEntity.rating, presentation.ratings)
        assertEquals(sampleEntity.category, presentation.category)
    }

    @Test
    fun `fromPresentationToData should map RoutinePM to RoutineEntity correctly`() {
        val entity = RoutinePresentationMapper.fromPresentationToData(samplePM)

        assertEquals(samplePM.id, entity.id)
        assertEquals(samplePM.name, entity.name)
        assertEquals(samplePM.type, entity.type)
        assertEquals(samplePM.startTime, entity.startTime)
        assertEquals(samplePM.endTime, entity.endTime)
        assertEquals(samplePM.description, entity.description)
        assertEquals(samplePM.completed, entity.completed)
        assertEquals(samplePM.ratings, entity.rating)
        assertEquals(samplePM.category, entity.category)
    }

    // --- List Mappers ---

    @Test
    fun `toDomainList should map list of RoutineEntity to list of Routine`() {
        val entities = listOf(sampleEntity, sampleEntity.copy(id = 10L))
        val domains = RoutinePresentationMapper.toDomainList(entities)

        assertEquals(entities.size, domains.size)
        entities.zip(domains).forEach { (entity, domain) ->
            assertEquals(entity.id, domain.id)
            assertEquals(entity.name, domain.name)
        }
    }

    @Test
    fun `toDomainList should return empty list for empty entity list`() {
        val domains = RoutinePresentationMapper.toDomainList(emptyList())
        assertTrue(domains.isEmpty())
    }

    @Test
    fun `toPresentationList should map list of Routine to list of RoutinePM`() {
        val domains = listOf(sampleDomain, sampleDomain.copy(id = 11L))
        val presentations = RoutinePresentationMapper.toPresentationList(domains)

        assertEquals(domains.size, presentations.size)
        domains.zip(presentations).forEach { (domain, presentation) ->
            assertEquals(domain.id, presentation.id)
            assertEquals(domain.name, presentation.name)
        }
    }

    @Test
    fun `toPresentationList should return empty list for empty domain list`() {
        val presentations = RoutinePresentationMapper.toPresentationList(emptyList())
        assertTrue(presentations.isEmpty())
    }


    @Test
    fun `fromPresentationList should map list of RoutinePM to list of Routine`() {
        val presentations = listOf(samplePM, samplePM.copy(id = 12L))
        val domains = RoutinePresentationMapper.fromPresentationList(presentations)

        assertEquals(presentations.size, domains.size)
        presentations.zip(domains).forEach { (pm, domain) ->
            assertEquals(pm.id, domain.id)
            assertEquals(pm.name, domain.name)
        }
    }

    @Test
    fun `fromPresentationList should return empty list for empty presentation list`() {
        val domains = RoutinePresentationMapper.fromPresentationList(emptyList())
        assertTrue(domains.isEmpty())
    }

    @Test
    fun `fromDomainList should map list of Routine to list of RoutineEntity`() {
        val domains = listOf(sampleDomain, sampleDomain.copy(id = 13L))
        val entities = RoutinePresentationMapper.fromDomainList(domains)

        assertEquals(domains.size, entities.size)
        domains.zip(entities).forEach { (domain, entity) ->
            assertEquals(domain.id, entity.id)
            assertEquals(domain.name, entity.name)
        }
    }

    @Test
    fun `fromDomainList should return empty list for empty domain list`() {
        val entities = RoutinePresentationMapper.fromDomainList(emptyList())
        assertTrue(entities.isEmpty())
    }

    @Test
    fun `toPresentationListFromData should map list of RoutineEntity to list of RoutinePM`() {
        val entities = listOf(sampleEntity, sampleEntity.copy(id = 14L))
        val presentations = RoutinePresentationMapper.toPresentationListFromData(entities)

        assertEquals(entities.size, presentations.size)
        entities.zip(presentations).forEach { (entity, presentation) ->
            assertEquals(entity.id, presentation.id)
            assertEquals(entity.name, presentation.name)
        }
    }

    @Test
    fun `toPresentationListFromData should return empty list for empty entity list`() {
        val presentations = RoutinePresentationMapper.toPresentationListFromData(emptyList())
        assertTrue(presentations.isEmpty())
    }

    @Test
    fun `fromPresentationListToData should map list of RoutinePM to list of RoutineEntity`() {
        val presentations = listOf(samplePM, samplePM.copy(id = 15L))
        val entities = RoutinePresentationMapper.fromPresentationListToData(presentations)

        assertEquals(presentations.size, entities.size)
        presentations.zip(entities).forEach { (pm, entity) ->
            assertEquals(pm.id, entity.id)
            assertEquals(pm.name, entity.name)
        }
    }

    @Test
    fun `fromPresentationListToData should return empty list for empty presentation list`() {
        val entities = RoutinePresentationMapper.fromPresentationListToData(emptyList())
        assertTrue(entities.isEmpty())
    }
}