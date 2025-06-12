package com.zeyadgasser.playground.routine.list

import com.zeyadgasser.playground.architecture.di.IO
import com.zeyadgasser.playground.routine.domain.CheckRoutineUseCase
import com.zeyadgasser.playground.routine.list.viewmodel.LoadRoutineListInputHandler
import com.zeyadgasser.playground.routine.list.viewmodel.RateRoutineInputHandler
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListState
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListViewModel
import com.zeyadgasser.playground.routine.list.viewmodel.RoutinesReducer
import com.zeyadgasser.playground.routine.sharedpresentation.RoutinePresentationMapper
import org.koin.core.qualifier.named
import org.koin.dsl.module

val routineListModule = module {
    single { CheckRoutineUseCase(get()) }
    single<RoutineListState> { RoutineListState.InitialState(false) }
    single { RoutinePresentationMapper }
    single { RoutinesReducer }
    single { LoadRoutineListInputHandler(get(), get()) }
    single { RateRoutineInputHandler(get(), get(), get()) }
    factory { RoutineListViewModel(get(), get(), get(), get(), get(named(IO))) }
}
