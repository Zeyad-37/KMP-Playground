package com.zeyadgasser.playground.database.di

import com.zeyadgasser.playground.database.DatabaseDriverFactory
import com.zeyadgasser.playground.tasks.data.db.PlaygroundDB
import org.koin.dsl.module

val dataBaseModule = module {
    single { PlaygroundDB((get() as DatabaseDriverFactory).createDriver()) }
}
