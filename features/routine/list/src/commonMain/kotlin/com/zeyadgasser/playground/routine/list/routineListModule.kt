package com.zeyadgasser.playground.routine.list

import com.zeyadgasser.playground.architecture.presentation.InputHandler
import com.zeyadgasser.playground.routine.domain.CheckRoutineUseCase
import com.zeyadgasser.playground.routine.list.viewmodel.LoadRoutineListInput
import com.zeyadgasser.playground.routine.list.viewmodel.LoadRoutineListInputHandler
import com.zeyadgasser.playground.routine.list.viewmodel.RateRoutineInputHandler
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListState
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListViewModel
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineRatedInput
import com.zeyadgasser.playground.routine.list.viewmodel.RoutinesReducer
import com.zeyadgasser.playground.routine.sharedpresentation.RoutinePresentationMapper
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val routineListModule = module {
    single { CheckRoutineUseCase(get()) }
    single<RoutineListState> { RoutineListState.InitialState(false) }
    single { RoutinePresentationMapper }
    single { RoutinesReducer }
    single<InputHandler<LoadRoutineListInput, RoutineListState>>(named("loadRoutineListInputHandler")) {
        LoadRoutineListInputHandler(get(), get())
    }
    single<InputHandler<RoutineRatedInput, RoutineListState>>(named("RateRoutineInputHandler")) {
        RateRoutineInputHandler(get(), get(), get())
    }
    viewModel { RoutineListViewModel(get(), get(), get(), get(), get(), get()) }
}
