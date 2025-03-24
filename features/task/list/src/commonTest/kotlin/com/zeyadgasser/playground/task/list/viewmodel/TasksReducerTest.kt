package com.zeyadgasser.playground.task.list.viewmodel

import com.zeyadgasser.playground.task.list.TestingData
import com.zeyadgasser.playground.task.list.viewmodel.TasksState.ErrorState
import com.zeyadgasser.playground.task.list.viewmodel.TasksState.InitialState
import com.zeyadgasser.playground.task.list.viewmodel.TasksState.SuccessState
import kotlin.test.Test
import kotlin.test.assertEquals

class TasksReducerTest {
    private val reducer: TasksReducer = TasksReducer()
    private val allList = listOf(TestingData.taskPM, TestingData.taskPM2)
    private val upcomingList = listOf(TestingData.taskPM, TestingData.taskPM2)

    @Test
    fun `reduce LoadingResult with InitialState to InitialState`() {
        val result = LoadingResult(true)
        val expected = InitialState(true)
        val actual = reducer.reduce(result, InitialState(false))
        assertEquals(expected, actual)
    }

    @Test
    fun `reduce LoadTasksResult with InitialState to SuccessState`() {
        val result = LoadTasksResult(allList, upcomingList)
        val expected = SuccessState(result.allTasks, result.upcomingTasks, true)
        val actual = reducer.reduce(result, InitialState(true))
        assertEquals(expected, actual)
    }

    @Test
    fun `reduce ErrorResult with InitialState to ErrorState`() {
        val result = ErrorResult("Error!")
        val expected = ErrorState("Error!", true)
        val actual = reducer.reduce(result, InitialState(true))
        assertEquals(expected, actual)
    }

    @Test
    fun `reduce LoadingResult with ErrorState to ErrorState`() {
        val result = LoadingResult(false)
        val expected = ErrorState("Error!", false)
        val actual = reducer.reduce(result, ErrorState("Error!", true))
        assertEquals(expected, actual)
    }

    @Test
    fun `reduce LoadTasksResult with ErrorState to SuccessState`() {
        val result = LoadTasksResult(allList, upcomingList)
        val expected = SuccessState(result.allTasks, result.upcomingTasks, true)
        val actual = reducer.reduce(result, ErrorState("Error!", true))
        assertEquals(expected, actual)
    }

    @Test
    fun `reduce ErrorResult with ErrorState to ErrorState`() {
        val result = ErrorResult("Error!")
        val expected = ErrorState("Error!", true)
        val actual = reducer.reduce(result, ErrorState("Error!", true))
        assertEquals(expected, actual)
    }

    @Test
    fun `reduce LoadingResult with SuccessState to SuccessState`() {
        val result = LoadingResult(true)
        val expected = SuccessState(allList, upcomingList, true)
        val actual = reducer.reduce(result, SuccessState(allList, upcomingList, false))
        assertEquals(expected, actual)
    }

    @Test
    fun `reduce LoadTasksResult with SuccessState to SuccessState`() {
        val result = LoadTasksResult(allList, upcomingList)
        val expected = SuccessState(result.allTasks, result.upcomingTasks, true)
        val actual = reducer.reduce(result, SuccessState(allList, result.upcomingTasks, true))
        assertEquals(expected, actual)
    }

    @Test
    fun `reduce ErrorResult with SuccessState to ErrorState`() {
        val result = ErrorResult("Error!")
        val expected = ErrorState("Error!", false)
        val actual = reducer.reduce(result, SuccessState(allList, upcomingList, false))
        assertEquals(expected, actual)
    }
}