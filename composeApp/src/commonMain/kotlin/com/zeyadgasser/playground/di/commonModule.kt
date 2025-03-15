package com.zeyadgasser.playground.di

import com.zeyadgasser.playground.networking.KtorHttpClient
import com.zeyadgasser.playground.tasks.data.TaskDataMapper
import com.zeyadgasser.playground.tasks.data.TaskRepositoryImpl
import com.zeyadgasser.playground.tasks.data.network.TasksAPI
import com.zeyadgasser.playground.tasks.domain.TaskRepository
import com.zeyadgasser.playground.tasks.presentation.detail.viewmodel.TaskDetailViewModel
import com.zeyadgasser.playground.tasks.presentation.list.viewmodel.TasksReducer
import com.zeyadgasser.playground.tasks.presentation.list.viewmodel.TasksState
import com.zeyadgasser.playground.tasks.presentation.list.viewmodel.TasksViewModel
import com.zeyadgasser.playground.tasks.sharedPresentation.TaskPresentationMapper
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val commonModule = module {
    single { Json { isLenient = true; ignoreUnknownKeys = true } }
    single { KtorHttpClient.httpClient() }
    single<TasksAPI> { TasksAPI(get()) }
//    single { SpaceDB(get()) }
    single<TaskRepository> { TaskRepositoryImpl(get(), get()) }
    single { TasksReducer() }
    single { TaskPresentationMapper }
    single { TaskDataMapper() }
    single<TasksState> { TasksState.InitialState(false) }
    factory { TasksViewModel(get(), get(), get(), get(), get(), get()) }
    factory { TaskDetailViewModel(get(), get(), get(), get()) }
}
