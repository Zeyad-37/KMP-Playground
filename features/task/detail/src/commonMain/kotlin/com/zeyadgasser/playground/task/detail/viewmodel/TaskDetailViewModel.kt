package com.zeyadgasser.playground.task.detail.viewmodel

import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.architecture.presentation.ViewModel
import com.zeyadgasser.playground.task.detail.viewmodel.TaskDetailState.InitialState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class TaskDetailViewModel(
    private val loadTaskInputHandler: LoadTaskInputHandler,
    private val backButtonTappedInputHandler: BackButtonTappedInputHandler,
    initialState: TaskDetailState = InitialState(false, ""),
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel<TaskDetailInput, Result, TaskDetailState, TaskDetailEffect>(
    initialState, dispatcher = dispatcher
) {
    override suspend fun resolve(input: TaskDetailInput, state: TaskDetailState): Flow<Result> =
        when (input) {
            is LoadTaskInput -> loadTaskInputHandler.invoke(input, state)
            is BackButtonTappedInput -> backButtonTappedInputHandler.invoke(input, state)
        }
}
