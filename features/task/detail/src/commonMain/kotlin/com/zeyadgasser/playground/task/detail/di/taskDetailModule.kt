package com.zeyadgasser.playground.task.detail.di

import com.zeyadgasser.playground.architecture.di.COMPUTATION
import com.zeyadgasser.playground.architecture.presentation.InputHandler
import com.zeyadgasser.playground.task.detail.viewmodel.BackButtonTappedInputHandler
import com.zeyadgasser.playground.task.detail.viewmodel.LoadTaskInputHandler
import com.zeyadgasser.playground.task.detail.viewmodel.TaskDetailState
import com.zeyadgasser.playground.task.detail.viewmodel.TaskDetailViewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val taskDetailModule = module {
    single<TaskDetailState> { TaskDetailState.InitialState(false, "") }
    single<InputHandler<*, TaskDetailState>>(named("LoadTaskInputHandler")) {
        LoadTaskInputHandler(get(), get())
    }
    single<InputHandler<*, TaskDetailState>>(named("BackButtonTappedInputHandler")) { BackButtonTappedInputHandler() }
    factory {
        TaskDetailViewModel(
            get(named("LoadTaskInputHandler")),
            get(named("BackButtonTappedInputHandler")),
            get(),
            get(named(COMPUTATION))
        )
    }
}
