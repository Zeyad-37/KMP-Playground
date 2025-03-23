package com.zeyadgasser.playground.tasks.presentation.detail.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.architecture.presentation.ViewModel
import com.zeyadgasser.playground.domain.TaskRepository
import com.zeyadgasser.playground.tasks.presentation.detail.viewmodel.TaskDetailState.InitialState
import com.zeyadgasser.playground.tasks.presentation.detail.viewmodel.TaskDetailState.SuccessState
import com.zeyadgasser.playground.tasks.sharedPresentation.mapper.TaskPresentationMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class TaskDetailViewModel(
    private val taskRepository: TaskRepository,
    private val taskPresentationMapper: TaskPresentationMapper,
    initialState: TaskDetailState = InitialState(false, ""),
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel<TaskDetailInput, Result, TaskDetailState, TaskDetailEffect>(
    initialState, dispatcher = dispatcher
), ScreenModel {

    override suspend fun resolve(input: TaskDetailInput, state: TaskDetailState): Flow<Result> =
        when (input) {
            is LoadTaskInput -> onLoadTask(input.taskId)
            BackButtonTappedInput -> flowOf(GoBackEffect)
        }

    private fun onLoadTask(taskId: String): Flow<Result> = flow {
        if (taskId.isNotEmpty()) {
            emit(InitialState(true, taskId))
            emit(
                SuccessState(
                    taskPresentationMapper.mapDomainToPresentation(taskRepository.getTask(taskId)),
                    false
                )
            )
        }
    }
}
