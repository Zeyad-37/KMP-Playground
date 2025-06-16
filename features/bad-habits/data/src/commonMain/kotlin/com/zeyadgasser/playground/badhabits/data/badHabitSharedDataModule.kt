package com.zeyadgasser.playground.badhabits.data

import com.zeyadgasser.playground.badhabits.domain.BadHabitsRepository
import org.koin.dsl.module

val badHabitsSharedDataModule = module {
    single { DataBadHabitsMapper }
    single { get<BadHabitsDatabase>().badHabitsDAO() }
    single { get<BadHabitsDatabase>().badHabitRatingDAO() }
    single<BadHabitsRepository> { BadHabitsRepositoryImpl(get(), get(), get()) }
}
