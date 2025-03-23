package com.zeyadgasser.playground.tasks.presentation.list.viewmodel

import app.cash.turbine.test
import com.zeyadgasser.playground.task.domain.model.Operation
import com.zeyadgasser.playground.task.domain.model.Value
import com.zeyadgasser.playground.task.domain.usecase.CheckTaskUseCase
import com.zeyadgasser.playground.task.domain.usecase.GetTasksUseCase
import com.zeyadgasser.playground.task.domain.usecase.GetUpcomingTasksUseCase
import com.zeyadgasser.playground.task.sharedpresentation.mapper.TaskPresentationMapper
import com.zeyadgasser.playground.tasks.TestingData
import com.zeyadgasser.playground.tasks.presentation.list.viewmodel.TasksState.InitialState
import com.zeyadgasser.playground.tasks.presentation.list.viewmodel.TasksState.SuccessState
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verifyNoMoreCalls
import dev.mokkery.verifySuspend
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TasksViewModelTest {
    private val initialState: InitialState = InitialState(false)
    private val reducer: TasksReducer = TasksReducer()
    private val getTasksUseCase: GetTasksUseCase = mock()
    private val getUpcomingTasksUseCase: GetUpcomingTasksUseCase = mock()
    private val checkTaskUseCase: CheckTaskUseCase = mock()
    private val mapper: TaskPresentationMapper = TaskPresentationMapper()
    private val domainList = listOf(TestingData.taskDomain)
    private val pMList = listOf(TestingData.taskPM)
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var tasksViewModel: TasksViewModel

    @Test
    fun loadTasksInput() = runTest(testDispatcher) {
        tasksViewModel = TasksViewModel(
            getTasksUseCase, checkTaskUseCase, getUpcomingTasksUseCase, mapper, initialState, reducer, testDispatcher
        )
        every { getTasksUseCase.invoke() } returns flowOf(domainList)
        every { getUpcomingTasksUseCase.invoke(domainList) }.returns(domainList)
        tasksViewModel.process(LoadTasksInput)
        tasksViewModel.state.test {
            assertEquals(initialState, awaitItem())
            assertEquals(initialState.copy(isLoading = true), awaitItem())
            assertEquals(SuccessState(pMList, pMList, true), awaitItem())
            assertEquals(SuccessState(pMList, pMList, false), awaitItem())
            verify { getTasksUseCase.invoke() }
            verify { getUpcomingTasksUseCase.invoke(domainList) }
        }
    }

    @Test
    fun onTaskCheckedReturnSuccessState() = runTest(testDispatcher) {
        tasksViewModel = TasksViewModel(
            getTasksUseCase,
            checkTaskUseCase,
            getUpcomingTasksUseCase,
            mapper,
            SuccessState(pMList, pMList, false),
            reducer,
            testDispatcher,
        )
        val taskPM = TestingData.taskPM.copy(done = true)
        val task = TestingData.taskDomain.copy(done = true)
        val list = listOf(task)
        everySuspend { checkTaskUseCase.invoke(task) } returns Pair(Operation(true), Value(true))
        every { getUpcomingTasksUseCase.invoke(list) } returns list

        tasksViewModel.process(TaskCheckedInput(taskPM))
        tasksViewModel.state.test {
            assertEquals(SuccessState(pMList, pMList, false), awaitItem())
            assertEquals(SuccessState(pMList, pMList, true), awaitItem())
            verifySuspend { checkTaskUseCase.invoke(task) }
            verifyNoMoreCalls(getUpcomingTasksUseCase)
        }
    }

    @Test
    fun onTaskCheckedReturnCantCheckTaskEffect() = runTest(testDispatcher) {
        tasksViewModel = TasksViewModel(
            getTasksUseCase,
            checkTaskUseCase,
            getUpcomingTasksUseCase,
            mapper,
            SuccessState(pMList, pMList, false),
            reducer,
            testDispatcher
        )
        everySuspend { checkTaskUseCase.invoke(TestingData.taskDomain) } returns Pair(
            Operation(false),
            Value(true)
        )
        tasksViewModel.process(TaskCheckedInput(TestingData.taskPM))
        tasksViewModel.effect.test {
            assertEquals(CantCheckTaskEffect, awaitItem())
        }
        tasksViewModel.state.test {
            assertEquals(SuccessState(pMList, pMList, false), awaitItem())
            verifySuspend { checkTaskUseCase.invoke(TestingData.taskDomain) }
            verifyNoMoreCalls(getUpcomingTasksUseCase)
        }
    }
}