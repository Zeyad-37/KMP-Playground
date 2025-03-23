package com.zeyadgasser.playground.database

import org.koin.dsl.module

val iosDataBaseModule = module {
    single<DatabaseDriverFactory> { IOSDatabaseDriverFactory() }
}
