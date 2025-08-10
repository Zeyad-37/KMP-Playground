package com.zeyadgasser.playground.task.data.di

import com.zeyadgasser.playground.architecture.di.IO
import com.zeyadgasser.playground.task.data.TaskRepositoryImpl
import com.zeyadgasser.playground.task.data.db.PlaygroundDatabase
import com.zeyadgasser.playground.task.data.mapper.TaskDataMapper
import com.zeyadgasser.playground.task.data.network.TasksAPI
import com.zeyadgasser.playground.task.domain.TaskRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val taskSharedDataModule = module {
    single { TaskDataMapper(get()) }
    single { TasksAPI(get()) }
    single { get<PlaygroundDatabase>().taskDAO() }
    single<TaskRepository> { TaskRepositoryImpl(get(), get(), get(), get(named(IO))) }
}
