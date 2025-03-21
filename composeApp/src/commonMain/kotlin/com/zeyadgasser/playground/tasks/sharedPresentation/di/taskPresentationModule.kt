package com.zeyadgasser.playground.tasks.sharedPresentation.di

import com.zeyadgasser.playground.tasks.sharedPresentation.mapper.TaskPresentationMapper
import org.koin.dsl.module

val taskPresentationModule = module {
    single { TaskPresentationMapper() }
}
