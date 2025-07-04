package com.zeyadgasser.playground.task.list.viewmodel

import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.architecture.presentation.ViewModel
import com.zeyadgasser.playground.task.domain.usecase.CheckTaskUseCase
import com.zeyadgasser.playground.task.domain.usecase.GetTasksUseCase
import com.zeyadgasser.playground.task.domain.usecase.GetUpcomingTasksUseCase
import com.zeyadgasser.playground.task.sharedpresentation.mapper.TaskPresentationMapper
import com.zeyadgasser.playground.task.sharedpresentation.model.TaskPM
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.onStart

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class TasksViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val checkTaskUseCase: CheckTaskUseCase,
    private val getUpcomingTasks: GetUpcomingTasksUseCase,
    private val taskPresentationMapper: TaskPresentationMapper,
    initialState: TasksState,
    reducer: TasksReducer,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel<TasksInput, TasksResult, TasksState, TasksEffect>(initialState, reducer, coroutineDispatcher) {

    override suspend fun resolve(input: TasksInput, state: TasksState): Flow<Result> =
        when (input) {
            LoadTasksInput -> onLoadTasks()
            is TaskCheckedInput -> onTaskChecked(input.task)
            is TaskClickedInput -> flowOf(GoToTaskDetailsEffect(input.taskId))
            ShowDialogInput -> flowOf(ShowDialogEffect)
            HideDialogInput -> flowOf(HideDialogEffect)
        }

    private fun onLoadTasks(): Flow<Result> = getTasksUseCase.invoke()
        .flatMapConcat { tasks ->
            flowOf(
                LoadTasksResult(
                    allTasks = taskPresentationMapper.mapDomainToPresentation(tasks),
                    upcomingTasks =
                        taskPresentationMapper.mapDomainToPresentation(getUpcomingTasks.invoke(tasks)),
                ),
                LoadingResult(false)
            )
        }.onEmpty { emit(LoadingResult(false)) }
        .onStart { emit(LoadingResult(true)) }
        .makeCancellable(LoadTasksInput::class, LoadingResult(false))

    private fun onTaskChecked(task: TaskPM): Flow<Result> = flow {
        emit(LoadingResult(true))
        checkTaskUseCase.invoke(taskPresentationMapper.mapPresentationToDomain(task)).let { pair ->
            if (!pair.first.success) {
                emit(CantCheckTaskEffect)
                emit(LoadingResult(false))
            }
        }
    }
}
