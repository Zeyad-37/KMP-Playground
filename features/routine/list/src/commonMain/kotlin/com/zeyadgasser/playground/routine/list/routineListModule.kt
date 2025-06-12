package com.zeyadgasser.playground.routine.list

import com.zeyadgasser.playground.routine.domain.CheckRoutineUseCase
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListState
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListViewModel
import com.zeyadgasser.playground.routine.list.viewmodel.RoutinesReducer
import com.zeyadgasser.playground.routine.sharedpresentation.RoutinePresentationMapper
import org.koin.dsl.module

val routineListModule = module {
    single { CheckRoutineUseCase(get()) }
    single<RoutineListState> { RoutineListState.InitialState(false) }
    single { RoutinePresentationMapper }
    single { RoutinesReducer }
    factory { RoutineListViewModel(get(), get(), get(), get(), get()) }
}
