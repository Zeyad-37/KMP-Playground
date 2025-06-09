package com.zeyadgasser.playground.task.detail.di

import com.zeyadgasser.playground.architecture.di.COMPUTATION
import com.zeyadgasser.playground.architecture.presentation.InputHandler
import com.zeyadgasser.playground.task.detail.viewmodel.BackButtonTappedInput
import com.zeyadgasser.playground.task.detail.viewmodel.BackButtonTappedInputHandler
import com.zeyadgasser.playground.task.detail.viewmodel.LoadTaskInput
import com.zeyadgasser.playground.task.detail.viewmodel.LoadTaskInputHandler
import com.zeyadgasser.playground.task.detail.viewmodel.TaskDetailInput
import com.zeyadgasser.playground.task.detail.viewmodel.TaskDetailState
import com.zeyadgasser.playground.task.detail.viewmodel.TaskDetailViewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.reflect.KClass

val taskDetailModule = module {
    single<TaskDetailState> { TaskDetailState.InitialState(false, "") }
    single<InputHandler<*, TaskDetailState>>(named("LoadTaskInputHandler")) { LoadTaskInputHandler(get(), get()) }
    single<InputHandler<*, TaskDetailState>>(named("BackButtonTappedInputHandler")) { BackButtonTappedInputHandler() }
    single<Map<KClass<out TaskDetailInput>, InputHandler<*, TaskDetailState>>> {
        mapOf<KClass<out TaskDetailInput>, InputHandler<*, TaskDetailState>>(
            LoadTaskInput::class to get<InputHandler<*, TaskDetailState>>(named("LoadTaskInputHandler")),
            BackButtonTappedInput::class to get<InputHandler<*, TaskDetailState>>(named("BackButtonTappedInputHandler")),
        )
    }
    factory { TaskDetailViewModel(get(), get(), get(), get(named(COMPUTATION))) }
}
