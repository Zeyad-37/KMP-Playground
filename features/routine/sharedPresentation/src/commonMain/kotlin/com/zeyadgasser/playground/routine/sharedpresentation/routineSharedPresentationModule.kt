package com.zeyadgasser.playground.routine.sharedpresentation

import org.koin.dsl.module

val routineSharedPresentationModule = module {
    single { RoutinePresentationMapper }
}
