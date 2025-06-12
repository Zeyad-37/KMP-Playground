package com.zeyadgasser.playground.routine.data.di

import com.zeyadgasser.playground.routine.data.db.RoutinesDatabase
import com.zeyadgasser.playground.routine.data.db.getRoutineDatabase
import org.koin.dsl.module

val desktopRoutineSharedDataModule = module {
    single<RoutinesDatabase> { getRoutineDatabase() }
}
