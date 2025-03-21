package com.zeyadgasser.playground.tasks.domain.usecase

import com.zeyadgasser.playground.tasks.TestingData
import com.zeyadgasser.playground.tasks.domain.TaskRepository
import com.zeyadgasser.playground.tasks.domain.model.Operation
import com.zeyadgasser.playground.tasks.domain.model.Value
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifyNoMoreCalls
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CheckTaskUseCaseTest {
    private val taskRepository: TaskRepository = mock()
    private lateinit var checkTaskUseCase: CheckTaskUseCase

    @BeforeTest
    fun setUp() {
        checkTaskUseCase = CheckTaskUseCase(taskRepository)
    }

    @Test
    fun invokeSuccess() = runTest {
        val inputTask = TestingData.taskDomain.copy(dependencies = emptyList())
        val expected = Operation(true) to Value(true)
        everySuspend { taskRepository.insertTask(inputTask.copy(done = true)) } returns true
        assertEquals(expected, checkTaskUseCase.invoke(inputTask))
        verifySuspend { taskRepository.insertTask(inputTask.copy(done = true)) }
    }

    @Test
    fun invokeFail() = runTest {
        val inputTask = TestingData.taskDomain.copy(dependencies = emptyList())
        val taskDomain = TestingData.taskDomain
        val expected = Operation(false) to Value(false)
        everySuspend { taskRepository.insertTask(inputTask.copy(done = true)) } returns true
        assertEquals(expected, checkTaskUseCase.invoke(taskDomain))
        verifyNoMoreCalls(taskRepository)
    }
}
