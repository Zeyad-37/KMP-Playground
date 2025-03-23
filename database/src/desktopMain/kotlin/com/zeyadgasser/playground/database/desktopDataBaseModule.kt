package com.zeyadgasser.playground.database

import org.koin.dsl.module

val desktopDataBaseModule = module {
    single<DatabaseDriverFactory> { DesktopDatabaseDriverFactory() }
}
