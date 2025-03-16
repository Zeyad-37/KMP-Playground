package com.zeyadgasser.playground.di

import com.zeyadgasser.playground.tasks.data.db.IOSDatabaseDriverFactory
import com.zeyadgasser.playground.tasks.data.db.DatabaseDriverFactory
import org.koin.dsl.module

val iosModule = module {
    single<DatabaseDriverFactory> { IOSDatabaseDriverFactory() }
}
