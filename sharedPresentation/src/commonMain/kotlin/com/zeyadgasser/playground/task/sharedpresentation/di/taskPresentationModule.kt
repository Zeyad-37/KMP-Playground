package com.zeyadgasser.playground.task.sharedpresentation.di

import com.zeyadgasser.playground.task.sharedpresentation.mapper.TaskPresentationMapper
import org.koin.dsl.module

val taskPresentationModule = module {
    single { TaskPresentationMapper() }
}
