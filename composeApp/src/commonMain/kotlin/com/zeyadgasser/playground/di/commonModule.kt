package com.zeyadgasser.playground.di

import com.zeyadgasser.playground.networking.KtorHttpClient
import com.zeyadgasser.playground.tasks.data.TaskDataMapper
import com.zeyadgasser.playground.tasks.data.TaskRepositoryImpl
import com.zeyadgasser.playground.tasks.data.network.TasksAPI
import com.zeyadgasser.playground.tasks.domain.TaskRepository
import com.zeyadgasser.playground.tasks.domain.usecase.CheckTaskUseCase
import com.zeyadgasser.playground.tasks.domain.usecase.GetTasksUseCase
import com.zeyadgasser.playground.tasks.domain.usecase.GetUpcomingTasksUseCase
import com.zeyadgasser.playground.tasks.presentation.detail.viewmodel.TaskDetailViewModel
import com.zeyadgasser.playground.tasks.presentation.list.viewmodel.TasksReducer
import com.zeyadgasser.playground.tasks.presentation.list.viewmodel.TasksState
import com.zeyadgasser.playground.tasks.presentation.list.viewmodel.TasksViewModel
import com.zeyadgasser.playground.tasks.sharedPresentation.TaskPresentationMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val commonModule = module {
    single { Dispatchers.IO }
    single { TaskDataMapper() }
    single { Json { isLenient = true; ignoreUnknownKeys = true } }
    single { KtorHttpClient.httpClient() }
    single { TasksAPI(get()) }
    single<TaskRepository> { TaskRepositoryImpl(get(), get(), get()) }
    single { GetUpcomingTasksUseCase() }
    single { CheckTaskUseCase(get()) }
    single { GetTasksUseCase(taskRepository =  get()) }
    single { TasksReducer() }
    single { TaskPresentationMapper }
    single<TasksState> { TasksState.InitialState(false) }
    factory { TasksViewModel(get(), get(), get(), get(), get(), get()) }
    factory { TaskDetailViewModel(get(), get(), get(), get()) }
}
