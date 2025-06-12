package com.zeyadgasser.playground.routine.data

import com.zeyadgasser.playground.architecture.di.IO
import com.zeyadgasser.playground.routine.data.db.RoutinesDatabase
import com.zeyadgasser.playground.routine.domain.RoutineRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val routineSharedDataModule = module {
    single { RoutineMapper }
    single { get<RoutinesDatabase>().routineDao() }
    single<RoutineRepository> { RoutineRepositoryImpl(get(), get(named(IO))) }
}
