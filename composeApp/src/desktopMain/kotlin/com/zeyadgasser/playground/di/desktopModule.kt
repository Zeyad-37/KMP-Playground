package com.zeyadgasser.playground.di

import com.zeyadgasser.playground.tasks.data.db.DatabaseDriverFactory
import com.zeyadgasser.playground.tasks.data.db.DesktopDatabaseDriverFactory
import org.koin.dsl.module

val desktopModule = module {
    single<DatabaseDriverFactory> { DesktopDatabaseDriverFactory() }
}
