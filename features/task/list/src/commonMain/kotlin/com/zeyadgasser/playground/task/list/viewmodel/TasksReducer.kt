package com.zeyadgasser.playground.task.list.viewmodel

import com.zeyadgasser.playground.architecture.presentation.Reducer
import com.zeyadgasser.playground.task.sharedpresentation.model.TaskPM
import com.zeyadgasser.playground.task.list.viewmodel.TasksState.ErrorState
import com.zeyadgasser.playground.task.list.viewmodel.TasksState.InitialState
import com.zeyadgasser.playground.task.list.viewmodel.TasksState.SuccessState

class TasksReducer : Reducer<TasksResult, TasksState> {

    override fun reduce(result: TasksResult, state: TasksState): TasksState =
        when (state) {
            is InitialState -> when (result) {
                is ErrorResult -> errorState(result.message, state.isLoading)
                is LoadingResult -> state.copy(isLoading = result.isLoading)
                is LoadTasksResult ->
                    createSuccessState(result.allTasks, result.upcomingTasks, state.isLoading)
            }

            is ErrorState -> when (result) {
                is ErrorResult -> state.copy(message = result.message, state.isLoading)
                is LoadingResult -> state.copy(isLoading = result.isLoading)
                is LoadTasksResult ->
                    createSuccessState(result.allTasks, result.upcomingTasks, state.isLoading)
            }

            is SuccessState -> when (result) {
                is ErrorResult -> errorState(result.message, state.isLoading)
                is LoadingResult -> state.copy(isLoading = result.isLoading)
                is LoadTasksResult ->
                    createSuccessState(result.allTasks, result.upcomingTasks, state.isLoading)
            }
        }

    private fun errorState(message: String, isLoading: Boolean) = ErrorState(message, isLoading)

    private fun createSuccessState(allTasks: List<TaskPM>, upcomingTasks: List<TaskPM>, isLoading: Boolean) =
        SuccessState(allTasks, upcomingTasks, isLoading)
}
