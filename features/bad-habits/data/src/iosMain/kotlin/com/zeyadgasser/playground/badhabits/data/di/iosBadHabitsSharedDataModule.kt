package com.zeyadgasser.playground.badhabits.data.di

import com.zeyadgasser.playground.badhabits.data.BadHabitsDatabase
import com.zeyadgasser.playground.badhabits.data.db.getPersistentDatabase
import org.koin.dsl.module

val iosBadHabitsSharedDataModule = module {
    single<BadHabitsDatabase> { getPersistentDatabase() }
}
