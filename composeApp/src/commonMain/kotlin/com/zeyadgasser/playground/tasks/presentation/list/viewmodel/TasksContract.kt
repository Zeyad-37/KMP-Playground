package com.zeyadgasser.playground.tasks.presentation.list.viewmodel

import com.zeyadgasser.playground.architecture.presentation.Effect
import com.zeyadgasser.playground.architecture.presentation.Input
import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.architecture.presentation.State
import com.zeyadgasser.playground.task.sharedpresentation.model.TaskPM

sealed class TasksInput : Input
data object LoadTasksInput : TasksInput()
data class TaskCheckedInput(val task: TaskPM) : TasksInput()
data class TaskClickedInput(val taskId: String) : TasksInput()
data object ShowDialogInput : TasksInput()
data object HideDialogInput : TasksInput()

sealed class TasksResult : Result
data class LoadTasksResult(val allTasks: List<TaskPM>, val upcomingTasks: List<TaskPM>) : TasksResult()
data class ErrorResult(val message: String) : TasksResult()
data class LoadingResult(val isLoading: Boolean) : TasksResult() // todo convert to object

sealed class TasksEffect : Effect
data object CantCheckTaskEffect : TasksEffect()
data class GoToTaskDetailsEffect(val taskId: String) : TasksEffect()
data object ShowDialogEffect : TasksEffect()
data object HideDialogEffect : TasksEffect()

sealed class TasksState(
    open val isLoading: Boolean,
    open val allTasks: List<TaskPM>,
    open val upcomingTasks: List<TaskPM>,
) : State {
    data class InitialState(override val isLoading: Boolean) : TasksState(isLoading, emptyList(), emptyList())

    data class ErrorState(
        val message: String,
        override val isLoading: Boolean,
    ) : TasksState(isLoading, emptyList(), emptyList())

    data class SuccessState(
        override val allTasks: List<TaskPM>,
        override val upcomingTasks: List<TaskPM>,
        override val isLoading: Boolean,
    ) : TasksState(isLoading, allTasks, upcomingTasks)
}
