package com.zeyadgasser.playground.task.list.di

import com.zeyadgasser.playground.architecture.di.COMPUTATION
import com.zeyadgasser.playground.task.domain.usecase.CheckTaskUseCase
import com.zeyadgasser.playground.task.domain.usecase.GetTasksUseCase
import com.zeyadgasser.playground.task.domain.usecase.GetUpcomingTasksUseCase
import com.zeyadgasser.playground.task.list.viewmodel.TasksReducer
import com.zeyadgasser.playground.task.list.viewmodel.TasksState
import com.zeyadgasser.playground.task.list.viewmodel.TasksViewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val taskListModule = module {
    single { GetUpcomingTasksUseCase() }
    single { CheckTaskUseCase(get()) }
    single { GetTasksUseCase(get(), get(named("isAndroid"))) }
    single { TasksReducer() }
    single<TasksState> { TasksState.InitialState(false) }
    factory { TasksViewModel(get(), get(), get(), get(), get(), get(), get(named(COMPUTATION))) }
}
