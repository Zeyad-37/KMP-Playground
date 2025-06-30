package com.zeyadgasser.playground.badhabits.domain

import org.koin.dsl.module

var badHabitsDomainModule = module {
    single { GetGoodDayDatesUseCase }
    single { GetLongestStreakUseCase(get()) }
    single { GetCurrentStreakUseCase(get(), get()) }
}
