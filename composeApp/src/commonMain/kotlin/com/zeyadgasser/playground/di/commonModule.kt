package com.zeyadgasser.playground.di

import com.zeyadgasser.playground.networking.KtorHttpClient
import com.zeyadgasser.playground.tasks.data.TaskDataMapper
import com.zeyadgasser.playground.tasks.data.TaskRepositoryImpl
import com.zeyadgasser.playground.tasks.data.db.PlaygroundDataBase
import com.zeyadgasser.playground.tasks.data.network.TasksAPI
import com.zeyadgasser.playground.tasks.domain.TaskRepository
import com.zeyadgasser.playground.tasks.domain.usecase.CheckTaskUseCase
import com.zeyadgasser.playground.tasks.domain.usecase.GetTasksUseCase
import com.zeyadgasser.playground.tasks.domain.usecase.GetUpcomingTasksUseCase
import com.zeyadgasser.playground.tasks.presentation.detail.viewmodel.TaskDetailState
import com.zeyadgasser.playground.tasks.presentation.detail.viewmodel.TaskDetailViewModel
import com.zeyadgasser.playground.tasks.presentation.list.viewmodel.TasksReducer
import com.zeyadgasser.playground.tasks.presentation.list.viewmodel.TasksState
import com.zeyadgasser.playground.tasks.presentation.list.viewmodel.TasksViewModel
import com.zeyadgasser.playground.tasks.sharedPresentation.TaskPresentationMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val IO = "io"
const val COMPUTATION = "default"
val commonModule = module { // todo separate into separate modules
    single(named(IO)) { Dispatchers.IO }
    single(named(COMPUTATION)) { Dispatchers.Default }
    single { KotlinLogging.logger("Networking") }
    single { TaskDataMapper() }
    single { KtorHttpClient.json() }
    single { KtorHttpClient.httpClient(get(), get()) }
    single { TasksAPI(get()) }
    single { PlaygroundDataBase(get()) }
//    single<TaskRepository> { TaskRepositoryImpl(get(), get(), get(), get(named(IO))) }
    single<TaskRepository> { TaskRepositoryImpl(get(), get(), get(named(IO))) }
    single { GetUpcomingTasksUseCase() }
    single { CheckTaskUseCase(get()) }
    single { GetTasksUseCase(get()) }
    single { TasksReducer() }
    single { TaskPresentationMapper }
    single<TasksState> { TasksState.InitialState(false) }
    single<TaskDetailState> { TaskDetailState.InitialState(false, "") }
    factory { TasksViewModel(get(), get(), get(), get(), get(), get(), get(named(COMPUTATION))) }
    factory { TaskDetailViewModel(get(), get(), get(), get(named(COMPUTATION))) }
}
