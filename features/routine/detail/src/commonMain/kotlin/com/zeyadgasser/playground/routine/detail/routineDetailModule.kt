package com.zeyadgasser.playground.routine.detail

import com.zeyadgasser.playground.architecture.di.COMPUTATION
import com.zeyadgasser.playground.routine.detail.viewmodel.DeleteRoutineDetailInputHandler
import com.zeyadgasser.playground.routine.detail.viewmodel.LoadRoutineDetailInputHandler
import com.zeyadgasser.playground.routine.detail.viewmodel.NavigationInputHandler
import com.zeyadgasser.playground.routine.detail.viewmodel.RoutineDetailState
import com.zeyadgasser.playground.routine.detail.viewmodel.RoutineDetailViewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val routineDetailModule = module {
    single<RoutineDetailState> { RoutineDetailState.InitialState }
    single { LoadRoutineDetailInputHandler(get(), get()) }
    single { DeleteRoutineDetailInputHandler(get()) }
    single { NavigationInputHandler }
    factory { RoutineDetailViewModel(get(), get(), get(), get(), get(named(COMPUTATION))) }
}
