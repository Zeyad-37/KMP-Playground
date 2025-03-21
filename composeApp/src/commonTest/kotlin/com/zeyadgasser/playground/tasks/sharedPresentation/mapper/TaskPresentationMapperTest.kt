package com.zeyadgasser.playground.tasks.sharedPresentation.mapper

import com.zeyadgasser.playground.tasks.TestingData
import kotlin.test.Test
import kotlin.test.assertEquals

class TaskPresentationMapperTest {

    val mapper: TaskPresentationMapper = TaskPresentationMapper()

    @Test
    fun mapDomainListToPresentation() {
        val domains = listOf(TestingData.taskDomain)
        val expectedPresentations = listOf(TestingData.taskPM)
        val actualDomains = mapper.mapDomainToPresentation(domains)
        assertEquals(expectedPresentations, actualDomains)
    }

    @Test
    fun mapPresentationListToDomain() {
        val presentations = listOf(TestingData.taskPM)
        val expectedDomains = listOf(TestingData.taskDomain)
        val actualDomains = mapper.mapPresentationToDomain(presentations)
        assertEquals(expectedDomains, actualDomains)
    }
}