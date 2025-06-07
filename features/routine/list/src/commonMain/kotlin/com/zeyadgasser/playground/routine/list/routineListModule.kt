package com.zeyadgasser.playground.routine.list

import com.zeyadgasser.playground.architecture.presentation.Input
import com.zeyadgasser.playground.architecture.presentation.InputHandler
import com.zeyadgasser.playground.architecture.presentation.State
import com.zeyadgasser.playground.routine.domain.CheckRoutineUseCase
import com.zeyadgasser.playground.routine.list.viewmodel.LoadRoutineListInput
import com.zeyadgasser.playground.routine.list.viewmodel.LoadRoutineListInputHandler
import com.zeyadgasser.playground.routine.list.viewmodel.RateRoutineInputHandler
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListInput
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListState
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListViewModel
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineRatedInput
import com.zeyadgasser.playground.routine.list.viewmodel.RoutinesReducer
import com.zeyadgasser.playground.routine.sharedpresentation.RoutinePresentationMapper
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.reflect.KClass

val routineListModule = module {
    single { CheckRoutineUseCase(get()) }
    single<RoutineListState> { RoutineListState.InitialState(false) }
    single { RoutinePresentationMapper }
    single { RoutinesReducer }
    single<InputHandler<LoadRoutineListInput, RoutineListState>>(named("loadRoutineListInputHandler")) { LoadRoutineListInputHandler(get(), get()) }
    single<InputHandler<RoutineRatedInput, RoutineListState>>(named("RateRoutineInputHandler")) { RateRoutineInputHandler(get(), get(), get()) }
//    single(named("routineListInputHandlers")) {
//        //as Map<KClass<out RoutineListInput>, InputHandler<RoutineListInput, RoutineListState>>
//    }
    factory {
        RoutineListViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
//            mapOf<KClass<out RoutineListInput>, InputHandler<RoutineListInput, RoutineListState>>(
//            mapOf<KClass<RoutineListInput>, InputHandler<RoutineListInput, State>>(
//                LoadRoutineListInput::class to get<InputHandler<LoadRoutineListInput, RoutineListState>>(named("loadRoutineListInputHandler")),
//                RoutineRatedInput::class to get<InputHandler<RoutineRatedInput, RoutineListState>>(named("RateRoutineInputHandler"))
//            )
            get(),
        )
    }
}
