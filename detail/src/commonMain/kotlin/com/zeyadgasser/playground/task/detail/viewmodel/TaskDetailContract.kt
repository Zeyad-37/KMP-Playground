package com.zeyadgasser.playground.task.detail.viewmodel

import com.zeyadgasser.playground.architecture.presentation.Effect
import com.zeyadgasser.playground.architecture.presentation.Input
import com.zeyadgasser.playground.architecture.presentation.State
import com.zeyadgasser.playground.task.sharedpresentation.model.TaskPM

sealed class TaskDetailInput : Input
data class LoadTaskInput(val taskId: String) : TaskDetailInput()
data object BackButtonTappedInput : TaskDetailInput()

sealed class TaskDetailEffect : Effect
data object GoBackEffect : TaskDetailEffect()

sealed class TaskDetailState(open val isLoading: Boolean) : State {

    data class InitialState(override val isLoading: Boolean, val taskId: String) : TaskDetailState(isLoading)

    data class ErrorState(val message: String, override val isLoading: Boolean) : TaskDetailState(isLoading)

    data class SuccessState(val task: TaskPM, override val isLoading: Boolean) : TaskDetailState(isLoading)
}
