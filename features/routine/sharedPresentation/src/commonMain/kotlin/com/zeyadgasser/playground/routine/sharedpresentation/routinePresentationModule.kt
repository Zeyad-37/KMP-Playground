package com.zeyadgasser.playground.routine.sharedpresentation

import org.koin.dsl.module

val routinePresentationModule = module {
    single { RoutinePresentationMapper }
}
