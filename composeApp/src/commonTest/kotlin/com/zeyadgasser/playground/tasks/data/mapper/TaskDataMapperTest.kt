package com.zeyadgasser.playground.tasks.data.mapper

import com.zeyadgasser.playground.tasks.TestingData
import com.zeyadgasser.playground.tasks.data.network.TaskDTO
import com.zeyadgasser.playground.tasks.domain.model.TaskDomain
import com.zeyadgasser.playground.utils.CryptoHelper
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verifyNoMoreCalls
import kotlin.test.Test
import kotlin.test.assertEquals

class TaskDataMapperTest {
    private val cryptoHelper: CryptoHelper = mock()
    private val mapper: TaskDataMapper = TaskDataMapper(cryptoHelper)

    @Test
    fun mapDTOsToDomains_validDTOs_returnsCorrectDomains() {
        every { cryptoHelper.decrypt(any()) } returns "decrypted"
        val dtos = listOf(TestingData.taskFormattedDTO)
        val expectedDomains = listOf(
            TestingData.taskDomain.copy(
                encryptedDescription = "decrypted",
                encryptedTitle = "decrypted"
            ).copy(dependencies = listOf())
        )
        val actualDomains = mapper.mapDTOsToDomains(dtos)
        assertEquals(expectedDomains, actualDomains)
        verify { cryptoHelper.decrypt(any()) }
    }

    @Test
    fun mapDTOsToDomains_emptyDTOs_returnsEmptyDomains() {
        val dtos = emptyList<TaskDTO>()
        val expectedDomains = emptyList<TaskDomain>()
        val actualDomains = mapper.mapDTOsToDomains(dtos)
        assertEquals(expectedDomains, actualDomains)
        verifyNoMoreCalls(cryptoHelper)
    }
}