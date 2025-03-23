package com.zeyadgasser.playground.domain.usecase

import app.cash.turbine.test
import com.zeyadgasser.playground.domain.TaskRepository
import com.zeyadgasser.playground.domain.TestingData
import com.zeyadgasser.playground.domain.model.TaskDomain
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetTasksUseCaseTest {
    private lateinit var getTasksUseCase: GetTasksUseCase
    private val taskRepository: TaskRepository = mock()

    @BeforeTest
    fun setUp() {
        getTasksUseCase = GetTasksUseCase(taskRepository, true)
    }

    @Test
    fun `invoke returns tasks from repository`() = runTest {
        val expectedTasks = listOf(
            TestingData.taskDomain2.copy(
                encryptedDescription = "decrypted",
                encryptedTitle = "decrypted"
            ),
            TestingData.taskDomain.copy(
                encryptedDescription = "decrypted",
                encryptedTitle = "decrypted"
            ),
        )
        every { taskRepository.getTasksOfflineFirst(any()) } returns flowOf(expectedTasks)
        getTasksUseCase.invoke().test {
            assertEquals(expectedTasks, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `invoke returns empty list when repository returns empty list`() = runTest {
        val expectedTasks = emptyList<TaskDomain>()
        every { taskRepository.getTasksOfflineFirst(any()) } returns flowOf(expectedTasks)
        getTasksUseCase.invoke().test {
            assertEquals(expectedTasks, awaitItem())
            awaitComplete()
        }
    }
}
