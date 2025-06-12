package com.zeyadgasser.playground.task.detail.viewmodel

import com.zeyadgasser.playground.architecture.presentation.InputHandler
import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.task.detail.viewmodel.TaskDetailState.InitialState
import com.zeyadgasser.playground.task.detail.viewmodel.TaskDetailState.SuccessState
import com.zeyadgasser.playground.task.domain.TaskRepository
import com.zeyadgasser.playground.task.sharedpresentation.mapper.TaskPresentationMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class LoadTaskInputHandler(
    private val taskRepository: TaskRepository,
    private val taskPresentationMapper: TaskPresentationMapper,
) : InputHandler<LoadTaskInput, TaskDetailState> {
    override suspend fun invoke(input: LoadTaskInput, state: TaskDetailState): Flow<Result> = flow {
        if (input.taskId.isNotEmpty()) {
            emit(InitialState(true, input.taskId))
            emit(
                SuccessState(
                    taskPresentationMapper.mapDomainToPresentation(taskRepository.getTask(input.taskId)),
                    false
                )
            )
        }
    }
}

class BackButtonTappedInputHandler : InputHandler<BackButtonTappedInput, TaskDetailState> {
    override suspend fun invoke(
        input: BackButtonTappedInput, state: TaskDetailState,
    ): Flow<Result> = flowOf(GoBackEffect)
}
