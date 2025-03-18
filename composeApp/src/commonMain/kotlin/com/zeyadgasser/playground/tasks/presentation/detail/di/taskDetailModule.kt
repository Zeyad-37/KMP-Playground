package com.zeyadgasser.playground.tasks.presentation.detail.di

import com.zeyadgasser.playground.di.COMPUTATION
import com.zeyadgasser.playground.tasks.presentation.detail.viewmodel.TaskDetailState
import com.zeyadgasser.playground.tasks.presentation.detail.viewmodel.TaskDetailViewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val taskDetailModule = module {
    single<TaskDetailState> { TaskDetailState.InitialState(false, "") }
    factory { TaskDetailViewModel(get(), get(), get(), get(named(COMPUTATION))) }
}
