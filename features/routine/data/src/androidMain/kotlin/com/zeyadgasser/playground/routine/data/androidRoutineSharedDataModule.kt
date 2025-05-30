package com.zeyadgasser.playground.routine.data

import com.zeyadgasser.playground.routine.data.db.RoutinesDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidRoutineSharedDataModule = module {
    single<RoutinesDatabase> { appDatabase(androidContext()) }
}
