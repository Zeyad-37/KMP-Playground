package com.zeyadgasser.playground.badhabits.detail

import com.zeyadgasser.playground.architecture.di.COMPUTATION
import com.zeyadgasser.playground.badhabits.detail.viewmodel.BadHabitDetailState
import com.zeyadgasser.playground.badhabits.detail.viewmodel.BadHabitDetailState.InitialState
import com.zeyadgasser.playground.badhabits.detail.viewmodel.BadHabitDetailViewModel
import com.zeyadgasser.playground.badhabits.detail.viewmodel.DeleteBadHabitInputHandler
import com.zeyadgasser.playground.badhabits.detail.viewmodel.LoadBadHabitDetailInputHandler
import com.zeyadgasser.playground.badhabits.detail.viewmodel.NavigationInputHandler
import com.zeyadgasser.playground.badhabits.sharedpresentation.BadHabitsPresentationMapper
import org.koin.core.qualifier.named
import org.koin.dsl.module

val badHabitsDetailModule = module {
    single<BadHabitDetailState> { InitialState }
    single { BadHabitsPresentationMapper }
    single { LoadBadHabitDetailInputHandler(get(), get()) }
    single { DeleteBadHabitInputHandler(get()) }
    single { NavigationInputHandler }
    factory { BadHabitDetailViewModel(get(), get(), get(), get(), get(named(COMPUTATION))) }
}
