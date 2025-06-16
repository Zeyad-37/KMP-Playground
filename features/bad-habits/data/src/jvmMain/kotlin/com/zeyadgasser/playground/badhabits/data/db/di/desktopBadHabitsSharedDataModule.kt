package com.zeyadgasser.playground.badhabits.data.db.di

import com.zeyadgasser.playground.badhabits.data.BadHabitsDatabase
import com.zeyadgasser.playground.badhabits.data.db.db.getBadHabitsDatabase
import org.koin.dsl.module

val desktopBadHabitsSharedDataModule = module {
    single<BadHabitsDatabase> { getBadHabitsDatabase() }
}
