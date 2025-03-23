package com.zeyadgasser.playground.tasks.presentation.detail.viewmodel

import app.cash.turbine.test
import com.zeyadgasser.playground.task.domain.TaskRepository
import com.zeyadgasser.playground.task.sharedpresentation.mapper.TaskPresentationMapper
import com.zeyadgasser.playground.tasks.TestingData
import com.zeyadgasser.playground.tasks.presentation.detail.viewmodel.TaskDetailState.InitialState
import com.zeyadgasser.playground.tasks.presentation.detail.viewmodel.TaskDetailState.SuccessState
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.spy
import dev.mokkery.verify
import dev.mokkery.verifyNoMoreCalls
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TaskDetailViewModelTest {
    private val initialState: InitialState = InitialState(false, "2")
    private val taskRepository: TaskRepository = mock()
    private val taskPresentationMapper: TaskPresentationMapper = spy(TaskPresentationMapper())
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var taskDetailViewModel: TaskDetailViewModel

    @BeforeTest
    fun setUp() {
        taskDetailViewModel =
            TaskDetailViewModel(taskRepository, taskPresentationMapper, initialState, testDispatcher)
    }

    @Test
    fun onLoadTask() = runTest(testDispatcher) {
        everySuspend { taskRepository.getTask("2") } returns TestingData.taskDomain2
        taskDetailViewModel.process(LoadTaskInput("2"))
        taskDetailViewModel.state.test {
            assertEquals(initialState, awaitItem())
            assertEquals(initialState.copy(isLoading = true), awaitItem())
            assertEquals(
                SuccessState(TestingData.taskPM2.copy(dependencies = listOf("2")), false),
                awaitItem()
            )
            verifySuspend { taskRepository.getTask("2") }
            verify { taskPresentationMapper.mapDomainToPresentation(TestingData.taskDomain2) }
        }
    }

    @Test
    fun onBackButtonTappedInput() = runTest(testDispatcher) {
        taskDetailViewModel.process(BackButtonTappedInput)
        taskDetailViewModel.effect.test {
            assertEquals(GoBackEffect, awaitItem())
        }
        taskDetailViewModel.state.test {
            assertEquals(initialState, awaitItem())
            verifyNoMoreCalls(taskRepository)
            verifyNoMoreCalls(taskPresentationMapper)
        }
    }
}