package com.zeyadgasser.playground.tasks.presentation.list.viewmodel

import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.architecture.presentation.ViewModel
import com.zeyadgasser.playground.tasks.domain.usecase.CheckTaskUseCase
import com.zeyadgasser.playground.tasks.domain.usecase.GetTasksUseCase
import com.zeyadgasser.playground.tasks.domain.usecase.GetUpcomingTasksUseCase
import com.zeyadgasser.playground.tasks.sharedPresentation.TaskPM
import com.zeyadgasser.playground.tasks.sharedPresentation.TaskPresentationMapper
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
//    @Dispatcher(PlaygroundDispatchers.Default) coroutineDispatcher: CoroutineDispatcher,
) : ViewModel<TasksInput, TasksResult, TasksState, TasksEffect>(
    initialState,
    reducer,
    Dispatchers.Default
) {

    override fun resolve(input: TasksInput, state: TasksState): Flow<Result> =
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
