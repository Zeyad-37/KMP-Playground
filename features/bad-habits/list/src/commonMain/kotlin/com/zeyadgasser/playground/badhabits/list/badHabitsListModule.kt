package com.zeyadgasser.playground.badhabits.list

import com.zeyadgasser.playground.architecture.di.IO
import com.zeyadgasser.playground.badhabits.list.viewmodel.BadHabitListState
import com.zeyadgasser.playground.badhabits.list.viewmodel.BadHabitListViewModel
import com.zeyadgasser.playground.badhabits.list.viewmodel.LoadBadHabitListInputHandler
import com.zeyadgasser.playground.badhabits.list.viewmodel.NavigationInputHandler
import com.zeyadgasser.playground.badhabits.list.viewmodel.RateBadHabitInputHandler
import com.zeyadgasser.playground.badhabits.sharedpresentation.BadHabitsPresentationMapper
import org.koin.core.qualifier.named
import org.koin.dsl.module

val badHabitsListModule = module {
    single<BadHabitListState> { BadHabitListState.InitialState(false) }
    single { BadHabitsPresentationMapper }
    single { LoadBadHabitListInputHandler(get(), get()) }
    single { RateBadHabitInputHandler(get(), get(), get()) }
    single { NavigationInputHandler }
    factory { BadHabitListViewModel(get(), get(), get(), get(), get(named(IO))) }
}
