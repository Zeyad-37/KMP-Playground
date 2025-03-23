package com.zeyadgasser.playground.tasks.data.di

import com.zeyadgasser.playground.di.IO
import com.zeyadgasser.playground.tasks.data.TaskRepositoryImpl
import com.zeyadgasser.playground.tasks.data.db.PlaygroundDataBase
import com.zeyadgasser.playground.tasks.data.mapper.TaskDataMapper
import com.zeyadgasser.playground.tasks.data.network.TasksAPI
import com.zeyadgasser.playground.domain.TaskRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val taskSharedDataModule = module {
    single { TaskDataMapper(get()) }
    single { TasksAPI(get()) }
    single { PlaygroundDataBase(get(), get(named(IO))) }
    single<TaskRepository> { TaskRepositoryImpl(get(), get(), get(), get(named(IO))) }
}
