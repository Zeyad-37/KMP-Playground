package com.zeyadgasser.playground.badhabits.sharedpresentation

import org.koin.dsl.module

val badHabitsSharedPresentationModule = module {
    single { BadHabitsPresentationMapper }
}