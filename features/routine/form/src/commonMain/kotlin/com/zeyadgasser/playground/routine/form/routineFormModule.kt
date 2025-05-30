package com.zeyadgasser.playground.routine.form

import com.zeyadgasser.playground.architecture.di.COMPUTATION
import com.zeyadgasser.playground.routine.domain.CheckRoutineUseCase
import com.zeyadgasser.playground.routine.form.viewmodel.RoutineFormState
import com.zeyadgasser.playground.routine.form.viewmodel.RoutineFormViewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val routineFormModule = module {
    single { CheckRoutineUseCase(get()) }
    single<RoutineFormState> { RoutineFormState.InitialState }
    factory { RoutineFormViewModel(get(), get(), get(named(COMPUTATION))) }
}
