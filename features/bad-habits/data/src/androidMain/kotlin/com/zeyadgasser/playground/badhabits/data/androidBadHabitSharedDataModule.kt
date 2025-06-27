package com.zeyadgasser.playground.badhabits.data

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidBadHabitSharedDataModule = module {
    single<BadHabitsDatabase> { appDatabase(androidContext()) }
}
