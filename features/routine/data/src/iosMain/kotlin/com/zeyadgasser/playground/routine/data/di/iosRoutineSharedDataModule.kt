package com.zeyadgasser.playground.routine.data.di

import com.zeyadgasser.playground.routine.data.db.RoutinesDatabase
import com.zeyadgasser.playground.routine.data.db.getPersistentDatabase
import org.koin.dsl.module

val iosRoutineSharedDataModule = module {
    single<RoutinesDatabase> { getPersistentDatabase() }
}
