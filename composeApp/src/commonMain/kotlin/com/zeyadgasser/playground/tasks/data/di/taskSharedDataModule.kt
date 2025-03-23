package com.zeyadgasser.playground.tasks.data.di

import com.zeyadgasser.playground.architecture.di.IO
import com.zeyadgasser.playground.database.DatabaseDriverFactory
import com.zeyadgasser.playground.task.domain.TaskRepository
import com.zeyadgasser.playground.tasks.data.TaskRepositoryImpl
import com.zeyadgasser.playground.tasks.data.db.PlaygroundDB
import com.zeyadgasser.playground.tasks.data.db.PlaygroundDAO
import com.zeyadgasser.playground.tasks.data.mapper.TaskDataMapper
import com.zeyadgasser.playground.tasks.data.network.TasksAPI
import org.koin.core.qualifier.named
import org.koin.dsl.module

val taskSharedDataModule = module {
    single { TaskDataMapper(get()) }
    single { TasksAPI(get()) }
    single { PlaygroundDAO(get(), get(named(IO))) }
    single<TaskRepository> { TaskRepositoryImpl(get(), get(), get(), get(named(IO))) }
}
