package com.zeyadgasser.playground.task.detail.di

import com.zeyadgasser.playground.architecture.di.COMPUTATION
import com.zeyadgasser.playground.task.detail.viewmodel.TaskDetailState
import com.zeyadgasser.playground.task.detail.viewmodel.TaskDetailViewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val taskDetailModule = module {
    single<TaskDetailState> { TaskDetailState.InitialState(false, "") }
    factory { TaskDetailViewModel(get(), get(), get(), get(named(COMPUTATION))) }
}
